package com.styleshow.service;

import javax.inject.Inject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.domain.repository.LoginRepository;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO: notifiaction on comment of my post
@AndroidEntryPoint
public class CommentNotificationService extends Service {

    int startMode;       // indicates how to behave if the service is killed
    IBinder binder;      // interface for clients that bind
    boolean allowRebind; // indicates whether onRebind should be used

    @Inject
    public CommentNotificationService(
            @NonNull LoginRepository loginRepository,
            @NonNull FirebaseFirestore firestore
    ) {
        // TODO: listen i guess
        Timber.i("CommentNotificationService created");

        var docRef = firestore.collection("cities").document("SF");
    }

    @Override
    public void onCreate() {
        // The service is being created
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        return startMode;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.i("onBind, intent = %s", intent);

        // A client is binding to the service with bindService()
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return allowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
    }
}
