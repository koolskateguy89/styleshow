package com.styleshow.ui.user_profile;

import android.os.Bundle;
import android.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.styleshow.R;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityUserProfileBinding;
import com.styleshow.domain.model.Post;
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

        var openPost = registerForActivityResult(new PostActivity.OpenPostContract(), result -> {
            if (result == null)
                return;

            if (result instanceof PostActivity.PostResult.LikeChanged likeChanged) {
                int index = likeChanged.index;
                Post post = likeChanged.post;

                viewModel.postUpdated(index, post);
                binding.viewDynamicPosts.getAdapter().notifyItemChanged(index, post);
            } else if (result instanceof PostActivity.PostResult.PostDeleted postDeleted) {
                // Inform user of deletion
                Snackbar.make(binding.getRoot(), R.string.delete_post_success,
                                Snackbar.LENGTH_SHORT)
                        .show();

                int index = postDeleted.index;

                viewModel.postDeleted(index);
                binding.viewDynamicPosts.getAdapter().notifyItemRemoved(index);
            }
        });

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
        binding.viewDynamicPosts.setItemClickListener((index, post) -> {
            openPost.launch(new Pair<>(index, post));
        });

        // TODO: only show the button if the user is not me
        binding.btnMessage.setOnClickListener(v -> {
            // TODO: open user-specific message (coversation) activity
        });
    }
}
