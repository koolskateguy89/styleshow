package com.styleshow.ui.chat;

import javax.inject.Inject;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.repository.MessageRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.annotations.NonNull;

// TODO

@HiltViewModel
public class ChatViewModel extends ViewModel {

    // TODO: inform this view model of the chat id
    private final @NonNull MessageRepository messageRepository;

    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public ChatViewModel(@NonNull MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    @MainThread
    public void loadMessages() {
        // TODO
        mLoadingState.setValue(LoadingState.LOADING);
    }
}
