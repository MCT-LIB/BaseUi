package com.mct.base.ui.transition;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;

import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.annotation.AnimDirection;
import com.mct.base.ui.transition.annotation.AnimationStyle;
import com.mct.base.ui.transition.annotation.Transit;

public abstract class FragmentTransitionFactory {

    @NonNull
    public static FragmentTransition createDefaultTransition() {
        return new CustomFragmentTransition();
    }

    @NonNull
    public static FragmentTransition createDefaultFadeTransition() {
        return new CustomFragmentTransition(
                android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out
        );
    }

    @NonNull
    public static FragmentTransition createTransition(@Transit int transit) {
        return new TransitFragmentTransition(transit);
    }

    @NonNull
    public static FragmentTransition createTransition(@AnimRes @AnimatorRes int enter,
                                                      @AnimRes @AnimatorRes int exit,
                                                      @AnimRes @AnimatorRes int popEnter,
                                                      @AnimRes @AnimatorRes int popExit) {
        return new CustomFragmentTransition(enter, exit, popEnter, popExit);
    }

    /**
     * Note: the anim enter, exit, popEnter, popExit<br/>
     * was create by ({@link AnimationStyle} and {@link AnimDirection}) the value always< 0 <br/>
     * {@link BaseFragment#onCreateAnimation(int, boolean, int)}
     */
    @NonNull
    public static FragmentTransition createAnimation(@AnimationStyle int style,
                                                     @AnimDirection int direction) {
        return createAnimation(style, direction, style, direction);
    }

    /**
     * Note: the anim enter, exit, popEnter, popExit<br/>
     * was create by ({@link AnimationStyle} and {@link AnimDirection}) the value always < 0<br/>
     * {@link BaseFragment#onCreateAnimation(int, boolean, int)}
     */
    @NonNull
    public static FragmentTransition createAnimation(@AnimationStyle int style,
                                                     @AnimDirection int direction,
                                                     @AnimationStyle int popStyle,
                                                     @AnimDirection int popDirection) {
        int enter = style | direction;
        int exit = style | direction;
        int popEnter = popStyle | popDirection;
        int popExit = popStyle | popDirection;
        return new CustomFragmentTransition(enter, exit, popEnter, popExit, Transit.TRANSIT_CUSTOM_ANIMATION);
    }

    @NonNull
    public static FragmentTransition createAnimator() {
        return new CustomFragmentTransition(0, 0, 0, 0, Transit.TRANSIT_CUSTOM_ANIMATOR);
    }

    private FragmentTransitionFactory() {
        //no instance
    }
}
