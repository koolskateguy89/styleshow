package com.styleshow.ui;

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
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import timber.log.Timber;

@AndroidEntryPoint
public class MainNavigationActivity extends AppCompatActivity {

    private ActivityMainNavigationBinding binding;
    private ConnectivityObserver connectivityObserver;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_navigation);
        NavigationUI.setupWithNavController(binding.navView, navController);

        compositeDisposable = new CompositeDisposable();

        connectivityObserver = new NetworkConnectivityObserver(this);
        compositeDisposable.add(connectivityObserver.observe().subscribe(status -> {
            // TODO: no network activity
            // in there, will need to not allow them to exit until they have network
            // and when they do, auto finish that activity
            switch (status) {
                case AVAILABLE -> {
                    Timber.d("Network available");
                }
                case UNAVAILABLE -> {
                    Timber.d("Network unavailable");
                }
                case LOSING -> {
                    Timber.d("Network losing");
                }
                case LOST -> {
                    Timber.d("Network lost");
                    Toast.makeText(this, "Network lost", Toast.LENGTH_LONG).show();
                }
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
