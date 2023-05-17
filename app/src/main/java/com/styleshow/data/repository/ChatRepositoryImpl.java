package com.styleshow.data.repository;

import java.util.List;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.data.remote.ChatDataSource;
import com.styleshow.domain.model.ChatMessage;
import com.styleshow.domain.repository.ChatRepository;

public class ChatRepositoryImpl implements ChatRepository {

    private final @NonNull ChatDataSource dataSource;

    public ChatRepositoryImpl(@NonNull ChatDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Task<List<ChatMessage>> getMessagesBetween(@NonNull String receiverId) {
        return dataSource.getMessagesBetween(receiverId);
    }

    @Override
    public void sendMessage(@NonNull String receiverUid, @NonNull String content) {
        dataSource.sendMessage(receiverUid, content);
    }
}
