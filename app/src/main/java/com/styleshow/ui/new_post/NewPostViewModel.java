package com.styleshow.ui.new_post;

import javax.inject.Inject;

import android.net.Uri;
import android.webkit.URLUtil;
import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.repository.PostRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class NewPostViewModel extends ViewModel {

    private final PostRepository postRepository;

    private final MutableLiveData<Uri> mImageUri = new MutableLiveData<>();
    private final MutableLiveData<String> mCaption = new MutableLiveData<>("");
    private final MutableLiveData<String> mShoeUrl = new MutableLiveData<>("");
    private final MutableLiveData<LoadingState> mLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public NewPostViewModel(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public LiveData<String> getCaption() {
        return mCaption;
    }

    @MainThread
    public void setCaption(String caption) {
        mCaption.setValue(caption);
    }

    public LiveData<Uri> getImageUri() {
        return mImageUri;
    }

    @MainThread
    public void setImageUri(Uri imageUri) {
        mImageUri.setValue(imageUri);
    }

    public LiveData<String> getShoeUrl() {
        return mShoeUrl;
    }

    @MainThread
    public void setShoeUrl(String shoeUrl) {
        mShoeUrl.setValue(shoeUrl);
    }

    public boolean isDataValid() {
        Uri imageUri = mImageUri.getValue();
        String caption = mCaption.getValue();
        String shoeUrl = mShoeUrl.getValue();
        boolean validShoeUrl = URLUtil.isValidUrl(shoeUrl);

        if (imageUri == null || caption == null || shoeUrl == null)
            return false;

        if (caption.isBlank() || validShoeUrl) {
            return false;
        }

        return true;
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    @MainThread
    public void publishPost() {
        Timber.d("Publishing post");

        mLoadingState.setValue(LoadingState.LOADING);

        postRepository.publishPost(
                mImageUri.getValue(),
                mCaption.getValue().trim(),
                mShoeUrl.getValue().trim()
        ).addOnSuccessListener(postId -> {
            mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
        }).addOnFailureListener(e -> {
            mLoadingState.setValue(LoadingState.ERROR);
        });
    }
}
