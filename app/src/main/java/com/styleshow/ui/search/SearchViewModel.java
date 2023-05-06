package com.styleshow.ui.search;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import timber.log.Timber;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private final UserProfileRepository userProfileRepository;

    private final MutableLiveData<String> mQuery = new MutableLiveData<>("");
    private final MutableLiveData<List<UserProfile>> mProfiles = new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mLoadingState = new MutableLiveData<>(LoadingState.IDLE);

    private final MediatorLiveData<List<UserProfile>> mFilteredProfiles;

    @Inject
    public SearchViewModel(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;

        mFilteredProfiles = new MediatorLiveData<>(List.of());
        mFilteredProfiles.addSource(mQuery, query -> {
            var profiles = mProfiles.getValue();
            var filteredProfiles = userProfileRepository.filterProfiles(profiles, query);
            mFilteredProfiles.setValue(filteredProfiles);
        });
        mFilteredProfiles.addSource(mProfiles, profiles -> {
            String query = mQuery.getValue();
            var filteredProfiles = userProfileRepository.filterProfiles(profiles, query);
            mFilteredProfiles.setValue(filteredProfiles);
        });

        // logging observing
        mFilteredProfiles.observeForever(profiles -> {
            Timber.d("Filtered profiles for query '%s': %s", mQuery.getValue(), profiles);
        });
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    public LiveData<String> getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        // TODO: debounce
        mQuery.setValue(query);

        //var res = Observable.just("a", "b", "c")
        //        .debounce(1, TimeUnit.SECONDS)
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(charSequence -> {
        //            // do something
        //        });
    }

    //public LiveData<List<UserProfile>> getProfiles() {
    //    return mProfiles;
    //}

    public LiveData<List<UserProfile>> getFilteredProfiles() {
        return mFilteredProfiles;
    }

    public void loadProfiles() {
        Timber.i("Loading profiles...");

        mLoadingState.setValue(LoadingState.LOADING);

        userProfileRepository.getAllProfiles()
                .addOnSuccessListener(profiles -> {
                    Timber.i("Loaded profiles: %s", profiles);
                    mProfiles.setValue(profiles);
                    mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                })
                .addOnFailureListener(e -> {
                    Timber.e(e, "Failed to load profiles");
                    mLoadingState.setValue(LoadingState.ERROR);
                })
        ;
    }
}
