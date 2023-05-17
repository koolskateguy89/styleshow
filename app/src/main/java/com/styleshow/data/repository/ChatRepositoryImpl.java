package com.styleshow.data.repository;

import android.app.Activity;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.styleshow.data.remote.ChatDataSource;
import com.styleshow.domain.repository.ChatRepository;
import io.reactivex.rxjava3.core.Observable;

public class ChatRepositoryImpl implements ChatRepository {

    private final @NonNull ChatDataSource dataSource;

    public ChatRepositoryImpl(@NonNull ChatDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void sendMessage(@NonNull String receiverUid, @NonNull String content) {
        dataSource.sendMessage(receiverUid, content);
    }

    @Override
    public Task<Void> deleteMessage(@NonNull String messageId) {
        return dataSource.deleteMessage(messageId);
    }

    @Override
    public void listenForMessagesBetween(@NonNull Activity activity, @NonNull String receiverId,
                                         @NonNull ChatListener listener) {
        dataSource.listenForMessagesBetween(activity, receiverId, listener);
    }

    @Override
    public Observable<MessageEvent> listenForMessageEvents(@NonNull String currentUserId) {
        return dataSource.listenForMessageEvents(currentUserId);
    }
}
