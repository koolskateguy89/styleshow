package com.styleshow.ui.post;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PostViewModel extends ViewModel {

    @Inject
    public PostViewModel() {
    }
}
