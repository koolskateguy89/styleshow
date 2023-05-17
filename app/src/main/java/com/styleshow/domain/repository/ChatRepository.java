package com.styleshow.domain.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.domain.model.ChatMessage;

public interface ChatRepository {

    Task<List<ChatMessage>> getMessagesBetween(@NonNull String receiverId);
    // TODO: listen for new messages

    void sendMessage(@NonNull String receiverUid, @NonNull String content);
}
