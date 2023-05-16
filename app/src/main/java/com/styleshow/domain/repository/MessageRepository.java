package com.styleshow.domain.repository;

import androidx.annotation.NonNull;

public interface MessageRepository {

    void sendMessage(@NonNull String receiverUid, @NonNull String content);
}
