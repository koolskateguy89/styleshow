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
import com.styleshow.R;
import com.styleshow.databinding.FragmentProfileBinding;
import com.styleshow.ui.login.LoginActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: edit profile/settings activity (use a fab?)
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
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        binding.signOut.setOnClickListener(v -> {
            Timber.d("Signing out");
            viewModel.logout();

            Toast.makeText(getContext(), R.string.signed_out, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);

            if (getActivity() != null)
                getActivity().finish();
        });

        viewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts == null)
                return;

            // Update the grid with the new posts
            binding.postPreviewGrid.setPosts(posts);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
