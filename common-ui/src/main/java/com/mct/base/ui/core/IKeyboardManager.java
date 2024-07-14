package com.mct.base.ui.core;

import android.view.View;

import androidx.annotation.NonNull;

public interface IKeyboardManager {

    void clearFocus();

    void showSoftInput(@NonNull View view);

    void hideSoftInput();

    void hideSoftInput(long delay);

    void hideSoftInput(@NonNull View view);

    boolean isSoftInputVisible();

}
