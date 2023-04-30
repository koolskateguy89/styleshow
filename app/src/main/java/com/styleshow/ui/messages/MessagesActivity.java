package com.styleshow.ui.messages;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import com.styleshow.R;
import com.styleshow.databinding.ActivityMessagesBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MessagesActivity extends AppCompatActivity {

    MessagesViewModel viewModel;
    ActivityMessagesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
    }
}
