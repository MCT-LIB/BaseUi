package com.mct.base.ui.transition.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.annotation.NonNull;

public class FadeAnimator extends ViewPropertyAnimator {

    protected final boolean mEnter;

    /**
     * Create new Animator.
     *
     * @param view     Target of animator
     * @param enter    true for Enter / false for Exit
     * @param duration Duration of animator
     * @return FadeAnimator
     */
    @NonNull
    public static FadeAnimator create(View view, boolean enter, long duration) {
        if (enter) {
            return new FadeInAnimator(view, enter, duration);
        } else {
            return new FadeOutAnimator(view, enter, duration);
        }
    }

    public FadeAnimator(View view, boolean enter, long duration) {
        super(view);
        this.mEnter = enter;
        setDuration(duration);
    }

    private static class FadeInAnimator extends FadeAnimator {

        public FadeInAnimator(View view, boolean enter, long duration) {
            super(view, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(target, "alpha", 0f, 1f);
            animator.play(alphaAnimator);
        }
    }

    private static class FadeOutAnimator extends FadeAnimator {

        public FadeOutAnimator(View view, boolean enter, long duration) {
            super(view, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(target, "alpha", 1f, 0f);
            animator.play(alphaAnimator);
        }
    }

}