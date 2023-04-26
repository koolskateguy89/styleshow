package com.styleshow.data;

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
public class Result<T> {
    private Result() {}

    @Override
    public String toString() {
        if (this instanceof Result.Success<T> success) {
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof Result.Error error) {
            return "Error[exception=" + error.getError().toString() + "]";
        }

        throw new IllegalStateException("Result is not success nor error");
    }

    public boolean isSuccess() {
        return this instanceof Result.Success<T>;
    }

    public boolean isError() {
        return this instanceof Result.Error;
    }

    // Success sub-class
    public final static class Success<T> extends Result<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends Result {
        private final Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
