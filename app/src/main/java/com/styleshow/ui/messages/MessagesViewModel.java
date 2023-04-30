package com.styleshow.ui.messages;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MessagesViewModel extends ViewModel {

    LiveData<List<String>> messages;

    @Inject
    public MessagesViewModel() {

    }

}
