package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

public class FadeFragmentTransition implements FragmentTransition {
    @Override
    public void applyTransition(@NonNull FragmentTransaction fragmentTransaction) {
        fragmentTransaction.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out
        );
    }
}
