package com.styleshow.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.databinding.FragmentProfileBinding;
import com.styleshow.ui.login.LoginActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel viewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        viewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.signOut.setOnClickListener(v -> {
            Log.d(TAG, "Signing out");
            viewModel.logout();

            var activity = getActivity();

            // TODO: switch to string res
            String message = "You have been signed out";
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();

            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
            activity.finish();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
