package com.styleshow.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class NetworkConnectivityObserver implements ConnectivityObserver {

    private final @NonNull ConnectivityManager connectivityManager;

    public NetworkConnectivityObserver(Context context) {
        connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public @NonNull Observable<Status> observe() {
        BehaviorSubject<Status> statusSubject = BehaviorSubject.create();

        var networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                statusSubject.onNext(Status.AVAILABLE);
            }

            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                statusSubject.onNext(Status.LOSING);
            }

            @Override
            public void onLost(@NonNull Network network) {
                statusSubject.onNext(Status.LOST);
            }

            @Override
            public void onUnavailable() {
                statusSubject.onNext(Status.UNAVAILABLE);
            }
        };

        connectivityManager.registerDefaultNetworkCallback(networkCallback);
        return statusSubject
                .distinctUntilChanged()
                .doOnDispose(() -> connectivityManager.unregisterNetworkCallback(networkCallback));
    }
}
