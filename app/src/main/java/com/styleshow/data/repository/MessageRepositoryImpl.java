package com.styleshow.data.repository;

import androidx.annotation.NonNull;
import com.styleshow.data.remote.MessageDataSource;
import com.styleshow.domain.repository.MessageRepository;

public class MessageRepositoryImpl implements MessageRepository {

    private final @NonNull MessageDataSource dataSource;

    public MessageRepositoryImpl(@NonNull MessageDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void sendMessage(@NonNull String receiverUid, @NonNull String content) {
        dataSource.sendMessage(receiverUid, content);
    }
}
