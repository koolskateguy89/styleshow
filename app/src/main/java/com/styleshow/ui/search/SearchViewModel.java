package com.styleshow.ui.search;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.styleshow.common.Constants;
import com.styleshow.data.LoadingState;
import com.styleshow.domain.model.UserProfile;
import com.styleshow.domain.repository.UserProfileRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import timber.log.Timber;

@HiltViewModel
public class SearchViewModel extends ViewModel {

    private final UserProfileRepository userProfileRepository;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final BehaviorSubject<String> querySubject = BehaviorSubject.createDefault("");
    private final BehaviorSubject<List<UserProfile>> profilesSubject = BehaviorSubject.createDefault(List.of());
    private final MutableLiveData<List<UserProfile>> mFilteredProfiles = new MutableLiveData<>(List.of());
    private final MutableLiveData<LoadingState> mLoadingState = new MutableLiveData<>(LoadingState.IDLE);

    @Inject
    public SearchViewModel(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;

        // Debounce the query subject
        var debouncedQueryObservable = querySubject
                .debounce(Constants.SEARCH_QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS)
                .debounce(query -> {
                    var result = Observable.just(query);

                    // Only add a delay (debounce) if the query is not empty
                    // so that we don't have to wait for the debounce when first opening the fragment
                    if (!query.isEmpty()) {
                        result = result
                                .delay(Constants.SEARCH_QUERY_DEBOUNCE_MILLIS, TimeUnit.MILLISECONDS);
                    }

                    return result;
                })
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.computation());

        // Set the schedulers for the profiles subject
        var profilesObservable = profilesSubject
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.computation());

        // Combine the debounced query and profiles subjects to get the filtered profiles
        var filteredProfilesObservable = Observable.combineLatest(debouncedQueryObservable, profilesObservable, (query, profiles) -> {
            var filteredProfiles = userProfileRepository.filterProfiles(profiles, query);

            Timber.d("Filtered profiles for query '%s': %s", query, filteredProfiles);

            return filteredProfiles;
        });

        var filteredProfilesDisposable = filteredProfilesObservable
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mFilteredProfiles::setValue);
        compositeDisposable.add(filteredProfilesDisposable);

        var queryLoggingDisposable = querySubject
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(query -> {
                    Timber.d("query = '%s'", query);
                });
        compositeDisposable.add(queryLoggingDisposable);
    }

    /**
     * Dispose of all RxJava subscriptions.
     */
    public void dispose() {
        compositeDisposable.clear();
    }

    /**
     * Set the query to filter profiles by. Will be debounced.
     * <p>
     * If it is empty, all profiles will be returned.
     *
     * @param query The search query.
     */
    public void setQuery(String query) {
        querySubject.onNext(query);
    }

    public LiveData<LoadingState> getLoadingState() {
        return mLoadingState;
    }

    public LiveData<List<UserProfile>> getFilteredProfiles() {
        return mFilteredProfiles;
    }

    /**
     * Initiate retrieving all profiles from the repository. Upon success, filtered profiles
     * live data will be updated.
     */
    public void loadProfiles() {
        Timber.i("Loading profiles...");

        mLoadingState.setValue(LoadingState.LOADING);

        userProfileRepository.getAllProfiles()
                .addOnSuccessListener(profiles -> {
                    Timber.i("Loaded profiles: %s", profiles);
                    profilesSubject.onNext(profiles);
                    mLoadingState.setValue(LoadingState.SUCCESS_IDLE);
                })
                .addOnFailureListener(e -> {
                    Timber.e(e, "Failed to load profiles");
                    mLoadingState.setValue(LoadingState.ERROR);
                })
        ;
    }
}
