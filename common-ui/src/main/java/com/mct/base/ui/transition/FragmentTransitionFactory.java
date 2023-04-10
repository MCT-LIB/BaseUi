package com.mct.base.ui.transition;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.BaseFragment;
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
        return new CustomFragmentTransition.Builder().build();
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
        return create(
                new AnimOptionsOrAnim(enter), new AnimOptionsOrAnim(exit),
                new AnimOptionsOrAnim(popEnter), new AnimOptionsOrAnim(popExit)
        );
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
    public static FragmentTransition createCircularAnimator() {
        AnimOptions circularReveal = createOptions(AnimType.ANIMATOR, AnimatorStyle.CIRCULAR_REVEAL).build();
        AnimOptions fade = createOptions(AnimType.ANIMATOR, AnimatorStyle.FADE).build();

        AnimOptionsOrAnim enter = new AnimOptionsOrAnim(circularReveal);
        AnimOptionsOrAnim exit = new AnimOptionsOrAnim(fade);
        AnimOptionsOrAnim popEnter = new AnimOptionsOrAnim(fade);
        AnimOptionsOrAnim popExit = new AnimOptionsOrAnim(circularReveal);

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

    private static AnimOptions.Builder createOptions(@AnimType int type, int style) {
        return new AnimOptions.Builder().animType(type).animStyle(style);
    }

    private FragmentTransitionFactory() {
        //no instance
    }

    private static class CustomFragmentTransition implements FragmentTransition {

        private final int enter;
        private final int exit;
        private final int popEnter;
        private final int popExit;

        private CustomFragmentTransition(@NonNull Builder builder) {
            this.enter = builder.enter;
            this.exit = builder.exit;
            this.popEnter = builder.popEnter;
            this.popExit = builder.popExit;
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
                return new CustomFragmentTransition(this);
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
