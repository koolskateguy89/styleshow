package com.styleshow.ui.guide;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.styleshow.databinding.ActivityGuideBinding;

/**
 * The activity containing the user guide.
 */
public class GuideActivity extends AppCompatActivity {

    private ActivityGuideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.wv.loadUrl("file:///android_asset/guide.html");
    }
}
