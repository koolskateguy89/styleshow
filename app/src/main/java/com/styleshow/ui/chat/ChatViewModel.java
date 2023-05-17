package com.styleshow.ui.chat;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.ChatMessage;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.ChatRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

// TODO

@HiltViewModel
public class ChatViewModel extends ViewModel {

    // TODO: inform this view model of the chat id
    private final @NonNull ChatRepository chatRepository;

    private final MutableLiveData<UserProfile> mReceiver = new MutableLiveData<>();

    private final MutableLiveData<List<ChatMessage>> mMessages =
            new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

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
}
