package com.styleshow.data.remote;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.data.remote.dto.PostDto;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;
import timber.log.Timber;

public class PostDataSource {

    private final CollectionReference mPosts;
    private final LoginDataSource mLoginDataSource;
    private final UserProfileDataSource mUserProfileDataSource;

    public PostDataSource(
            FirebaseFirestore firestore,
            LoginDataSource loginDataSource,
            UserProfileDataSource userProfileDataSource
    ) {
        mPosts = firestore.collection("posts");
        mLoginDataSource = loginDataSource;
        mUserProfileDataSource = userProfileDataSource;
    }

    // TODO: getAllPosts (maybe paginated)

    public Task<List<Post>> getPostsByUser(@NonNull UserProfile author) {
        // This can only be called when the user is logged in so we can safely get the uid
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        return mPosts.whereEqualTo("uid", author.getUid())
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documents = task.getResult().getDocuments();

                    return documents.stream()
                            .map(document -> {
                                var postDto = document.toObject(PostDto.class);

                                if (postDto != null) {
                                    postDto.id = document.getId();
                                }

                                return postDto;
                            })
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
}
