package com.styleshow.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.snackbar.Snackbar;
import com.styleshow.R;
import com.styleshow.databinding.FragmentProfileBinding;
import com.styleshow.domain.model.Post;
import com.styleshow.ui.login.LoginActivity;
import com.styleshow.ui.post.PostActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: edit profile/settings activity

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.loadProfile();

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
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

        binding.btnSignOut.setOnClickListener(v -> {
            Timber.d("Signing out");
            viewModel.logout();

            Toast.makeText(requireContext(), R.string.signed_out, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);

            requireActivity().finish();
        });

        viewModel.getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcher.setDisplayedChild(1);
                }
                case SUCCESS_IDLE -> {
                    // Display profile & posts
                    binding.viewSwitcher.setDisplayedChild(0);
                }
            }
        });

        // Open post (fullscreen) on click
        binding.viewDynamicPosts.setItemClickListener((index, post) -> {
            openPost.launch(new Pair<>(index, post));
        });

        binding.btnEditProfile.setOnClickListener(v -> {
            // TODO
        });

        binding.btnOpenSettings.setOnClickListener(v -> {
            // TODO
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }
}
