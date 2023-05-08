package com.styleshow.ui.new_post;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.domain.repository.PostRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class NewPostViewModel extends ViewModel {

    private final PostRepository postRepository;

    private final MutableLiveData<NewPostFormState> formState = new MutableLiveData<>(
            new NewPostFormState("caption", "")
    );

    @Inject
    public NewPostViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // TODO?: rename to formDataChanged & accept imageUrl
    public void captionChanged(String caption) {
        formState.setValue(formState.getValue().withCaption(caption));
    }

    public LiveData<NewPostFormState> getFormState() {
        return formState;
    }

    // TODO: func submitPost
}
