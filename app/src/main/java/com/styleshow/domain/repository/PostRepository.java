package com.styleshow.domain.repository;

import java.util.List;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;

public interface PostRepository {

    // TODO: impl. pagination
    Task<List<Post>> getAllPosts();

    Task<List<Post>> getPostsByUser(@NonNull String uid);

    Task<List<Post>> getPostsByUser(@NonNull UserProfile author);

    Task<Void> likePost(@NonNull String postId);

    Task<Void> unlikePost(@NonNull String postId);

    Task<String> publishPost(
            @NonNull Uri imageUri,
            @NonNull String caption,
            @NonNull String shoeUrl
    );

    Task<Void> deletePost(@NonNull Post post);
}
