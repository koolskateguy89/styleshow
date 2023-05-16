package com.styleshow.data.remote;

import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessaging;
import timber.log.Timber;

public class MessageDataSource {

    private final @NonNull FirebaseMessaging messaging;

    public MessageDataSource(@NonNull FirebaseMessaging messaging) {
        this.messaging = messaging;
    }

    public void sendMessage(@NonNull String receiverUid, @NonNull String content) {
        // TODO
        //messaging.send(new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
        //        .setMessageId(Integer.toString(messageId))
        //        .addData("my_message", "Hello World")
        //        .addData("my_action","SAY_HELLO")
        //        .build());
        Timber.i("sendMessage: %s, %s", receiverUid, content);
    }
}
