package com.styleshow.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.styleshow.R;
import com.styleshow.databinding.ActivityMainNavigationBinding;
import dagger.hilt.android.AndroidEntryPoint;

// TODO?: try and store last tab in shared prefs

@AndroidEntryPoint
public class MainNavigationActivity extends AppCompatActivity {

    private ActivityMainNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_navigation);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}
