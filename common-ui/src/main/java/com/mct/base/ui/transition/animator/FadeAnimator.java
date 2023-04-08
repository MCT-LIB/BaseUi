package com.mct.base.ui.transition.animator;

import static com.mct.base.ui.transition.annotation.AnimBehavior.IN;
import static com.mct.base.ui.transition.annotation.AnimBehavior.NONE;
import static com.mct.base.ui.transition.annotation.AnimBehavior.OUT;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimBehavior;

public class FadeAnimator extends ViewPropertyAnimator {

    protected final @AnimBehavior int mBehavior;
    protected final boolean mEnter;

    @NonNull
    public static Animator create(View view, @AnimBehavior int behavior, boolean enter, long duration) {
        switch (behavior) {
            case IN:
                return new FadeInAnimator(view, behavior, enter, duration).init();
            case OUT:
            case NONE:
            default:
                return new FadeOutAnimator(view, behavior, enter, duration).init();
        }
    }

    public FadeAnimator(View view, @AnimBehavior int behavior, boolean enter, long duration) {
        super(view);
        this.mBehavior = behavior;
        this.mEnter = enter;
        setDuration(duration);
    }

    private static class FadeInAnimator extends FadeAnimator {

        public FadeInAnimator(View view, int behavior, boolean enter, long duration) {
            super(view, behavior, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(target, "alpha", 0f, 1f);
            animator.play(alphaAnimator);
        }
    }

    private static class FadeOutAnimator extends FadeAnimator {

        public FadeOutAnimator(View view, int behavior, boolean enter, long duration) {
            super(view, behavior, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(target, "alpha", 1f, 0f);
            animator.play(alphaAnimator);
        }
    }

}