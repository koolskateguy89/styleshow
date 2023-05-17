package com.styleshow.ui.post;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupMenu;
import android.widget.ShareActionProvider;
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
import com.styleshow.domain.model.Comment;
import com.styleshow.domain.model.Post;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// FIXME: when typing, comment et is not visible cos keyboard is covering it

@AndroidEntryPoint
public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private PostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var post = (Post) getIntent().getSerializableExtra(Constants.NAME_POST);
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
            confirmDeletePostWithDialog();
        });

        binding.viewPostActions.setOnShareClickListener(this::showSharePopupMenu);

        binding.etComment.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.tryPostComment();
            }
            return false;
        });

        // Setup comments recycler view
        var commentAdapter = new CommentAdapter(List.of());

        // Enable deleting comment on click if user has permission
        commentAdapter.setItemLongClickListener((index, comment) -> {
            // User doesn't have permission to delete comment
            if (!viewModel.canDeleteComment(comment))
                return;

            confirmDeleteCommentWithDialog(comment);
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
        data.putExtra(Constants.NAME_POST, viewModel.getPost().getValue());
        data.putExtra(Constants.NAME_POST_INDEX,
                getIntent().getIntExtra(Constants.NAME_POST_INDEX, -1));

        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Use intents and a share action provider to open the shoe url in the browser.
     */
    private void showSharePopupMenu(View v) {
        String shoeUrl = viewModel.getPost().getValue().getShoeUrl();

        // Create a share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shoeUrl);

        var shareIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(shoeUrl));

        // Set up the ShareActionProvider
        var shareActionProvider = new ShareActionProvider(this);
        shareActionProvider.setShareIntent(shareIntent);

        shareActionProvider.setOnShareTargetSelectedListener((source, intent) -> {
            // Handle share target selected
            startActivity(shareIntent2);
            return false;
        });

        var popup = new PopupMenu(v.getContext(), v);
        popup.inflate(R.menu.post_share_menu);

        MenuItem openItem = popup.getMenu().findItem(R.id.menu_item_open);
        openItem.setActionProvider(shareActionProvider);

        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_item_open) {
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void confirmDeletePostWithDialog() {
        var builder = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_post_dialog_title)
                .setMessage(R.string.delete_post_dialog_message)
                // Add the buttons
                .setPositiveButton(R.string.dialog_ok, (dialog, id) -> {
                    // User clicked OK button

                    // Delete the post
                    viewModel.deletePost();

                    // Return to previous activity
                    var data = new Intent();
                    data.putExtra(Constants.NAME_POST, (Serializable) null);
                    data.putExtra(Constants.NAME_POST_INDEX,
                            getIntent().getIntExtra(Constants.NAME_POST_INDEX, -1));

                    setResult(RESULT_OK, data);
                    finish();
                })
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> {
                    // User cancelled the dialog (do nothing)
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void confirmDeleteCommentWithDialog(@NonNull Comment comment) {
        var builder = new AlertDialog.Builder(this)
                .setTitle(R.string.delete_comment_dialog_title)
                .setMessage(R.string.delete_comment_dialog_message)
                // Add the buttons
                .setPositiveButton(R.string.dialog_ok, (dialog, id) -> {
                    // User clicked OK button
                    // Delete the comment
                    viewModel.tryDeleteComment(comment).addOnSuccessListener(ignore -> {
                        // Inform user of deletion
                        Snackbar.make(binding.getRoot(), R.string.delete_comment_success,
                                        Snackbar.LENGTH_SHORT)
                                .show();
                    });
                })
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> {
                    // User cancelled the dialog (do nothing)
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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
                    .putExtra(Constants.NAME_POST, pair.second)
                    .putExtra(Constants.NAME_POST_INDEX, (int) pair.first);
        }

        @Override
        public PostResult parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK && intent != null) {
                int index = intent.getIntExtra(Constants.NAME_POST_INDEX, -1);
                Post post = (Post) intent.getSerializableExtra(Constants.NAME_POST);

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
