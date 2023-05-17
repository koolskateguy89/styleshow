package com.styleshow.ui.post;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.styleshow.R;
import com.styleshow.adapters.CommentAdapter;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityPostBinding;
import com.styleshow.domain.model.Post;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: physical back button
// TODO: display number of likes
// TODO: on tablets, show image on left and actions,text,etc on right

// FIXME: when typing, comment et is not visible cos keyboard is covering it

// TODO: comment et action DONE

// TODO: if my post, allow delete

@AndroidEntryPoint
public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var post = (Post) getIntent().getSerializableExtra(Constants.POST_NAME);
        Timber.d("opened post: %s", post);

        viewModel = new ViewModelProvider(this).get(PostViewModel.class);
        viewModel.setPost(post);
        viewModel.loadComments();

        // A lot of actions are set in the layout xml using data binding
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        binding.viewPostActions.setOnDeleteClickListener(v -> {
            // TODO: confirm dialog then
            viewModel.deleteButtonClicked();
            // then finish() but with appropriate result - im not too sure what the result should be
            // need to check fragments that use this activity

            var data = new Intent();
            data.putExtra(Constants.POST_NAME, (Serializable) null);
            data.putExtra(Constants.POST_INDEX_NAME,
                    getIntent().getIntExtra(Constants.POST_INDEX_NAME, -1));

            setResult(RESULT_OK, data);
            finish();
        });

        // Setup comments recycler view
        var commentAdapter = new CommentAdapter(List.of());

        // Enable deleting comment on click if user has permission
        commentAdapter.setItemLongClickListener((index, comment) -> {
            // User doesn't have permission to delete comment
            if (!viewModel.canDeleteComment(comment))
                return;

            var builder = new AlertDialog.Builder(this)
                    .setTitle(R.string.delete_comment_dialog_title)
                    .setMessage(R.string.delete_comment_dialog_message)
                    // Add the buttons
                    .setPositiveButton(R.string.delete_comment_dialog_ok, (dialog, id) -> {
                        // User clicked OK button
                        viewModel.tryDeleteComment(comment).addOnSuccessListener(ignore -> {
                            Snackbar.make(binding.getRoot(), R.string.delete_comment_success,
                                            Snackbar.LENGTH_SHORT)
                                    .show();
                        });
                    })
                    .setNegativeButton(R.string.delete_comment_dialog_cancel, (dialog, id) -> {
                        // User cancelled the dialog (do nothing)
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        viewModel.getComments().observe(this, commentAdapter::setItems);
        binding.rvComments.setAdapter(commentAdapter);

        // Handle comments loading state
        viewModel.getCommentLoadingState().observe(this, loadingState -> {
            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcher.setDisplayedChild(1);
                }
                case SUCCESS_IDLE -> {
                    // Display comments
                    binding.viewSwitcher.setDisplayedChild(0);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Only return the post if it has changed
        if (!viewModel.hasPostChanged()) {
            super.onBackPressed();
            return;
        }

        var data = new Intent();
        data.putExtra(Constants.POST_NAME, viewModel.getPost().getValue());
        data.putExtra(Constants.POST_INDEX_NAME,
                getIntent().getIntExtra(Constants.POST_INDEX_NAME, -1));

        setResult(RESULT_OK, data);
        finish();
    }

    public static sealed abstract class PostResult permits PostResult.LikeChanged, PostResult.PostDeleted {
        public static final class LikeChanged extends PostResult {
            public final int index;
            public final @NonNull Post post;

            public LikeChanged(int index, @NonNull Post post) {
                this.index = index;
                this.post = post;
            }
        }

        public static final class PostDeleted extends PostResult {
            public final int index;

            public PostDeleted(int index) {
                this.index = index;
            }
        }
    }

    public static class OpenPostContract extends ActivityResultContract<Pair<Integer, Post>, PostResult> {

        @Override
        public @NonNull Intent createIntent(@NonNull Context context, Pair<Integer, Post> pair) {
            return new Intent(context, PostActivity.class)
                    .putExtra(Constants.POST_NAME, pair.second)
                    .putExtra(Constants.POST_INDEX_NAME, (int) pair.first);
        }

        @Override
        public PostResult parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK && intent != null) {
                int index = intent.getIntExtra(Constants.POST_INDEX_NAME, -1);
                Post post = (Post) intent.getSerializableExtra(Constants.POST_NAME);

                if (post == null) {
                    return new PostResult.PostDeleted(index);
                } else {
                    return new PostResult.LikeChanged(index, post);
                }
            }

            return null;
        }
    }
}
