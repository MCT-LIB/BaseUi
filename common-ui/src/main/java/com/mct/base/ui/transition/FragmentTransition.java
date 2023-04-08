package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

public interface FragmentTransition {

    //
    void applyTransition(@NonNull FragmentTransaction transaction);

    default boolean couldPopImmediateWhenReplaceFragmentInBackStack() {
        return true;
    }

}
