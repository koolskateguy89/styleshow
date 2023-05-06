package com.styleshow.ui.post;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.squareup.picasso.Picasso;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityPostBinding;
import com.styleshow.domain.model.Post;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: back button

/*
- [x] author display (PostAuthor.java)
- [ ] like button (heart - filled if liked)
 */

@AndroidEntryPoint
public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var post = (Post) getIntent().getSerializableExtra(Constants.POST_NAME);
        Timber.d("opened post: %s", post);

        var viewModel = new ViewModelProvider(this).get(PostViewModel.class);

        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setPost(post);
    }
}
