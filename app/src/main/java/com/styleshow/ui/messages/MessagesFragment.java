package com.styleshow.ui.messages;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.common.Constants;
import com.styleshow.databinding.FragmentMessagesBinding;
import com.styleshow.ui.chat.ChatActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MessagesFragment extends Fragment {

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

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
    }
}
