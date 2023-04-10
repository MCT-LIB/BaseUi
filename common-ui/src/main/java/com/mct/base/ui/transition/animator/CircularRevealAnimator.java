package com.mct.base.ui.transition.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

public class CircularRevealAnimator extends ViewPropertyAnimator {

    private static final int OFFSET_DURATION = 200;
    protected final boolean mEnter;
    protected final int mCenterX;
    protected final int mCenterY;

    /**
     * Create new Animator.
     *
     * @param view     Target of animator
     * @param enter    true for Enter / false for Exit
     * @param duration Duration of animator
     * @return FadeAnimator
     */
    @NonNull
    public static CircularRevealAnimator create(View view, boolean enter, int duration, int centerX, int centerY) {
        if (enter) {
            return new CircularRevealInAnimator(view, enter, duration, centerX, centerY);
        } else {
            return new CircularRevealOutAnimator(view, enter, duration, centerX, centerY);
        }
    }

    public CircularRevealAnimator(View view, boolean enter, int duration, int centerX, int centerY) {
        super(view);
        this.mEnter = enter;
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        setDuration(duration + OFFSET_DURATION);
    }

    private static class CircularRevealInAnimator extends CircularRevealAnimator {

        public CircularRevealInAnimator(View view, boolean enter, int duration, int centerX, int centerY) {
            super(view, enter, duration, centerX, centerY);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            View parent = getTargetParent();
            int endRadius = (int) Math.hypot(parent.getWidth(), parent.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(target, mCenterX, mCenterY, 0f, endRadius);
            animator.play(circularReveal);
            animator.setInterpolator(new AccelerateInterpolator(1f));
        }
    }

    private static class CircularRevealOutAnimator extends CircularRevealAnimator {

        public CircularRevealOutAnimator(View view, boolean enter, int duration, int centerX, int centerY) {
            super(view, enter, duration, centerX, centerY);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            View parent = getTargetParent();
            int startRadius = (int) Math.hypot(parent.getWidth(), parent.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(target, mCenterX, mCenterY, startRadius, 0f);
            animator.play(circularReveal);
            animator.setInterpolator(new DecelerateInterpolator(1f));
        }
    }

}