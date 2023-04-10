package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mct.base.ui.transition.annotation.AnimatorStyle;
import com.mct.base.ui.transition.annotation.Transit;
import com.mct.base.ui.transition.options.AnimOptions;

public abstract class FragmentTransitionFactory {

    @NonNull
    public static FragmentTransition createDefaultTransition() {
        return NormalFragmentTransition.builder().build();
    }

    @NonNull
    public static FragmentTransition createCircularRevealTransition() {
        AnimOptions circularReveal = AnimOptions.animator(AnimatorStyle.CIRCULAR_REVEAL).build();
        AnimOptions none = AnimOptions.animator(AnimatorStyle.NONE).build();

        return createTransition(circularReveal, none, none, circularReveal);
    }

    @NonNull
    public static FragmentTransition createTransition(@Nullable AnimOptions enter,
                                                      @Nullable AnimOptions exit) {
        return createTransition(enter, exit, null, null);
    }

    @NonNull
    public static FragmentTransition createTransition(@Nullable AnimOptions enter,
                                                      @Nullable AnimOptions exit,
                                                      @Nullable AnimOptions popEnter,
                                                      @Nullable AnimOptions popExit) {
        return NormalFragmentTransition.builder()
                .enter(enter).exit(exit)
                .popEnter(popEnter).popExit(popExit)
                .build();
    }

    @NonNull
    public static FragmentTransition createTransition(@Transit int transit) {
        return new TransitFragmentTransition(transit);
    }

    private FragmentTransitionFactory() {
        //no instance
    }

}
