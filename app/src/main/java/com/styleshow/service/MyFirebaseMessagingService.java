package com.styleshow.service;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    FirebaseMessaging messaging;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        // TODO?

        Timber.d("messaging: %s", messaging);
        Timber.d("new token: %s", token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token);
        // TODO ^ idk what the impl. of sendRegistrationToServer would look like, or if i even need it
    }

    @Override
    public void onMessageSent(@NonNull String msgId) {
        super.onMessageSent(msgId);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.i("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Timber.i("onDestroy");
    }
}
