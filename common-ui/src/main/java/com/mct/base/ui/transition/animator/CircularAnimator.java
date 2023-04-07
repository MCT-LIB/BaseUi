package com.mct.base.ui.transition.animator;

import static com.mct.base.ui.transition.annotation.AnimBehavior.IN;
import static com.mct.base.ui.transition.annotation.AnimBehavior.NONE;
import static com.mct.base.ui.transition.annotation.AnimBehavior.OUT;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimBehavior;

public class CircularAnimator extends ViewPropertyAnimator {

    protected final @AnimBehavior int mBehavior;
    protected final boolean mEnter;
    protected final int mCenterX;
    protected final int mCenterY;

    @NonNull
    public static Animator create(View view, @AnimBehavior int behavior, boolean enter, long duration, int centerX, int centerY) {
        switch (behavior) {
            case IN:
                return new InCircularAnimator(view, behavior, enter, duration, centerX, centerY).init();
            case OUT:
            case NONE:
            default:
                return new OutCircularAnimator(view, behavior, enter, duration, centerX, centerY).init();
        }
    }

    public CircularAnimator(View view, @AnimBehavior int behavior, boolean enter, long duration, int centerX, int centerY) {
        super(view);
        this.mBehavior = behavior;
        this.mEnter = enter;
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        setDuration(duration);
    }

    private static class InCircularAnimator extends CircularAnimator {

        public InCircularAnimator(View view, int behavior, boolean enter, long duration, int centerX, int centerY) {
            super(view, behavior, enter, duration, centerX, centerY);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            int endRadius = (int) Math.hypot(target.getWidth(), target.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(target, mCenterX, mCenterY, 0f, endRadius);
            animator.play(circularReveal);

            ObjectAnimator colorAnimator = ObjectAnimator.ofArgb(target, "backgroundColor", Color.WHITE, Color.GRAY);
            animator.play(colorAnimator);

            animator.setDuration(animator.getDuration() + 300);
            animator.setInterpolator(new LinearInterpolator());
        }
    }

    private static class OutCircularAnimator extends CircularAnimator {

        public OutCircularAnimator(View view, int behavior, boolean enter, long duration, int centerX, int centerY) {
            super(view, behavior, enter, duration, centerX, centerY);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            int startRadius = (int) Math.hypot(target.getWidth(), target.getHeight());
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(target, mCenterX, mCenterY, startRadius, 0f);
            animator.play(circularReveal);

            ObjectAnimator colorAnimator = ObjectAnimator.ofArgb(target, "backgroundColor", Color.WHITE, Color.GRAY);
            animator.play(colorAnimator);

            animator.setDuration(animator.getDuration() + 300);
            animator.setInterpolator(new LinearInterpolator());
        }
    }

}