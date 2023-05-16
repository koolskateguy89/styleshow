package com.styleshow.domain.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.Comment;

public interface CommentRepository {

    Task<List<Comment>> getCommentsForPost(@NonNull String postId);

    Task<?> postComment(@NonNull String postId, @NonNull String content);

    Task<Void> deleteComment(@NonNull String postId, @NonNull String commentId);
}
