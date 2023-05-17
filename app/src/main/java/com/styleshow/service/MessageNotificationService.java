package com.styleshow.service;

import javax.inject.Inject;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import androidx.annotation.Nullable;
import com.styleshow.CancelNotificationReceiver;
import com.styleshow.NotificationReceiver;
import com.styleshow.R;
import com.styleshow.common.Constants;
import com.styleshow.domain.model.ChatMessage;
import com.styleshow.domain.repository.ChatRepository;
import com.styleshow.domain.repository.LoginRepository;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.Disposable;
import timber.log.Timber;

/**
 * A service that listens for new messages and displays a notification when a new message
 * is received. Uses a {@link PowerManager.WakeLock} to ensure the device stays awake while the service
 * is running.
 */
@AndroidEntryPoint
public class MessageNotificationService extends Service {

    private static final int WAKELOCK_TIMEOUT_MILLIS = 10 * 60 * 1000; // 10 minutes

    private static final int ALARM_TRIGGER = 1000; // 1 second

    @Inject
    LoginRepository loginRepository;

    @Inject
    UserProfileRepository userProfileRepository;

    @Inject
    ChatRepository chatRepository;

    private @Nullable PowerManager.WakeLock wakeLock;

    private @Nullable Disposable messageEventsDisposable;

    // No args constructor
    public MessageNotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        var powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "styleshow:MessageNotificationService");
        wakeLock.acquire(WAKELOCK_TIMEOUT_MILLIS);

        // Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Timber.d("Creating notification channel");

            var channelId = getString(R.string.message_notification_channel_id);
            var name = getString(R.string.message_notification_channel_id);
            var description = getString(R.string.message_notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Service logic here
        var loggedInUser = loginRepository.getCurrentUser();

        if (loggedInUser != null) {
            String userId = loggedInUser.getUid();

            messageEventsDisposable = chatRepository.listenForMessageEvents(userId).subscribe(messageEvent -> {
                if (messageEvent instanceof ChatRepository.MessageEvent.MessageSentEvent sentEvent) {
                    Timber.d("Message received: %s", sentEvent.message);
                    scheduleNotification(sentEvent.message);
                } else if (messageEvent instanceof ChatRepository.MessageEvent.MessageDeletedEvent deletedEvent) {
                    Timber.d("Message received deleted: %s", deletedEvent.message);
                    cancelNotification(deletedEvent.message);
                } else {
                    throw new IllegalStateException("Unexpected message event type: " + messageEvent);
                }
            });
        }

        return START_STICKY;  // Restart the service if it gets terminated
    }

    private void scheduleNotification(ChatMessage message) {
        userProfileRepository.getProfileForUid(message.senderUid).addOnSuccessListener(profile -> {
            var username = profile.getUsername();
            var channelId = getString(R.string.message_notification_channel_id);

            var notificationIntent = new Intent(this, NotificationReceiver.class)
                    .putExtra(Constants.NAME_NOTIFICATION_CHANNEL_ID, channelId)
                    .putExtra(Constants.NAME_NOTIFICATION_ID, message.getId().hashCode())
                    .putExtra(Constants.NAME_NOTIFICATION_TITLE, username)
                    .putExtra(Constants.NAME_NOTIFICATION_CONTENT, message.getContent());

            var pendingIntent = PendingIntent.getBroadcast(
                    this,
                    message.getId().hashCode(),
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            var alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + ALARM_TRIGGER,
                    pendingIntent);
        }).addOnFailureListener(e -> {
            Timber.w(e, "Failed to get profile for uid %s", message.senderUid);
        });
    }

    private void cancelNotification(ChatMessage message) {
        var intent = new Intent(this, CancelNotificationReceiver.class)
                .putExtra(Constants.NAME_NOTIFICATION_ID, message.getId().hashCode());
        this.sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // no binding
    }

    @Override
    public void onDestroy() {
        Timber.w("onDestroy");
        super.onDestroy();

        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }

        if (messageEventsDisposable != null && !messageEventsDisposable.isDisposed()) {
            messageEventsDisposable.dispose();
        }
    }
}
