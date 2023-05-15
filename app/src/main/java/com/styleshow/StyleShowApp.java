package com.styleshow;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

// TODO: error screen if internet is not available
// not sure where to put it tho
// can't use broadcast receiver for this because it's deprecated
// supposedly have to use WorkManager

@HiltAndroidApp
public class StyleShowApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupTimber();
    }

    private void setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new DebugTree());
        }
    }
}
