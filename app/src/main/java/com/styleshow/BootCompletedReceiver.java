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

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            var serviceIntent = new Intent(context, CommentNotificationService.class);
            context.startService(serviceIntent);
        }
    }
}
