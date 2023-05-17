package com.styleshow.domain.repository;

import androidx.annotation.NonNull;

public interface ChatRepository {

    void sendMessage(@NonNull String receiverUid, @NonNull String content);
}
