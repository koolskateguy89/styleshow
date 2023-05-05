package com.styleshow.data.remote;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.data.remote.dto.PostDto;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;

public class PostDataSource {

    private static final String TAG = "PostDataSource";

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

    private Task<List<Post>> getPostsByAuthor(@NonNull UserProfile author) {
        // This can only be called when the user is logged in so we can safely get the uid
        String currentUserId = mLoginDataSource.getCurrentUser().getUid();

        return mPosts.whereEqualTo("uid", author.getUid())
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var documents = task.getResult().getDocuments();

                    var posts = documents.stream()
                            .map(document -> {
                                var postDto = document.toObject(PostDto.class);

                                if (postDto != null) {
                                    postDto.id = document.getId();
                                    postDto.author = author;
                                }

                                return postDto;
                            })
                            .peek(postDto -> Log.d(TAG, "postDto: " + postDto))
                            .filter(Objects::nonNull)
                            .map(postDto -> postDto.toPost(currentUserId))
                            .collect(Collectors.toList());

                    Log.i(TAG, "posts: " + posts);

                    return posts;
                })
                ;
    }

    public Task<List<Post>> getPostsByUser(@NonNull String uid) {
        return mUserProfileDataSource.getProfileForUid(uid)
                .continueWithTask(profileTask -> {
                    if (!profileTask.isSuccessful())
                        return null;

                    var authorProfile = profileTask.getResult();

                    return getPostsByAuthor(authorProfile);
                });
    }

}
