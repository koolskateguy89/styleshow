package com.styleshow.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import timber.log.Timber;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> mQuery;

    public SearchViewModel() {
        mText = new MutableLiveData<>("This is search fragment");
        mQuery = new MutableLiveData<>("");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<String> getQuery() {
        return mQuery;
    }

    public void setQuery(String query) {
        mQuery.setValue(query);
    }

    public void search() {
        Timber.i("Searched with query: %s", mQuery.getValue());
    }
}
