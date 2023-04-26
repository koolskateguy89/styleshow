package com.styleshow.ui.messages;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.styleshow.R;
import com.styleshow.databinding.ActivityMessagesBinding;

public class MessagesActivity extends AppCompatActivity {

    ActivityMessagesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
