package com.styleshow.ui.new_post;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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
    private NewPostViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(NewPostViewModel.class);

        binding = ActivityNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.etCaption.addTextChangedListener(new AfterTextChangedTextWatcher(s -> {
            viewModel.captionChanged(s.toString());
        }));

        // TODO: image picker
    }
}