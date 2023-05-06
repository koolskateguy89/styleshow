package com.styleshow.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.data.remote.PostDataSource;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.repository.PostRepository;

public class PostRepositoryImpl implements PostRepository {

    private final @NonNull PostDataSource postDataSource;

    public PostRepositoryImpl(@NonNull PostDataSource postDataSource) {
        this.postDataSource = postDataSource;
    }

    // TODO: getAllPosts (maybe paginated)

    @Override
    public Task<List<Post>> getPostsByUser(@NonNull String uid) {
        return postDataSource.getPostsByUser(uid);
    }
}
