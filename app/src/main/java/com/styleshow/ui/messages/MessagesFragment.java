package com.styleshow.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.databinding.FragmentMessagesBinding;
import com.styleshow.ui.select_chat.SelectChatActivity;
import dagger.hilt.android.AndroidEntryPoint;

// TODO: display recent conversations

@AndroidEntryPoint
public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private MessagesViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        viewModel.loadMessages();

        binding = FragmentMessagesBinding.inflate(inflater, container, false);

        // TODO: setup the recycler view

        binding.fabNewMessage.setOnClickListener(v -> {
            var intent = new Intent(requireContext(), SelectChatActivity.class);
            startActivity(intent);
        });

        // TODO (remove): this is a TEST!!
        binding.fabTestSendMessage.setOnClickListener(v -> {
            // sends to aaa@aaa.com
            viewModel.sendMessage("UEl9dsIOexVlZG010enL4VoL15f2", "Test!!");
        });

        viewModel.getLoadingState().observe(getViewLifecycleOwner(), loadingState -> {
            switch (loadingState) {
                case LOADING -> {
                    // Display progress indicator
                    binding.viewSwitcher.setDisplayedChild(1);
                }
                case SUCCESS_IDLE -> {
                    // Display messages
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
        viewModel = null;
    }
}
