package com.mct.base.ui.transition;

import androidx.fragment.app.FragmentTransaction;

public interface FragmentTransition {
    void applyTransition(FragmentTransaction fragmentTransaction);

    static void apply(FragmentTransaction transaction, FragmentTransition transition) {
        if (transition != null) {
            transition.applyTransition(transaction);
        }
    }
}
