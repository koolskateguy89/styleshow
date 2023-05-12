package com.styleshow.ui.new_post;

import javax.inject.Inject;

import android.net.Uri;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.domain.repository.PostRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class NewPostViewModel extends ViewModel {

    private final PostRepository postRepository;

    private final MutableLiveData<NewPostFormState> formState = new MutableLiveData<>(
            new NewPostFormState("caption", null, "")
    );

    @Inject
    public NewPostViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void captionChanged(String caption) {
        formState.setValue(formState.getValue().withCaption(caption));
    }

    public void imageChanged(Uri imageUri) {
        formState.setValue(formState.getValue().withImageUri(imageUri));
    }

    public LiveData<NewPostFormState> getFormState() {
        return formState;
    }

    public void publishPost() {
        Timber.i("publishPost");
        // TODO
    }
}
