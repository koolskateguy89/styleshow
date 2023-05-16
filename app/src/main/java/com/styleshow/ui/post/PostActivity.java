package com.styleshow.ui.post;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.adapters.CommentAdapter;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityPostBinding;
import com.styleshow.domain.model.Post;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: physical back button
// TODO: display number of likes
// TODO: on tablets, show image on left and actions,text,etc on right

// TODO: investigate launchMode

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

        // Setup comments recycler view
        var commentAdapter = new CommentAdapter(List.of());
        commentAdapter.setItemClickListener((index, comment) -> {
            // TODO: confirm delete dialog if is my comment
            Toast.makeText(this, comment.getContent(), Toast.LENGTH_SHORT).show();

            // TODO: viewmodel delete comment
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

    public static class OpenPostContract extends ActivityResultContract<Pair<Integer, Post>, Pair<Integer, Post>> {

        @Override
        public @NonNull Intent createIntent(@NonNull Context context, Pair<Integer, Post> pair) {
            return new Intent(context, PostActivity.class)
                    .putExtra(Constants.POST_NAME, pair.second)
                    .putExtra(Constants.POST_INDEX_NAME, (int) pair.first);
        }

        @Override
        public Pair<Integer, Post> parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK && intent != null) {
                int index = intent.getIntExtra(Constants.POST_INDEX_NAME, -1);
                Post post = (Post) intent.getSerializableExtra(Constants.POST_NAME);

                return new Pair<>(index, post);
            }

            return null;
        }
    }
}
