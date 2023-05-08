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

// TODO: edit profile/settings activity (use a FAB?)
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

        binding.btnSignOut.setOnClickListener(v -> {
            Timber.d("Signing out");
            viewModel.logout();

            Toast.makeText(getContext(), R.string.signed_out, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);

            if (getActivity() != null)
                getActivity().finish();
        });

        viewModel.getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            if (loadingState == null)
                return;

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

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
