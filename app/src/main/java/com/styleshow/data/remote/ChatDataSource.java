package com.styleshow.data.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.styleshow.common.Constants;
import com.styleshow.data.remote.dto.ChatMessageDto;
import com.styleshow.domain.model.ChatMessage;
import timber.log.Timber;

public class ChatDataSource {

    private final @NonNull FirebaseMessaging messaging; // may not use

    private final @NonNull LoginDataSource loginDataSource;

    private final @NonNull CollectionReference mChatsRef;

    public ChatDataSource(
            @NonNull FirebaseFirestore firestore,
            @NonNull FirebaseMessaging messaging,
            @NonNull LoginDataSource loginDataSource
    ) {
        mChatsRef = firestore.collection(Constants.Chat.COLLECTION_NAME);
        this.messaging = messaging; // may not use
        this.loginDataSource = loginDataSource;
    }

    public Task<List<ChatMessage>> getMessagesBetween(@NonNull String receiverId) {
        String currentUserId = loginDataSource.getCurrentUser().getUid();

        return mChatsRef.where(Filter.or(
                        // Messages sent by me to receiver
                        Filter.and(
                                Filter.equalTo(Constants.Chat.FIELD_SENDER_UID, currentUserId),
                                Filter.equalTo(Constants.Chat.FIELD_RECEIVER_UID, receiverId)
                        ),
                        // Messages sent by receiver to me
                        Filter.and(
                                Filter.equalTo(Constants.Chat.FIELD_SENDER_UID, receiverId),
                                Filter.equalTo(Constants.Chat.FIELD_RECEIVER_UID, currentUserId)
                        )
                ))
                .get()
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        return null;

                    var querySnapshot = task.getResult();

                    var chatMessageDtos = querySnapshot.toObjects(ChatMessageDto.class);

                    return chatMessageDtos.stream()
                            .peek(dto -> Timber.d("dto = %s", dto))
                            .map(dto -> dto.toChatMessage(currentUserId))
                            .collect(Collectors.toList());
                })
                .addOnSuccessListener(messages -> {
                    Timber.d("Messages between me and '%s' : %s", receiverId, messages);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to get messages between me and %s", receiverId);
                })
                ;
    }

    public void sendMessage(@NonNull String receiverUid, @NonNull String content) {
        String currentUserId = loginDataSource.getCurrentUser().getUid();

        Map<String, Object> data = new HashMap<>();
        data.put(Constants.Chat.FIELD_SENDER_UID, currentUserId);
        data.put(Constants.Chat.FIELD_RECEIVER_UID, receiverUid);
        data.put(Constants.Chat.FIELD_CONTENT, content);
        data.put(Constants.Chat.FIELD_SENT_AT, FieldValue.serverTimestamp());

        mChatsRef.add(data)
                .addOnSuccessListener(documentReference -> {
                    //Timber.d("sendMessage: %s", messageDto);
                    Timber.d("Sent message to '%s'", receiverUid);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to send message to '%s'", receiverUid);
                });

        // TODO
        //messaging.send(new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
        //        .setMessageId(Integer.toString(messageId))
        //        .addData("my_message", "Hello World")
        //        .addData("my_action","SAY_HELLO")
        //        .build());
    }
}
