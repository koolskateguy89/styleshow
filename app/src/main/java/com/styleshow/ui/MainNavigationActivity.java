package com.styleshow.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.styleshow.R;
import com.styleshow.databinding.ActivityMainNavigationBinding;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainNavigationActivity extends AppCompatActivity {

    private ActivityMainNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPrefs = getPreferences(MODE_PRIVATE);
        String lastDestinationKey = getString(R.string.sp_last_destination_key);

        @IdRes int lastDestinationId = sharedPrefs.getInt(lastDestinationKey, R.id.navigation_home);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_navigation);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Open last tab
        if (lastDestinationId != R.id.navigation_home)
            navController.navigate(lastDestinationId);

        // Save opened tab to shared prefs
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            @IdRes int id = destination.getId();

            sharedPrefs.edit()
                    .putInt(lastDestinationKey, id)
                    .apply();
        });
    }
}
