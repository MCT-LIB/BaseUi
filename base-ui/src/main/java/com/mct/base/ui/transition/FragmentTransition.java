package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

public interface FragmentTransition {

    void applyTransition(@NonNull FragmentTransaction transaction);

    /**
     * a function to manage the transition when (pop and replace) fragment
     */
    default boolean couldPopImmediate() {
        return true;
    }

}
