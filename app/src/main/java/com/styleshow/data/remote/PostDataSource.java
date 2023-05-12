package com.styleshow.data.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import com.styleshow.data.remote.dto.PostDto;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;
import timber.log.Timber;

public class PostDataSource {

    private final CollectionReference mPostRef;
    private final StorageReference mPostImagesRef;
    private final LoginDataSource mLoginDataSource;
    private final UserProfileDataSource mUserProfileDataSource;

    public PostDataSource(
            FirebaseFirestore firestore,
            FirebaseStorage storage,
            LoginDataSource loginDataSource,
            UserProfileDataSource userProfileDataSource
    ) {
        mPostRef = firestore.collection("posts");
        mPostImagesRef = storage.getReference("postImages");
        mLoginDataSource = loginDataSource;
        mUserProfileDataSource = userProfileDataSource;
    }

    private static @Nullable PostDto getPostDtoFromDocument(DocumentSnapshot document) {
        var postDto = document.toObject(PostDto.class);

        if (postDto != null) {
            postDto.id = document.getId();
        }

        return postDto;
    }

    // TODO: impl. pagination
    @SuppressWarnings("unchecked")
    public Task<List<Post>> getAllPosts() {
        // This can only be called when the user is logged in so we can safely get the uid
        @SuppressWarnings("ConstantConditions")
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        var profilesTask = mUserProfileDataSource.getAllProfiles()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var profiles = task.getResult();

                    return profiles.stream()
                            .collect(Collectors.toMap(UserProfile::getUid, Function.identity()));
                });

        var postDtosTask = mPostRef
                .orderBy("postedAt", Query.Direction.DESCENDING) // newest first
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documents = task.getResult().getDocuments();

                    return documents.stream()
                            .map(PostDataSource::getPostDtoFromDocument)
                            .filter(Objects::nonNull)
                            ;
                });

        return Tasks.whenAllComplete(profilesTask, postDtosTask)
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var tasks = task.getResult();

                    var profiles = (Map<String, UserProfile>) tasks.get(0).getResult();
                    var postDtos = (Stream<PostDto>) tasks.get(1).getResult();

                    return postDtos
                            .map(postDto -> postDto.toPost(profiles.get(postDto.uid), currentUserId))
                            .collect(Collectors.toList());
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

        return mPostRef.whereEqualTo("uid", author.getUid())
                .orderBy("postedAt", Query.Direction.DESCENDING) // newest first
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documents = task.getResult().getDocuments();

                    return documents.stream()
                            .map(PostDataSource::getPostDtoFromDocument)
                            .filter(Objects::nonNull)
                            .map(postDto -> postDto.toPost(author, currentUserId))
                            .collect(Collectors.toList());
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

        return mPostRef.document(postId)
                .update("likes", FieldValue.arrayUnion(currentUserId))
                .addOnSuccessListener(aVoid -> {
                    Timber.d("liked post %s", postId);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "failed to like post %s", postId);
                });
    }

    public Task<Void> unlikePost(@NonNull String postId) {
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        return mPostRef.document(postId)
                .update("likes", FieldValue.arrayRemove(currentUserId))
                .addOnSuccessListener(aVoid -> {
                    Timber.d("unliked post %s", postId);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "failed to unlike post %s", postId);
                });
    }

    public Task<String> publishPost(
            @NonNull Uri imageUri,
            @NonNull String caption,
            @NonNull String shoeUrl // might have to change this to a URI or smthn, need to upload to storage
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
            data.put("uid", currentUserId);
            data.put("caption", caption);
            data.put("imageUrl", imageDownloadUri.toString());
            data.put("imageId", imageId);
            data.put("shoeUrl", shoeUrl);
            data.put("postedAt", FieldValue.serverTimestamp());
            data.put("likes", List.of());

            return mPostRef.add(data);
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
}
