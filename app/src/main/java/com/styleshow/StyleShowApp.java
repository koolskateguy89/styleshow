package com.styleshow;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

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
