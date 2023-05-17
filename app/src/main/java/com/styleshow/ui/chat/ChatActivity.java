package com.styleshow.ui.chat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivityChatBinding;
import com.styleshow.domain.model.UserProfile;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        var receiver = (UserProfile) getIntent().getSerializableExtra(Constants.NAME_PROFILE);
        Timber.i("receiver = %s", receiver);

        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.setReceiver(receiver);
        viewModel.loadMessages();

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
