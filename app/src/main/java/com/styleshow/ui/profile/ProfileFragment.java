package com.styleshow.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.styleshow.R;
import com.styleshow.databinding.FragmentProfileBinding;
import com.styleshow.ui.adapter.PostPreviewAdapter;
import com.styleshow.ui.login.LoginActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: edit profile/settings activity
// TODO: show profile picture

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel viewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        viewModel.loadProfile();

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        var lifecycleOwner = getViewLifecycleOwner();
        var activity = getActivity();

        // test text
        viewModel.getText().observe(lifecycleOwner, binding.textProfile::setText);

        binding.signOut.setOnClickListener(v -> {
            if (activity == null)
                return;

            Timber.d("Signing out");
            viewModel.logout();

            Toast.makeText(activity, R.string.signed_out, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);

            activity.finish();
        });

        viewModel.getLoadingState().observe(lifecycleOwner, state -> {
            Timber.d("Loading state: %s", state);
            switch (state) {
                case IDLE -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.wrapper.setVisibility(View.GONE);
                }
                case LOADING -> {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.wrapper.setVisibility(View.GONE);
                }
                case ERROR -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.wrapper.setVisibility(View.GONE);
                    //binding.error.setVisibility(View.VISIBLE);
                    // TODO: there "should ONLY" be an error if the user doesn't have access
                    // to internet
                    // i dont see why else there would be
                    // but that should be handled "globally" in the app
                    Toast.makeText(activity, "Could not load profile", Toast.LENGTH_LONG).show();
                }
                case SUCCESS_IDLE -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.wrapper.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getUserProfile().observe(lifecycleOwner, profile -> {
            if (profile == null)
                return;

            // Display the user's profile
            binding.username.setText(profile.getUsername());
            binding.bio.setText(profile.getBio());
        });

        // Post preview recycler view setup
        var postPreviewAdapter = new PostPreviewAdapter();
        binding.rvPosts.setAdapter(postPreviewAdapter);

        binding.rvPosts.setLayoutManager(new GridLayoutManager(activity, 3));

        // Update the adapter when the posts change
        viewModel.getPosts().observe(lifecycleOwner, posts -> {
            if (posts == null)
                return;

            postPreviewAdapter.setPosts(posts);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
