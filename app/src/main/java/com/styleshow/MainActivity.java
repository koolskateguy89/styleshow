package com.styleshow;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.styleshow.databinding.ActivityMainBinding;
import com.styleshow.domain.repository.LoginRepository;
import com.styleshow.ui.MainNavigationActivity;
import com.styleshow.ui.login.LoginActivity;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

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
        Timber.d("!loggedIn = %s", loggedIn);

        Intent intent;
        if (loggedIn) {
            Timber.d("Already logged in, going to Home");
            intent = new Intent(this, MainNavigationActivity.class);
        } else {
            Timber.d("Not logged in, going to Login");
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        // Finish the activity so the user can't go back to this with the back button
        finish();
    }
}
