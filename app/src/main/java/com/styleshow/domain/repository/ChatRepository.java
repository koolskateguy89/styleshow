package com.styleshow.domain.repository;

import java.util.List;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.ChatMessage;

public interface ChatRepository {

    Task<List<ChatMessage>> getMessagesBetween(@NonNull String receiverId);

    void sendMessage(@NonNull String receiverUid, @NonNull String content);

    Task<Void> deleteMessage(@NonNull String messageId);

    void listenForMessagesBetween(@NonNull Activity activity, @NonNull String receiverId,
                                  @NonNull ChatListener listener);

    interface ChatListener {

        void onNewMessage(ChatMessage message);

        void onMessageDeleted(ChatMessage message);
    }
}
