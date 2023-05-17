package com.styleshow;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationManagerCompat;
import com.styleshow.common.Constants;
import timber.log.Timber;

/**
 * Cancel the notification with the provided id.
 */
public class CancelNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(Constants.NAME_NOTIFICATION_ID, -1);

        if (notificationId == -1)
            return;

        Timber.d("Canceling notification with id %d", notificationId);
        NotificationManagerCompat.from(context).cancel(notificationId);
    }
}
