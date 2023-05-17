package com.styleshow.ui.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import javax.inject.Inject;

import android.app.Activity;
import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Task;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.ChatMessage;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.ChatRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import timber.log.Timber;

// TODO

@HiltViewModel
public class ChatViewModel extends ViewModel {

    private final @NonNull ChatRepository chatRepository;

    private final MutableLiveData<UserProfile> mReceiver = new MutableLiveData<>();

    private final MutableLiveData<List<ChatMessage>> mMessages =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    private final MutableLiveData<String> mNewMessage = new MutableLiveData<>("");

    @Inject
    public ChatViewModel(@NonNull ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public LiveData<UserProfile> getReceiver() {
        return mReceiver;
    }

    @MainThread
    public void setReceiver(UserProfile receiver) {
        mReceiver.setValue(receiver);
    }

    public LiveData<List<ChatMessage>> getMessages() {
        return mMessages;
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    public MutableLiveData<String> getNewMessage() {
        return mNewMessage;
    }

    @MainThread
    public void loadMessages() {
        mLoadingState.setValue(LoadingState.LOADING);

        chatRepository.getMessagesBetween(mReceiver.getValue().getUid())
                .addOnSuccessListener(messages -> {
                    mMessages.setValue(messages);
                    mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                })
                .addOnFailureListener(e -> {
                    mLoadingState.setValue(LoadingState.ERROR);
                });
    }

    public void listenForMessage(@NonNull Activity activity, RecyclerView.Adapter<?> adapter,
                                 @NonNull IntConsumer scrollToBottom) {
        var messages = mMessages.getValue();
        assert messages != null;

        chatRepository.listenForMessagesBetween(activity, mReceiver.getValue().getUid(), new ChatRepository.ChatListener() {
            @Override
            public void onNewMessage(ChatMessage message) {
                int indexAdded = messages.size();

                messages.add(message);
                adapter.notifyItemInserted(indexAdded);
                scrollToBottom.accept(indexAdded);
            }

            @Override
            public void onMessageDeleted(ChatMessage message) {
                int indexRemoved = messages.indexOf(message);

                //messages.remove(message);
                adapter.notifyItemRemoved(indexRemoved);
            }
        });
    }

    @MainThread
    public void trySendMessage() {
        var receiver = mReceiver.getValue();
        if (receiver == null) return;
        String receiverUid = receiver.getUid();

        String content = mNewMessage.getValue();
        Timber.i("(sendMessage) content = %s", content);
        if (content == null || content.isBlank()) return;
        content = content.trim();

        // Send the message
        chatRepository.sendMessage(receiverUid, content);

        // Reset the input field
        mNewMessage.setValue("");
    }

    public boolean canDeleteMessage(@NonNull ChatMessage message) {
        return message.isMyMessage();
    }

    public Task<Void> deleteMessage(@NonNull ChatMessage message) {
        return chatRepository.deleteMessage(message.getId());
    }
}
