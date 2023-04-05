package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

public class NoneFragmentTransition implements FragmentTransition {
    @Override
    public void applyTransition(@NonNull FragmentTransaction fragmentTransaction) {
        fragmentTransaction.setCustomAnimations(0, 0, 0, 0);
    }
}
