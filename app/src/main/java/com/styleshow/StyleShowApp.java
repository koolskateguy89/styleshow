package com.styleshow;

import android.app.Application;
import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

// TODO: error screen if internet is not available
// not sure where to put it tho

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
