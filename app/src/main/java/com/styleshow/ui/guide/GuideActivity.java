package com.styleshow.ui.guide;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.styleshow.databinding.ActivityGuideBinding;
import com.styleshow.databinding.ActivityRegisterBinding;

// TODO

public class GuideActivity extends AppCompatActivity {

    private ActivityGuideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.wv;
    }
}
