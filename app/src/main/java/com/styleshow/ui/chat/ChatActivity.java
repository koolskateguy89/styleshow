package com.styleshow.ui.chat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.databinding.ActivityChatBinding;
import com.styleshow.databinding.ActivityPostBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        // viewModel.loadMessages();

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle loading state
        viewModel.getLoadingState().observe(this, loadingState -> {
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

    }
}
