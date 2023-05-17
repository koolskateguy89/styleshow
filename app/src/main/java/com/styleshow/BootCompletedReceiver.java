package com.styleshow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.styleshow.service.MessageNotificationService;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

/**
 * On boot completed, start a long-running background service to listen for message events
 * and send notifications.
 */
@AndroidEntryPoint
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.d("(boot) intent = %s", intent);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            var serviceIntent = new Intent(context, MessageNotificationService.class);
            context.startService(serviceIntent);
        }
    }
}
