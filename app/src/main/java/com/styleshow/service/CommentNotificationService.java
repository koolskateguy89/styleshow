package com.styleshow.service;

import javax.inject.Inject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.domain.repository.LoginRepository;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO?: reset alarm manager on boot
// TODO: notifiaction on comment of my post

@AndroidEntryPoint
public class CommentNotificationService extends Service {

    int startMode = START_STICKY;       // indicates how to behave if the service is killed
    IBinder binder;      // interface for clients that bind
    boolean allowRebind; // indicates whether onRebind should be used

    @Inject
    FirebaseAuth auth;
    @Inject
    LoginRepository loginRepository;
    @Inject
    FirebaseFirestore firestore;

    public CommentNotificationService() {
        // TODO: listen i guess
        Timber.i("(init) firestore = %s", firestore);

        //var docRef = firestore.collection("cities").document("SF");
    }

    @Override
    public void onCreate() {
        // The service is being created
        super.onCreate();

        // TODO: init using injected dependencies

        var loggedInUser = loginRepository.getCurrentUser();
        Timber.i("(onCreate) loggedInUser = %s", loggedInUser);

        if (loggedInUser != null) {
            //firestore.collectionGroup("comments").addSnapshotListener((value, error) -> {
            //    // TODO
            //    var docs = value.getDocuments();
            //
            //    var datas = docs.stream()
            //            .map(DocumentSnapshot::getData)
            //            .peek(data -> Timber.i("data = %s", data))
            //            .collect(Collectors.toList());
            //});
            //
            //firestore.collection("posts")
            //        .whereEqualTo("uid", loggedInUser.getUid())
            //;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TOOD?: return START_STICKY;
        Timber.i("onStartCommand, intent = %s, flags = %d, startId = %d", intent, flags, startId);
        Timber.i("(onStart) firestore = %s", firestore);

        // The service is starting, due to a call to startService()
        return startMode;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.i("onBind, intent = %s", intent);
        // A client is binding to the service with bindService()
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        Timber.i("onUnbind, intent = %s", intent);
        return allowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Timber.i("onRebind, intent = %s", intent);
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        Timber.i("onDestroy");

        // TODO: restart service
        var intent = new Intent("RestartService");
    }
}
