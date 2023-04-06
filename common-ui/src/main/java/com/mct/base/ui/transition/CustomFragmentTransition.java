package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.transition.annotation.Transit;

class CustomFragmentTransition implements FragmentTransition {

    private int enter;
    private int exit;
    private int popEnter;
    private int popExit;
    private int transit;

    CustomFragmentTransition() {
    }

    CustomFragmentTransition(int enter, int exit, int popEnter, int popExit) {
        this(enter, exit, popEnter, popExit, FragmentTransaction.TRANSIT_NONE);
    }

    public CustomFragmentTransition(int enter, int exit, int popEnter, int popExit, @Transit int transit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
        this.transit = transit;
    }

    @Override
    public void applyTransition(@NonNull FragmentTransaction transaction) {
        transaction.setTransition(transit);
        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
    }
}
