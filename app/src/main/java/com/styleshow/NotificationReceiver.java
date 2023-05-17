package com.styleshow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.styleshow.common.Constants;
import timber.log.Timber;

/**
 * Create and display a notification with the provided characteristics.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Timber.d("no permission to post notification");
            return;
        }

        String channelId = intent.getStringExtra(Constants.NAME_NOTIFICATION_CHANNEL_ID);
        int notificationId = intent.getIntExtra(Constants.NAME_NOTIFICATION_ID, 0);
        String title = intent.getStringExtra(Constants.NAME_NOTIFICATION_TITLE);
        String content = intent.getStringExtra(Constants.NAME_NOTIFICATION_CONTENT);

        var builder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_styleshow)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Timber.d("Displaying noti with content: '%s'", content);

        // Build the notification and display it
        NotificationManagerCompat.from(context).notify(notificationId, builder.build());
    }
}
