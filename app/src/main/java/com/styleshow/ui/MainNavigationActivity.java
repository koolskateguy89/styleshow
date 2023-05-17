package com.styleshow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.styleshow.R;
import com.styleshow.common.ConnectivityObserver;
import com.styleshow.common.NetworkConnectivityObserver;
import com.styleshow.databinding.ActivityMainNavigationBinding;
import com.styleshow.no_network.NoNetworkActivity;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Essentially the main activity for the app. Handles top-level navigation. Also starts
 * the handling of the share action from other apps.
 */
@AndroidEntryPoint
public class MainNavigationActivity extends AppCompatActivity {

    private ActivityMainNavigationBinding binding;
    private NavController navController;
    private ConnectivityObserver connectivityObserver;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_navigation);
        NavigationUI.setupWithNavController(binding.navView, navController);

        compositeDisposable = new CompositeDisposable();

        connectivityObserver = new NetworkConnectivityObserver(this);
        compositeDisposable.add(connectivityObserver.observe().subscribe(status -> {
            switch (status) {
                case LOSING -> {
                    Timber.d("Network losing");
                    Toast.makeText(this, R.string.network_losing, Toast.LENGTH_LONG).show();
                }
                case LOST -> {
                    Timber.d("Network lost");
                    openNoNetworkActivity();
                }
            }
        }));
    }

    private void openNoNetworkActivity() {
        var intent = new Intent(this, NoNetworkActivity.class);
        startActivity(intent);
    }

    // This being invoked means the user clicked on share from another app
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String action = intent.getAction();
        String type = intent.getType();

        // TODO: use switch if having to handle multiple actions
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Timber.i("(onNewIntent) action = %s", intent.getAction());
                navController.navigate(R.id.navigation_home, intent.getExtras());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
