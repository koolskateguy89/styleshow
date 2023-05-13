package com.styleshow.ui.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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

        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
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

    // TODO?: This is only called if the activity has launchmode of singleTop
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // im not really sure what to do here
        Timber.d("onNewIntent: %s", intent);
    }

    public static class OpenPostContract extends ActivityResultContract<Pair<Integer, Post>, Pair<Integer, Post>> {

        @Override
        public @NonNull Intent createIntent(@NonNull Context context, Pair<Integer, Post> pair) {
            var intent = new Intent(context, PostActivity.class);
            intent.putExtra(Constants.POST_NAME, pair.second);
            intent.putExtra(Constants.POST_INDEX_NAME, (int) pair.first);
            return intent;
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
