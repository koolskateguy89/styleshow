package com.styleshow.ui.messages;

import javax.inject.Inject;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.repository.ChatRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

// TODO: get list of convos from firebasemessaging

@HiltViewModel
public class MessagesViewModel extends ViewModel {

    private final @NonNull ChatRepository chatRepository;

    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public MessagesViewModel(@NonNull ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    @MainThread
    public void loadMessages() {
        mLoadingState.setValue(LoadingState.LOADING);

        var handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
        }, 1500);
    }

    // this is a test, it almost defo won't be in this viewmodel,
    // it will be in the one specific to the receiver of the message
    public void sendMessage(@NonNull String receiverUid, @NonNull String content) {
        chatRepository.sendMessage(receiverUid, content);
    }
}
