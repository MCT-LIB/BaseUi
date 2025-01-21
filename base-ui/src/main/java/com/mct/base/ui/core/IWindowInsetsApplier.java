package com.mct.base.ui.core;

import android.view.View;

import androidx.core.view.WindowInsetsCompat;

import com.mct.base.ui.utils.ScreenUtils;

public interface IWindowInsetsApplier {

    default void applyInsetsSystemBar(View view) {
        ScreenUtils.applyInsets(view, WindowInsetsCompat.Type.systemBars());
    }

    default void applyInsetsSystemBar2(View view) {
        ScreenUtils.applyInsets2(view, WindowInsetsCompat.Type.systemBars());
    }

    default void applyInsetsStatusBar(View view) {
        ScreenUtils.applyInsets(view, WindowInsetsCompat.Type.statusBars());
    }

    default void applyInsetsStatusBar2(View view) {
        ScreenUtils.applyInsets2(view, WindowInsetsCompat.Type.statusBars());
    }

    default void applyInsetsNavigationBar(View view) {
        ScreenUtils.applyInsets(view, WindowInsetsCompat.Type.navigationBars());
    }

    default void applyInsetsNavigationBar2(View view) {
        ScreenUtils.applyInsets2(view, WindowInsetsCompat.Type.navigationBars());
    }
}
