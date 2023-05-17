package com.styleshow.data.repository;

import androidx.annotation.NonNull;
import com.styleshow.data.remote.ChatDataSource;
import com.styleshow.domain.repository.ChatRepository;

public class ChatRepositoryImpl implements ChatRepository {

    private final @NonNull ChatDataSource dataSource;

    public ChatRepositoryImpl(@NonNull ChatDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void sendMessage(@NonNull String receiverUid, @NonNull String content) {
        dataSource.sendMessage(receiverUid, content);
    }
}
