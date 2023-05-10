package com.styleshow.ui.post;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityPostBinding;
import com.styleshow.domain.model.Post;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: back button

/*
- [ ] like button (heart - filled if liked)
- actions
- comment button
 */

// TODO: display number of likes

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
}
