package com.styleshow.ui.user_profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityUserProfileBinding;
import com.styleshow.domain.model.UserProfile;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: back button

/**
 * To be honest this is basically the same as {@link com.styleshow.ui.profile.ProfileFragment}.
 * But I couldn't figure out the best way to extract the common code into a base/shared class so
 * I just copied it (along with layout & view-model) with some minor changes.
 */
@AndroidEntryPoint
public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var userProfile = (UserProfile) getIntent().getSerializableExtra(Constants.PROFILE_NAME);
        Timber.d("opened profile: %s", userProfile);

        var viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        viewModel.loadPosts(userProfile.getUid());

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);
        binding.setProfile(userProfile);
        binding.setViewModel(viewModel);

        viewModel.getPosts().observe(this, posts -> {
            if (posts == null)
                return;

            // Update the grid with the new posts
            binding.postPreviewGrid.setPosts(posts);
        });
    }
}
