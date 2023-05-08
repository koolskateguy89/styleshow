package com.styleshow.common;

import java.util.function.Consumer;

import android.text.Editable;
import android.text.TextWatcher;

public class AfterTextChangedTextWatcher implements TextWatcher {

    private final Consumer<Editable> afterTextChangedRunnable;

    public AfterTextChangedTextWatcher(Consumer<Editable> afterTextChangedRunnable) {
        this.afterTextChangedRunnable = afterTextChangedRunnable;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // ignore
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // ignore
    }

    @Override
    public void afterTextChanged(Editable s) {
        afterTextChangedRunnable.accept(s);
    }
}
