package com.styleshow.data;

public enum LoadingState {
    /**
     * Initial state
     */
    IDLE,
    /**
     * Loading data
     */
    LOADING,
    /**
     * Error loading data
     */
    ERROR,
    /**
     * Data loaded successfully
     */
    SUCCESS_IDLE,
    ;
}
