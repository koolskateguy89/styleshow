package com.styleshow.ui.new_post;

import javax.inject.Inject;

import android.net.Uri;
import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.repository.PostRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class NewPostViewModel extends ViewModel {

    private final PostRepository postRepository;

    private final MutableLiveData<NewPostFormState> formState = new MutableLiveData<>(
            new NewPostFormState("caption", null, "")
    );
    private final MutableLiveData<Uri> mImageUri = new MutableLiveData<>();
    private final MutableLiveData<String> mCaption = new MutableLiveData<>("");
    private final MutableLiveData<String> mShoeUrl = new MutableLiveData<>("");
    private final LiveData<Boolean> mIsDataValid;
    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public NewPostViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;

        // TODO: mediatorLiveData with multiple sources of all fields

        mIsDataValid = Transformations.map(mImageUri, imageUri -> {
            // TODO: more validation
            return imageUri != null;
        });
    }

    public LiveData<Uri> getImageUri() {
        return mImageUri;
    }

    @MainThread
    public void setImageUri(Uri imageUri) {
        mImageUri.setValue(imageUri);
    }

    public LiveData<String> getCaption() {
        return mCaption;
    }

    public LiveData<String> getShoeUrl() {
        return mShoeUrl;
    }

    public LiveData<Boolean> getIsDataValid() {
        return mIsDataValid;
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    @MainThread
    public void captionChanged(String caption) {
        mCaption.setValue(caption);
    }

    @MainThread
    public void publishPost() {
        Timber.d("Publishing post");

        mLoadingState.setValue(LoadingState.LOADING);

        postRepository.publishPost(
                mImageUri.getValue(),
                mCaption.getValue(),
                mShoeUrl.getValue()
        ).addOnSuccessListener(postId -> {
            mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
        }).addOnFailureListener(e -> {
            mLoadingState.setValue(LoadingState.ERROR);
        });
    }
}
