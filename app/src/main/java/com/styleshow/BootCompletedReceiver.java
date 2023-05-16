package com.styleshow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.styleshow.service.CommentNotificationService;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("(boot) intent = %s", intent);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            var serviceIntent = new Intent(context, CommentNotificationService.class);
            context.startService(serviceIntent);
        }
    }
}
