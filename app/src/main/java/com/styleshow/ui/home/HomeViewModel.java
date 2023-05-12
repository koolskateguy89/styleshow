package com.styleshow.ui.home;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.common.Constants;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.Post;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.PostRepository;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final PostRepository postRepository;
    private final UserProfileRepository userProfileRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MutableLiveData<List<Post>> mPosts = new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mPostLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    private final BehaviorSubject<String> querySubject = BehaviorSubject.createDefault("");
    private final MutableLiveData<List<UserProfile>> mFilteredProfiles =
            new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mSearchLoadingState =
            new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public HomeViewModel(PostRepository postRepository, UserProfileRepository userProfileRepository) {
        this.postRepository = postRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public void setup() {
        // Debounce the query subject
        var filteredProfilesDisposable = querySubject
                .distinctUntilChanged() // idk if there's any point in this
                .debounce(Constants.SEARCH_QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
                    mSearchLoadingState.setValue(LoadingState.LOADING);

                    userProfileRepository.searchProfiles(query)
                            .addOnSuccessListener(profiles -> {
                                mSearchLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                                mFilteredProfiles.setValue(profiles);
                            })
                            .addOnFailureListener(e -> {
                                mSearchLoadingState.setValue(LoadingState.ERROR);
                            })
                    ;
                });
        compositeDisposable.add(filteredProfilesDisposable);
    }

    public void dispose() {
        compositeDisposable.clear();
    }

    public LiveData<List<Post>> getPosts() {
        return mPosts;
    }

    public LiveData<LoadingState> getPostLoadingState() {
        return mPostLoadingState;
    }

    public LiveData<List<UserProfile>> getFilteredProfiles() {
        return mFilteredProfiles;
    }

    public LiveData<LoadingState> getSearchLoadingState() {
        return mSearchLoadingState;
    }

    @MainThread
    public void loadPosts() {
        Timber.i("Loading posts");

        mPostLoadingState.setValue(LoadingState.LOADING);

        postRepository.getAllPosts()
                .addOnSuccessListener(posts -> {
                    mPosts.setValue(posts);
                    mPostLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                })
                .addOnFailureListener(e -> {
                    mPostLoadingState.setValue(LoadingState.ERROR);
                });
    }

    public void setSearchQuery(@NonNull String query) {
        querySubject.onNext(query);
    }
}
