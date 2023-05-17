package com.styleshow.data.remote;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.styleshow.common.Constants;
import com.styleshow.data.remote.dto.ChatMessageDto;
import com.styleshow.domain.model.ChatMessage;
import com.styleshow.domain.repository.ChatRepository;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

public class ChatDataSource {

    private final @NonNull LoginDataSource loginDataSource;

    private final @NonNull CollectionReference mChatsRef;

    public ChatDataSource(
            @NonNull FirebaseFirestore firestore,
            @NonNull LoginDataSource loginDataSource
    ) {
        mChatsRef = firestore.collection(Constants.Chat.COLLECTION_NAME);
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

            if (snapshots == null)
                return;

            List<ChatMessage> added = new ArrayList<>();

            snapshots.getDocumentChanges().forEach(dc -> {
                switch (dc.getType()) {
                    case ADDED -> {
                        var dto = dc.getDocument().toObject(ChatMessageDto.class);
                        var message = dto.toChatMessage(currentUserId);

                        Timber.d("New message: %s", message);
                        added.add(message);
                    }
                    case REMOVED -> {
                        var dto = dc.getDocument().toObject(ChatMessageDto.class);
                        var message = dto.toChatMessage(currentUserId);

                        Timber.d("Deleted message: %s", message);
                        listener.onMessageDeleted(message);
                    }
                }
            });

            if (!added.isEmpty())
                listener.onNewMessages(added);
        });
    }

    public Observable<ChatRepository.MessageEvent> listenForMessageEvents(
            @NonNull String currentUserId) {
        BehaviorSubject<ChatRepository.MessageEvent> subject = BehaviorSubject.create();

        var listenerRegistration = mChatsRef
                .whereEqualTo(Constants.Chat.FIELD_RECEIVER_UID, currentUserId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Timber.w(e, "Listen failed.");
                        return;
                    }

                    if (snapshots == null)
                        return;

                    snapshots.getDocumentChanges().forEach(dc -> {
                        switch (dc.getType()) {
                            case ADDED -> {
                                var dto = dc.getDocument().toObject(ChatMessageDto.class);
                                var message = dto.toChatMessage(currentUserId);

                                Timber.d("New message: %s", message);
                                subject.onNext(new ChatRepository.MessageEvent.MessageSentEvent(message));
                            }
                            case REMOVED -> {
                                var dto = dc.getDocument().toObject(ChatMessageDto.class);
                                var message = dto.toChatMessage(currentUserId);

                                Timber.d("Deleted message: %s", message);
                                subject.onNext(new ChatRepository.MessageEvent.MessageDeletedEvent(message));
                            }
                        }
                    });
                });

        // Remove listener when observable is disposed
        return subject.doOnDispose(listenerRegistration::remove);
    }
}
