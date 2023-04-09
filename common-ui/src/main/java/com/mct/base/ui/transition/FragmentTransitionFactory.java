package com.mct.base.ui.transition;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.annotation.AnimBehavior;
import com.mct.base.ui.transition.annotation.AnimDirection;
import com.mct.base.ui.transition.annotation.AnimType;
import com.mct.base.ui.transition.annotation.AnimationStyle;
import com.mct.base.ui.transition.annotation.AnimatorStyle;
import com.mct.base.ui.transition.annotation.Transit;
import com.mct.base.ui.transition.option.AnimOptions;
import com.mct.base.ui.transition.option.AnimOptionsOrAnim;

@SuppressWarnings("unused")
public abstract class FragmentTransitionFactory {

    @NonNull
    public static FragmentTransition createDefaultTransition() {
        return new CustomFragmentTransition();
    }

    @NonNull
    public static FragmentTransition createDefaultFadeTransition() {
        return createTransition(
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

    ///////////////////////////////////////////////////////////////////////////
    // Animation
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Note: the anim enter, exit, popEnter, popExit<br/>
     * was create by ({@link AnimationStyle} and {@link AnimDirection}) the value always < 0 <br/>
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
        return withDirection(AnimType.ANIMATION, style, direction, AnimType.ANIMATION, popStyle, popDirection);
    }

    @NonNull
    public static FragmentTransition createAnimationWithBehavior(@AnimatorStyle int style, @AnimBehavior int behavior) {
        return createAnimationWithBehavior(style, behavior, style, behavior);
    }

    @NonNull
    public static FragmentTransition createAnimationWithBehavior(@AnimatorStyle int style, @AnimBehavior int behavior, @AnimatorStyle int popStyle, @AnimBehavior int popBehavior) {
        return withBehavior(AnimType.ANIMATION, style, behavior, AnimType.ANIMATION, popStyle, popBehavior);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Animator
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Note: the anim enter, exit, popEnter, popExit<br/>
     * was create by ({@link AnimatorStyle} and {@link AnimDirection}) the value always < 0 <br/>
     * {@link BaseFragment#onCreateAnimation(int, boolean, int)}
     */
    @NonNull
    public static FragmentTransition createAnimator(@AnimatorStyle int style, @AnimDirection int direction) {
        return createAnimator(style, direction, style, direction);
    }

    /**
     * Note: the anim enter, exit, popEnter, popExit<br/>
     * was create by ({@link AnimatorStyle} and {@link AnimDirection}) the value always < 0 <br/>
     * {@link BaseFragment#onCreateAnimation(int, boolean, int)}
     */
    @NonNull
    public static FragmentTransition createAnimator(@AnimatorStyle int style, @AnimDirection int direction, @AnimatorStyle int popStyle, @AnimDirection int popDirection) {
        return withDirection(AnimType.ANIMATOR, style, direction, AnimType.ANIMATOR, popStyle, popDirection);
    }

    @NonNull
    public static FragmentTransition createAnimatorWithBehavior(@AnimatorStyle int style, @AnimBehavior int behavior) {
        return createAnimatorWithBehavior(style, behavior, style, behavior);
    }

    @NonNull
    public static FragmentTransition createAnimatorWithBehavior(@AnimatorStyle int style, @AnimBehavior int behavior, @AnimatorStyle int popStyle, @AnimBehavior int popBehavior) {
        return withBehavior(AnimType.ANIMATOR, style, behavior, AnimType.ANIMATOR, popStyle, popBehavior);
    }

    @NonNull
    public static FragmentTransition createCircularAnimator() {
        return createCircularAnimator(AnimBehavior.IN, AnimBehavior.OUT);
    }

    @NonNull
    public static FragmentTransition createCircularAnimator(@AnimBehavior int behavior, @AnimBehavior int popBehavior) {
        int type = AnimType.ANIMATOR;
        int style = AnimatorStyle.CIRCULAR_REVEAL;

        AnimOptionsOrAnim enter = new AnimOptionsOrAnim(createOptions(type, style).animBehavior(behavior).build());
        AnimOptionsOrAnim exit = null;
        AnimOptionsOrAnim popEnter = null;
        AnimOptionsOrAnim popExit = new AnimOptionsOrAnim(createOptions(type, style).animBehavior(popBehavior).build());

        return create(enter, exit, popEnter, popExit);
    }

    ///////////////////////////////////////////////////////////////////////////
    // mix utils
    ///////////////////////////////////////////////////////////////////////////

    public static FragmentTransition create(AnimOptionsOrAnim enter,
                                            AnimOptionsOrAnim exit,
                                            AnimOptionsOrAnim popEnter,
                                            AnimOptionsOrAnim popExit) {
        return new CustomFragmentTransition.Builder()
                .enter(enter)
                .exit(exit)
                .popEnter(popEnter)
                .popExit(popExit)
                .build();
    }

    private static FragmentTransition withDirection(int type, int style, int direction,
                                                    int popType, int popStyle, int popDirection) {
        AnimOptionsOrAnim enterExit = new AnimOptionsOrAnim(createOptions(type, style)
                .animDirection(direction)
                .build());
        AnimOptionsOrAnim popEnterExit = new AnimOptionsOrAnim(createOptions(popType, popStyle)
                .animDirection(popDirection)
                .build());
        return create(enterExit, enterExit, popEnterExit, popEnterExit);
    }

    private static FragmentTransition withBehavior(int type, int style, int behavior,
                                                   int popType, int popStyle, int popBehavior) {
        AnimOptionsOrAnim enterExit = new AnimOptionsOrAnim(createOptions(type, style)
                .animBehavior(behavior)
                .build());
        AnimOptionsOrAnim popEnterExit = new AnimOptionsOrAnim(createOptions(popType, popStyle)
                .animBehavior(popBehavior)
                .build());
        return create(enterExit, enterExit, popEnterExit, popEnterExit);
    }

    private static AnimOptions.Builder createOptions(@AnimType int type, int style) {
        return new AnimOptions.Builder().animType(type).animStyle(style);
    }

    private FragmentTransitionFactory() {
        //no instance
    }

    private static class CustomFragmentTransition implements FragmentTransition {

        private int enter;
        private int exit;
        private int popEnter;
        private int popExit;

        public CustomFragmentTransition() {
        }

        public CustomFragmentTransition(int enter, int exit, int popEnter, int popExit) {
            this.enter = enter;
            this.exit = exit;
            this.popEnter = popEnter;
            this.popExit = popExit;
        }

        @Override
        public void applyTransition(@NonNull FragmentTransaction transaction) {
            transaction.setCustomAnimations(enter, exit, popEnter, popExit);
        }

        @Override
        public boolean couldPopImmediate() {
            AnimOptions options = AnimOptions.fromOptionsValue(enter);
            // do not pop immediate when style is CIRCULAR_REVEAL
            if (options.getAnimStyle() == AnimatorStyle.CIRCULAR_REVEAL) {
                return false;
            }
            return FragmentTransition.super.couldPopImmediate();
        }

        public static class Builder {
            private int enter;
            private int exit;
            private int popEnter;

            private int popExit;

            public Builder enter(AnimOptionsOrAnim options) {
                this.enter = options == null ? 0 : options.getValue();
                return this;
            }

            public Builder exit(AnimOptionsOrAnim options) {
                this.exit = options == null ? 0 : options.getValue();
                return this;
            }

            public Builder popEnter(AnimOptionsOrAnim options) {
                this.popEnter = options == null ? 0 : options.getValue();
                return this;
            }

            public Builder popExit(AnimOptionsOrAnim options) {
                this.popExit = options == null ? 0 : options.getValue();
                return this;
            }

            public CustomFragmentTransition build() {
                return new CustomFragmentTransition(enter, exit, popEnter, popExit);
            }

        }
    }

    private static class TransitFragmentTransition implements FragmentTransition {

        @Transit
        private final int transit;

        public TransitFragmentTransition(@Transit int transit) {
            this.transit = transit;
        }

        @Override
        public void applyTransition(@NonNull FragmentTransaction transaction) {
            transaction.setTransition(transit);
        }

        @Override
        public boolean couldPopImmediate() {
            return false;
        }
    }
}
