package com.styleshow.no_network;

import android.content.Intent;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.styleshow.R;
import com.styleshow.common.ConnectivityObserver;
import com.styleshow.common.NetworkConnectivityObserver;
import com.styleshow.databinding.ActivityNoNetworkBinding;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import timber.log.Timber;

public class NoNetworkActivity extends AppCompatActivity {

    private ActivityNoNetworkBinding binding;
    private ConnectivityObserver connectivityObserver;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNoNetworkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        compositeDisposable = new CompositeDisposable();

        connectivityObserver = new NetworkConnectivityObserver(this);
        compositeDisposable.add(connectivityObserver.observe().subscribe(status -> {
            if (status == ConnectivityObserver.Status.AVAILABLE) {
                Timber.d("Network available");
                finish();
            } else {
                Timber.d("Network unavailable");
            }
        }));
    }

    private void openNoNetworkActivity() {
        var intent = new Intent(this, NoNetworkActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        // No op so they can't exit until they have a working network connection
        //super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }
}
