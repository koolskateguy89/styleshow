package com.styleshow.ui.messages;

import javax.inject.Inject;

import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import dagger.hilt.android.lifecycle.HiltViewModel;

// TODO: Implement the ViewModel

@HiltViewModel
public class MessagesViewModel extends ViewModel {

    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public MessagesViewModel() {
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    public void loadMessages() {
        mLoadingState.setValue(LoadingState.LOADING);

        var handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
        }, 1500);
    }
}
