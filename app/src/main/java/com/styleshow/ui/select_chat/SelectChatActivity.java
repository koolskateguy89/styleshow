package com.styleshow.ui.select_chat;

import java.util.List;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.adapters.ProfilePreviewAdapter;
import com.styleshow.databinding.ActivitySelectChatBinding;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: back button

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

        // TODO: setup users recycler view
        var adapter = new ProfilePreviewAdapter(List.of());

        adapter.setItemClickListener((index, user) -> {
            // TODO: open activity to message that user, and finish this one
            // or maybe make this an activity result?
            Timber.d("Clicked on user %s", user);
        });

        binding.rvUsers.setAdapter(adapter);

        viewModel.getUsers().observe(this, adapter::setItems);
    }
}
