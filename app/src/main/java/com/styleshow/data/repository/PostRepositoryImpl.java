package com.styleshow.data.repository;

import java.util.List;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.styleshow.data.remote.PostDataSource;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.PostRepository;

public class PostRepositoryImpl implements PostRepository {

    private final @NonNull PostDataSource postDataSource;

    public PostRepositoryImpl(@NonNull PostDataSource postDataSource) {
        this.postDataSource = postDataSource;
    }

    // TODO: impl. pagination
    @Override
    public Task<List<Post>> getAllPosts() {
        return postDataSource.getAllPosts();
    }

    @Override
    public Task<List<Post>> getPostsByUser(@NonNull String uid) {
        return postDataSource.getPostsByUser(uid);
    }

    @Override
    public Task<List<Post>> getPostsByUser(@NonNull UserProfile author) {
        return postDataSource.getPostsByUser(author);
    }

    @Override
    public Task<Void> likePost(@NonNull String postId) {
        return postDataSource.likePost(postId);
    }

    @Override
    public Task<Void> unlikePost(@NonNull String postId) {
        return postDataSource.unlikePost(postId);
    }

    @Override
    public Task<String> publishPost(
            @NonNull Uri imageUri,
            @NonNull String caption,
            @NonNull String shoeUrl
    ) {
        return postDataSource.publishPost(
                imageUri,
                caption,
                shoeUrl
        );
    }

    @Override
    public Task<Void> deletePost(@NonNull Post post) {
        return postDataSource.deletePost(post);
    }
}
