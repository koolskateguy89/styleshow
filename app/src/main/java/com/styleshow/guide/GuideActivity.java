package com.styleshow.guide;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.styleshow.databinding.ActivityRegisterBinding;

// TODO

public class GuideActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
