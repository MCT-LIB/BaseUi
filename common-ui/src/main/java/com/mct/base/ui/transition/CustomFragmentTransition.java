package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

class CustomFragmentTransition implements FragmentTransition {

    private int enter;
    private int exit;
    private int popEnter;
    private int popExit;

    CustomFragmentTransition() {
    }

    CustomFragmentTransition(int enter, int exit, int popEnter, int popExit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
    }

    @Override
    public void applyTransition(@NonNull FragmentTransaction transaction) {
        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
    }
}
