package com.styleshow.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.data.remote.CommentDataSource;
import com.styleshow.domain.model.Comment;
import com.styleshow.domain.repository.CommentRepository;

public class CommentRepositoryImpl implements CommentRepository {

    private final @NonNull CommentDataSource dataSource;

    public CommentRepositoryImpl(@NonNull CommentDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Task<List<Comment>> getCommentsForPost(@NonNull String postId) {
        return dataSource.getCommentsForPost(postId);
    }

    @Override
    public Task<?> postComment(@NonNull String postId, @NonNull String content) {
        return dataSource.postComment(postId, content);
    }

    @Override
    public Task<Void> deleteComment(@NonNull String postId, @NonNull String commentId) {
        return dataSource.deleteComment(postId, commentId);
    }
}
