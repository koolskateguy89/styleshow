package com.styleshow.data.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.messaging.FirebaseMessaging;
import com.styleshow.common.Constants;
import com.styleshow.data.remote.dto.ChatMessageDto;
import com.styleshow.domain.model.ChatMessage;
import com.styleshow.domain.repository.ChatRepository;
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

    private Query chatsBetweenUsersReference(@NonNull String uid1, @NonNull String uid2) {
        return mChatsRef.where(Filter.or(
                // Messages uid1 -> uid2
                Filter.and(
                        Filter.equalTo(Constants.Chat.FIELD_SENDER_UID, uid1),
                        Filter.equalTo(Constants.Chat.FIELD_RECEIVER_UID, uid2)
                ),
                // Messages uid2 -> uid1
                Filter.and(
                        Filter.equalTo(Constants.Chat.FIELD_SENDER_UID, uid2),
                        Filter.equalTo(Constants.Chat.FIELD_RECEIVER_UID, uid1)
                )
        ));
    }

    public Task<List<ChatMessage>> getMessagesBetween(@NonNull String receiverId) {
        String currentUserId = loginDataSource.getCurrentUser().getUid();

        return chatsBetweenUsersReference(currentUserId, receiverId)
                .orderBy(Constants.Chat.FIELD_SENT_AT, Query.Direction.ASCENDING) // oldest first
                .get()
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to get messages between me and %s", receiverId);
                })
                .continueWith(task -> {
                    if (!task.isSuccessful())
                        //return (Task<List<ChatMessageDto>>) ((Object)task);
                        return List.<ChatMessage>of();

                    var querySnapshot = task.getResult();

                    var chatMessageDtos = querySnapshot.toObjects(ChatMessageDto.class);

                    return chatMessageDtos.stream()
                            .map(dto -> dto.toChatMessage(currentUserId))
                            .collect(Collectors.toList());
                })
                .addOnSuccessListener(messages -> {
                    Timber.d("Messages between me and '%s' : %s", receiverId, messages);
                })
                //.addOnFailureListener(e -> {
                //    Timber.w(e, "Failed to get messages between me and %s", receiverId);
                //})
                ;
    }

    public void sendMessage(@NonNull String receiverUid, @NonNull String content) {
        String currentUserId = loginDataSource.getCurrentUser().getUid();

        var dto = new ChatMessageDto();
        dto.senderUid = currentUserId;
        dto.receiverUid = receiverUid;
        dto.content = content;

        mChatsRef.add(dto)
                .addOnSuccessListener(documentReference -> {
                    Timber.d("Sent message to '%s'", receiverUid);
                })
                .addOnFailureListener(e -> {
                    Timber.w(e, "Failed to send message to '%s'", receiverUid);
                });

        // TODO?
        //messaging.send(new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
        //        .setMessageId(Integer.toString(messageId))
        //        .addData("my_message", "Hello World")
        //        .addData("my_action","SAY_HELLO")
        //        .build());
    }

    public Task<Void> deleteMessage(@NonNull String messageId) {
        return mChatsRef.document(messageId).delete().addOnSuccessListener(ignore -> {
            Timber.d("Deleted message with id '%s'", messageId);
        }).addOnFailureListener(e -> {
            Timber.w(e, "Failed to delete message with id '%s'", messageId);
        });
    }

    public void listenForMessagesBetween(@NonNull Activity activity, @NonNull String receiverId,
                                         @NonNull ChatRepository.ChatListener listener) {
        String currentUserId = loginDataSource.getCurrentUser().getUid();

        chatsBetweenUsersReference(currentUserId, receiverId).addSnapshotListener(activity, (snapshots, e) -> {
            if (e != null) {
                Timber.w(e, "Listen failed.");
                return;
            }

            assert snapshots != null;

            snapshots.getDocumentChanges().forEach(dc -> {
                switch (dc.getType()) {
                    case ADDED -> {
                        Timber.d("New message: %s", dc.getDocument().getData());
                        var dto = dc.getDocument().toObject(ChatMessageDto.class);
                        var message = dto.toChatMessage(currentUserId);
                        listener.onNewMessage(message);
                    }
                    case REMOVED -> {
                        Timber.d("Deleted message: %s", dc.getDocument().getData());
                        var dto = dc.getDocument().toObject(ChatMessageDto.class);
                        var message = dto.toChatMessage(currentUserId);
                        listener.onMessageDeleted(message);
                    }
                }
            });
        });
    }
}
