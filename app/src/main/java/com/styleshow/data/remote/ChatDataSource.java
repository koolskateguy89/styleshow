package com.styleshow.data.remote;

import androidx.annotation.NonNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.styleshow.common.Constants;
import timber.log.Timber;

public class ChatDataSource {

    private final @NonNull FirebaseMessaging messaging;

    private final @NonNull CollectionReference mChatsRef;

    public ChatDataSource(
            @NonNull FirebaseFirestore firestore,
            @NonNull FirebaseMessaging messaging
    ) {
        mChatsRef = firestore.collection(Constants.COLLECTION_CHATS);
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
