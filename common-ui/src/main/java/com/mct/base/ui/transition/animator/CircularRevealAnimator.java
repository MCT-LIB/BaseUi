package com.mct.base.ui.transition.animator;

import static com.mct.base.ui.transition.annotation.AnimBehavior.IN;
import static com.mct.base.ui.transition.annotation.AnimBehavior.NONE;
import static com.mct.base.ui.transition.annotation.AnimBehavior.OUT;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimBehavior;

public class CircularRevealAnimator extends ViewPropertyAnimator {

    private static final int OFFSET_DURATION = 200;
    protected final @AnimBehavior int mBehavior;
    protected final boolean mEnter;
    protected final int mCenterX;
    protected final int mCenterY;

    @NonNull
    public static Animator create(View view, @AnimBehavior int behavior, boolean enter, int duration, int centerX, int centerY) {
        switch (behavior) {
            case IN:
                return new CircularRevealInAnimator(view, behavior, enter, duration, centerX, centerY).init();
            case OUT:
            case NONE:
            default:
                return new CircularRevealOutAnimator(view, behavior, enter, duration, centerX, centerY).init();
        }
    }

    public CircularRevealAnimator(View view, @AnimBehavior int behavior, boolean enter, int duration, int centerX, int centerY) {
        super(view);
        this.mBehavior = behavior;
        this.mEnter = enter;
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        setDuration(duration + OFFSET_DURATION);
    }

    private static class CircularRevealInAnimator extends CircularRevealAnimator {

        public CircularRevealInAnimator(View view, int behavior, boolean enter, int duration, int centerX, int centerY) {
            super(view, behavior, enter, duration, centerX, centerY);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            int endRadius = (int) Math.hypot(target.getWidth(), target.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(target, mCenterX, mCenterY, 0f, endRadius);
            animator.play(circularReveal);
            animator.setInterpolator(new AccelerateInterpolator(1f));
        }
    }

    private static class CircularRevealOutAnimator extends CircularRevealAnimator {

        public CircularRevealOutAnimator(View view, int behavior, boolean enter, int duration, int centerX, int centerY) {
            super(view, behavior, enter, duration, centerX, centerY);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            int startRadius = (int) Math.hypot(target.getWidth(), target.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(target, mCenterX, mCenterY, startRadius, 0f);
            animator.play(circularReveal);
            animator.setInterpolator(new DecelerateInterpolator(1f));
        }
    }

}