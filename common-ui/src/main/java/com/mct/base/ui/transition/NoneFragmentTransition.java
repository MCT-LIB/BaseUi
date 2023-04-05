package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.R;

public class NoneFragmentTransition implements FragmentTransition {
    @Override
    public void applyTransition(@NonNull FragmentTransaction fragmentTransaction) {
        fragmentTransaction.setCustomAnimations(
                R.anim.no_anim, R.anim.no_anim,
                R.anim.no_anim, R.anim.no_anim
        );
    }
}
