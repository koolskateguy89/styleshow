package com.styleshow.ui.search;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private static final String TAG = "SearchViewModel";

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
        Log.i(TAG, "Searched with query: " + mQuery.getValue());
    }
}
