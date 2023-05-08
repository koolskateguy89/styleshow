package com.styleshow.ui.new_post;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.styleshow.common.AfterTextChangedTextWatcher;
import com.styleshow.databinding.ActivityNewPostBinding;
import dagger.hilt.android.AndroidEntryPoint;

/*
TODO
- [ ] add image picker
 */

@AndroidEntryPoint
public class NewPostActivity extends AppCompatActivity {

    private ActivityNewPostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.etCaption.addTextChangedListener(new AfterTextChangedTextWatcher(s -> {
            // TODO: handle caption change, use viewmodel i guess
        }));
    }
}
