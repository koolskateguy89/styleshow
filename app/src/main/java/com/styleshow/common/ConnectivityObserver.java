package com.styleshow.common;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.core.Observable;

/**
 * Observes connectivity status of a resource.
 *
 * @see <a href="https://youtu.be/TzV0oCRDNfM">[https://youtu.be/TzV0oCRDNfM]</a>
 */
public interface ConnectivityObserver {

    @NonNull Observable<Status> observe();

    enum Status {
        AVAILABLE,
        UNAVAILABLE,
        LOSING,
        LOST,
    }
}
