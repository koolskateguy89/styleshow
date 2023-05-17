package com.styleshow.ui.post;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.Comment;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.repository.CommentRepository;
import com.styleshow.domain.repository.LoginRepository;
import com.styleshow.domain.repository.PostRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class PostViewModel extends ViewModel {

    private final @NonNull LoginRepository loginRepository;
    private final @NonNull PostRepository postRepository;
    private final @NonNull CommentRepository commentRepository;

    private final MutableLiveData<Post> mPost = new MutableLiveData<>();
    private final MediatorLiveData<Boolean> mDeletable = new MediatorLiveData<>(false);

    private final MutableLiveData<List<Comment>> mComments = new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mCommentLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    private final MutableLiveData<String> mComment = new MutableLiveData<>("");

    private volatile boolean postingComment = false;
    private volatile boolean deletingComment = false;

    private boolean originalLikeState;

    @Inject
    public PostViewModel(
            @NonNull LoginRepository loginRepository,
            @NonNull PostRepository postRepository,
            @NonNull CommentRepository commentRepository
    ) {
        this.loginRepository = loginRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;

        mDeletable.addSource(mPost, post -> {
            if (post == null) {
                mDeletable.setValue(false);
            } else {
                String currentUserId = loginRepository.getCurrentUser().getUid();
                mDeletable.setValue(post.getAuthor().getUid().equals(currentUserId));
            }
        });
    }

    public LiveData<Post> getPost() {
        return mPost;
    }

    @MainThread
    public void setPost(Post post) {
        mPost.setValue(post);
        originalLikeState = post.isLiked();
    }

    public LiveData<Boolean> getDeletable() {
        return mDeletable;
    }

    public LiveData<List<Comment>> getComments() {
        return mComments;
    }

    public LiveData<LoadingState> getCommentLoadingState() {
        return mCommentLoadingState;
    }

    /**
     * Returning the MutableLiveData because of two-way data binding.
     */
    public MutableLiveData<String> getComment() {
        return mComment;
    }

    @MainThread
    public void setComment(String comment) {
        mComment.setValue(comment);
    }

    @MainThread
    public void loadComments() {
        mCommentLoadingState.setValue(LoadingState.LOADING);

        commentRepository.getCommentsForPost(mPost.getValue().getId())
                .addOnSuccessListener(comments -> {
                    Timber.d("loaded comments: %s", comments);
                    mComments.setValue(comments);
                    mCommentLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                })
                .addOnFailureListener(e -> {
                    mCommentLoadingState.setValue(LoadingState.ERROR);
                });
    }

    public boolean hasPostChanged() {
        return originalLikeState != mPost.getValue().isLiked();
    }

    @MainThread
    public void likeButtonClicked() {
        var post = mPost.getValue();
        boolean isLiked = post.isLiked();

        if (isLiked) {
            // Unlike
            postRepository.unlikePost(post.getId());
        } else {
            // Like
            postRepository.likePost(post.getId());
        }

        // Update the post - optimistic update
        this.mPost.setValue(post.withLiked(!isLiked));
    }

    @MainThread
    public void deleteButtonClicked() {
        var post = mPost.getValue();
        if (post == null) {
            return;
        }

        boolean canDelete = mDeletable.getValue();
        if (!canDelete) {
            return;
        }

        // TODO: actually delete
        Timber.d("deleting post: %s", post);
    }

    public void tryPostComment() {
        var content = mComment.getValue();
        if (content == null || content.isBlank()) {
            return;
        }

        var post = mPost.getValue();
        if (post == null) {
            return;
        }

        if (postingComment) {
            Timber.w("already posting comment");
            return;
        }

        postingComment = true;

        commentRepository.postComment(post.getId(), content)
                .addOnCompleteListener(ignore -> {
                    postingComment = false;
                })
                .addOnSuccessListener(ignore -> {
                    Timber.d("posted comment: %");
                    mComment.setValue("");
                    loadComments();
                });
    }

    /**
     * Returns {@code true} if the current user can delete the comment.
     * <p>
     * They can delete it if they are the author of the comment or
     * the author of the post.
     */
    public boolean canDeleteComment(Comment comment) {
        String currentUserId = loginRepository.getCurrentUser().getUid();
        String postAuthorId = mPost.getValue().getAuthor().getUid();

        return currentUserId.equals(comment.getAuthorId())
                || currentUserId.equals(postAuthorId);
    }

    public Task<Void> tryDeleteComment(Comment comment) {
        String postId = mPost.getValue().getId();

        if (!canDeleteComment(comment))
            return Tasks.forCanceled();

        if (deletingComment) {
            Timber.w("already deleting comment");
            return Tasks.forCanceled();
        }

        deletingComment = true;

        return commentRepository.deleteComment(postId, comment.getId())
                .addOnCompleteListener(ignore -> {
                    deletingComment = false;
                })
                .addOnSuccessListener(ignore -> {
                    Timber.d("deleted comment: %s", comment);
                    loadComments();
                });
    }
}
