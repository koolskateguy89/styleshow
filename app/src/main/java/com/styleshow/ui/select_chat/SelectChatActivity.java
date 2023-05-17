package com.styleshow.ui.select_chat;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.common.Constants;
import com.styleshow.databinding.ActivitySelectChatBinding;
import com.styleshow.ui.chat.ChatActivity;
import dagger.hilt.android.AndroidEntryPoint;

// TODO: back button

/**
 * Displays a list of users, that the user can select to start a chat with.
 */
@AndroidEntryPoint
public class SelectChatActivity extends AppCompatActivity {

    private ActivitySelectChatBinding binding;
    private SelectChatViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SelectChatViewModel.class);
        viewModel.loadUsers();

        binding = ActivitySelectChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle loading state
        viewModel.getLoadingState().observe(this, loadingState -> {
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
            var intent = new Intent(this, ChatActivity.class)
                    .putExtra(Constants.NAME_PROFILE, user);
            startActivity(intent);
            finish();
        });

        binding.rvUsers.setAdapter(adapter);
        viewModel.getUsers().observe(this, adapter::setItems);
    }
}
