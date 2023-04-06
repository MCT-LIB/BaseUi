package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

public interface FragmentTransition {
    void applyTransition(@NonNull FragmentTransaction transaction);

    static void apply(FragmentTransaction transaction, FragmentTransition transition) {
        if (transition != null) {
            transition.applyTransition(transaction);
        }
    }
}
