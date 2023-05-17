package com.styleshow.ui.messages;

import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.R;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.common.Constants;
import com.styleshow.databinding.FragmentMessagesBinding;
import com.styleshow.ui.chat.ChatActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MessagesFragment extends Fragment {

    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Can post notifications.
                } else {
                    // Inform user that no notifications will be sent.
                    Toast.makeText(requireContext(), R.string.no_notifications, Toast.LENGTH_LONG).show();
                }
            });
    private FragmentMessagesBinding binding;
    private MessagesViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        viewModel.loadUsers();

        binding = FragmentMessagesBinding.inflate(inflater, container, false);

        // Handle loading state
        viewModel.getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcher.setDisplayedChild(1);
                }
                case SUCCESS_IDLE -> {
                    // Display users
                    binding.viewSwitcher.setDisplayedChild(0);
                }
            }
        });

        // Setup users recycler view
        var adapter = new ProfilePreviewAdapter(List.of());
        adapter.setItemClickListener((index, user) -> {
            var intent = new Intent(requireContext(), ChatActivity.class)
                    .putExtra(Constants.NAME_PROFILE, user);
            startActivity(intent);
        });

        binding.rvUsers.setAdapter(adapter);
        viewModel.getUsers().observe(getViewLifecycleOwner(), adapter::setItems);

        // Request notification permission
        askNotificationPermission();

        return binding.getRoot();
    }

    /**
     * Ask for permission to send notifications.
     *
     * @see <a href="https://firebase.google.com/docs/cloud-messaging/android/client#request-permission13">https://firebase.google.com/docs/cloud-messaging/android/client#request-permission13</a>
     */
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Can post notifications
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
                var builder = new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.notifications_dialog_title)
                        .setMessage(R.string.notifications_dialog_message)
                        // Add the buttons
                        .setPositiveButton(R.string.notifications_dialog_ok, (dialog, id) -> {
                            // User clicked OK button, ask for the permission
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                        })
                        .setNegativeButton(R.string.notifications_dialog_cancel, (dialog, id) -> {
                            // User cancelled the dialog (do nothing)
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }
}
