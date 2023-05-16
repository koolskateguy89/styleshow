package com.styleshow.service;

import javax.inject.Inject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.styleshow.domain.repository.LoginRepository;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

// TODO?: reset alarm manager on boot
// TODO: notifiaction on comment of my post

// FIXME: even with alarm manager, service is killed after a few minutes

@AndroidEntryPoint
public class CommentNotificationService extends Service {

    private static final int NOTIFICATION_ID = 1;

    //private static final long INTERVAL_MILLIS = 1000 * 60 * 60; // 1 hour
    private static final long INTERVAL_MILLIS = 1000 * 15; // 15 seconds

    @Inject
    FirebaseAuth auth;
    @Inject
    LoginRepository loginRepository;
    @Inject
    FirebaseFirestore firestore;

    // No args constructor
    public CommentNotificationService() {
        Timber.i("(constructor)");
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

    private void scheduleServiceRestart() {
        Timber.i("scheduleServiceRestart");

        var alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        var serviceIntent = new Intent(this, CommentNotificationService.class);
        var pendingIntent = PendingIntent.getService(this, 0, serviceIntent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INTERVAL_MILLIS,
                INTERVAL_MILLIS,
                pendingIntent
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Place your background service logic here
        Timber.i("(onStart) intent = %s", intent);

        scheduleServiceRestart();

        return START_STICKY;  // Restart the service if it gets terminated
    }

    // TODO: use if making this a foreground service
    private Notification createNotification() {
        return new Notification.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
                .setContentTitle("Comment Notification Service")
                .setContentText("Running")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Timber.i("(onBind) intent = %s", intent);
        return null; // no binding
    }

    @Override
    public void onDestroy() {
        // The service is no longer used and is being destroyed
        Timber.i("onDestroy");
        // TODO?: restart service - no, it's unreliable
    }
}
