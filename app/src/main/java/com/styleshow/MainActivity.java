package com.styleshow;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.styleshow.databinding.ActivityMainBinding;
import com.styleshow.domain.repository.LoginRepository;
import com.styleshow.ui.MainNavigationActivity;
import com.styleshow.ui.login.LoginActivity;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Inject
    LoginRepository loginRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean loggedIn = loginRepository.isLoggedIn();
        Log.d("MainActivity", String.format("loggedIn = %s", loggedIn));

        loggedIn = false;

        Intent intent;
        if (loggedIn) {
            Log.d("MainActivity", "going to Home");
            intent = new Intent(this, MainNavigationActivity.class);
        } else {
            Log.d("MainActivity", "going to Login");
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        // Finish the activity so the user can't go back to this with the back button
        finish();
    }
}
