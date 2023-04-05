package com.mct.base.ui.abs;

import android.view.View;

import androidx.annotation.NonNull;

public interface IKeyboardManager {

    void clearFocus();

    void showSoftInput(@NonNull View view);

    void hideSoftInput(@NonNull View view);

    void hideSoftInput();

    boolean isSoftInputVisible();

}
