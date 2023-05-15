package com.styleshow.common;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;

public interface ConnectivityObserver {

    @NonNull Observable<Status> observe();

    enum Status {
        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST,
    }
}
