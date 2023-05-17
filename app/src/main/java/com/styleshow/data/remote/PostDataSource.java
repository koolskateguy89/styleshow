package com.styleshow.data.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.styleshow.common.Constants;
import com.styleshow.common.FirebaseUtils;
import com.styleshow.data.remote.dto.PostDto;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;
import timber.log.Timber;

public class PostDataSource {

    private final CollectionReference mPostsRef;
    private final StorageReference mPostImagesRef;

    private final LoginDataSource mLoginDataSource;
    private final UserProfileDataSource mUserProfileDataSource;

    private final Executor executor = Executors.newWorkStealingPool();

    public PostDataSource(
            @NonNull FirebaseFirestore firestore,
            @NonNull FirebaseStorage storage,
            @NonNull LoginDataSource loginDataSource,
            @NonNull UserProfileDataSource userProfileDataSource
    ) {
        mPostsRef = firestore.collection(Constants.Post.COLLECTION_NAME);
        mPostImagesRef = storage.getReference(Constants.Post.IMAGES_STORAGE_PATH);
        mLoginDataSource = loginDataSource;
        mUserProfileDataSource = userProfileDataSource;
    }

    // TODO: impl. pagination
    public Task<List<Post>> getAllPosts() {
        // This can only be called when the user is logged in so we can safely get the uid
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        return mPostsRef
                .orderBy(Constants.Post.FIELD_POSTED_AT, Query.Direction.DESCENDING) // newest first
                .get()
                .continueWith(executor, task -> {
                    if (!task.isSuccessful())
                        return null;

                    var querySnapshot = task.getResult();

                    return querySnapshot.toObjects(PostDto.class);
                })
                .continueWithTask(executor, task -> {
                    if (!task.isSuccessful() && task.getException() != null)
                        return Tasks.forException(task.getException());

                    var postDtos = task.getResult();

                    var uids = postDtos.stream()
                            .map(postDto -> postDto.uid)
                            .distinct()
                            .collect(Collectors.toList());

                    return mUserProfileDataSource
                            .getProfilesForUids(uids)
                            .continueWith(task1 -> {
                                if (!task1.isSuccessful())
                                    return null;

                                var profiles = task1.getResult();

                                return profiles.stream()
                                        .collect(Collectors.toMap(UserProfile::getUid, Function.identity()));
                            })
                            .continueWith(task1 -> {
                                if (!task1.isSuccessful())
                                    return null;

                                var profilesMap = task1.getResult();

                                return postDtos
                                        .stream()
                                        .map(postDto -> postDto.toPost(profilesMap.get(postDto.uid), currentUserId))
                                        .collect(Collectors.toList())
                                        ;
                            });
                })
                .addOnSuccessListener(posts -> {
                    Timber.d("All %d posts: %s", posts.size(), posts);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "failed to get all posts");
                });
    }

    public Task<List<Post>> getPostsByUser(@NonNull UserProfile author) {
        // This can only be called when the user is logged in so we can safely get the uid
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        return mPostsRef.whereEqualTo(Constants.Post.FIELD_AUTHOR_UID, author.getUid())
                .orderBy(Constants.Post.FIELD_POSTED_AT, Query.Direction.DESCENDING) // newest first
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documents = task.getResult().getDocuments();

                    var querySnapshot = task.getResult();

                    return querySnapshot.toObjects(PostDto.class)
                            .stream()
                            .map(postDto -> postDto.toPost(author, currentUserId))
                            .collect(Collectors.toList())
                            ;
                })
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        var posts = task.getResult();
                        Timber.d("posts: %s", posts);
                    } else {
                        Timber.w(task.getException(), "failed to get posts for %s", author.getUid());
                    }
                })
                ;
    }

    public Task<List<Post>> getPostsByUser(@NonNull String uid) {
        return mUserProfileDataSource.getProfileForUid(uid)
                .continueWithTask(profileTask -> {
                    if (!profileTask.isSuccessful())
                        return null;

                    var authorProfile = profileTask.getResult();

                    return getPostsByUser(authorProfile);
                });
    }

    public Task<Void> likePost(@NonNull String postId) {
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        return mPostsRef.document(postId)
                .update(Constants.Post.FIELD_LIKES, FieldValue.arrayUnion(currentUserId))
                .addOnSuccessListener(ignore -> {
                    Timber.d("liked post %s", postId);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "failed to like post %s", postId);
                });
    }

    public Task<Void> unlikePost(@NonNull String postId) {
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        return mPostsRef.document(postId)
                .update(Constants.Post.FIELD_LIKES, FieldValue.arrayRemove(currentUserId))
                .addOnSuccessListener(ignore -> {
                    Timber.d("unliked post %s", postId);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "failed to unlike post %s", postId);
                });
    }

    public Task<String> publishPost(
            @NonNull Uri imageUri,
            @NonNull String caption,
            @NonNull String shoeUrl
    ) {
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        String imageId = UUID.randomUUID().toString();
        var imageRef = mPostImagesRef.child(imageId);

        var imageUploadTask = imageRef.putFile(imageUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful())
                        return null;

                    return imageRef.getDownloadUrl();
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to upload image");
                });

        return imageUploadTask.continueWithTask(imageTask -> {
            if (!imageTask.isSuccessful())
                return null;

            var imageDownloadUri = imageTask.getResult();

            Map<String, Object> data = new HashMap<>();
            data.put(Constants.Post.FIELD_AUTHOR_UID, currentUserId);
            data.put(Constants.Post.FIELD_CAPTION, caption);
            data.put(Constants.Post.FIELD_IMAGE_URL, imageDownloadUri.toString());
            data.put(Constants.Post.FIELD_IMAGE_ID, imageId);
            data.put(Constants.Post.FIELD_SHOE_URL, shoeUrl);
            data.put(Constants.Post.FIELD_POSTED_AT, FieldValue.serverTimestamp());
            data.put(Constants.Post.FIELD_LIKES, List.of());

            return mPostsRef.add(data);
        }).continueWith(postsTask -> {
            if (!postsTask.isSuccessful())
                return null;

            var documentReference = postsTask.getResult();
            return documentReference.getId();
        }).addOnSuccessListener(postId -> {
            Timber.d("Published new post with id '%s'", postId);
        }).addOnFailureListener(e -> {
            Timber.w(e, "Failed to publish post");
        });
    }

    public Task<Void> deletePost(@NonNull Post post) {
        Timber.d("Deleting post %s", post);

        var imageId = post.getImageId();
        Task<?> deleteImageTask = null;

        if (imageId != null) {
            var imageRef = mPostImagesRef.child(imageId);

            deleteImageTask = imageRef.delete()
                    .addOnSuccessListener(ignore -> {
                        Timber.d("Deleted image %s", imageId);
                    })
                    .addOnFailureListener(e -> {
                        Timber.w(e, "Failed to delete image %s", imageId);
                    });
        }

        var postRef = mPostsRef.document(post.getId());
        var postCommentsRef = postRef.collection(Constants.Comment.COLLECTION_NAME);

        // Run in background thread
        executor.execute(() -> {
            Timber.i("Deleting comments for post %s", post.getId());
            try {
                FirebaseUtils.deleteCollection(postCommentsRef, 20);
                Timber.i("Finished deleting comments for post %s", post.getId());
            } catch (Exception e) {
                Timber.w(e, "Error deleting comments for post %s", post.getId());
            }
        });

        var deletePostTask = postRef
                .delete()
                .addOnSuccessListener(ignore -> {
                    Timber.d("Deleted post %s", post.getId());
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to delete post %s", post.getId());
                });

        return Tasks.whenAll(deleteImageTask, deletePostTask);
    }
}
