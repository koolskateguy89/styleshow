package com.styleshow.ui.user_profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityUserProfileBinding;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.ui.post.PostActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: back button

// TODO: if not me, message button

/**
 * To be honest this is basically the same as {@link com.styleshow.ui.profile.ProfileFragment}.
 * But I couldn't figure out the best way to extract the common code into a base/shared class so
 * I just copied it (along with layout & view-model) with some minor changes.
 */
@AndroidEntryPoint
public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;
    private UserProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var userProfile = (UserProfile) getIntent().getSerializableExtra(Constants.PROFILE_NAME);
        Timber.d("opened profile: %s", userProfile);

        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        viewModel.loadPosts(userProfile.getUid());

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setLifecycleOwner(this);
        binding.setProfile(userProfile);
        binding.setViewModel(viewModel);

        viewModel.getLoadingState().observe(this, loadingState -> {
            if (loadingState == null)
                return;

            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcher.setDisplayedChild(0);
                }
                case SUCCESS_IDLE -> {
                    // Display posts
                    binding.viewSwitcher.setDisplayedChild(1);
                }
            }
        });

        // Open post (fullscreen) on click
        binding.viewDynamicPosts.setItemClickListener(post -> {
            var intent = new Intent(this, PostActivity.class);
            intent.putExtra(Constants.POST_NAME, post);
            startActivity(intent);
        });
    }
}
