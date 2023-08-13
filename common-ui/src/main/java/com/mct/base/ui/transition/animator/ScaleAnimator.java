package com.mct.base.ui.transition.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.mct.base.ui.transition.annotation.AnimDirection;

public class ScaleAnimator extends ViewPropertyAnimator {

    protected final @AnimDirection int mDirection;
    protected final boolean mEnter;

    /**
     * Create new Animator.
     *
     * @param view      Target of animator
     * @param direction Direction of animator
     * @param enter     true for Enter / false for Exit
     * @param duration  Duration of animator
     * @return ScaleAnimator
     */
    public static @NonNull ScaleAnimator create(View view, @AnimDirection int direction, boolean enter, long duration) {
        switch (direction) {
            case AnimDirection.UP:
                return new ScaleUpAnimator(view, direction, enter, duration);
            case AnimDirection.DOWN:
                return new ScaleDownAnimator(view, direction, enter, duration);
            case AnimDirection.LEFT:
            case AnimDirection.NONE:
            case AnimDirection.RIGHT:
            default:
                return new ScaleAnimator(view, direction, enter, duration);
        }
    }

    public ScaleAnimator(View view, int direction, boolean enter, long duration) {
        super(view);
        this.mDirection = direction;
        this.mEnter = enter;
        setDuration(duration);
    }

    public static class ScaleUpAnimator extends ScaleAnimator {

        public ScaleUpAnimator(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            AnimatorSet alphaSet = new AnimatorSet();
            if (mEnter) {
                alphaSet.playSequentially(
                        ObjectAnimator.ofFloat(target, "alpha", 0, 0).setDuration(66),
                        ObjectAnimator.ofFloat(target, "alpha", 0, 1).setDuration(50)
                );
                animator.playTogether(alphaSet);
            } else {
                alphaSet.playSequentially(
                        ObjectAnimator.ofFloat(target, "alpha", 1, 1).setDuration(66),
                        ObjectAnimator.ofFloat(target, "alpha", 1, 0).setDuration(50)
                );
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 1f, 1.15f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 1f, 1.15f);
                scaleX.setInterpolator(new FastOutLinearInInterpolator());
                scaleY.setInterpolator(new FastOutLinearInInterpolator());
                animator.playTogether(alphaSet, scaleX, scaleY);
            }
        }
    }

    public static class ScaleDownAnimator extends ScaleAnimator {

        public ScaleDownAnimator(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            if (mEnter) {
                ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1f).setDuration(50);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 1.2f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 1.2f, 1f);
                scaleX.setInterpolator(new DecelerateInterpolator());
                scaleY.setInterpolator(new DecelerateInterpolator());
                animator.playTogether(alpha, scaleX, scaleY);
            } else {
                ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0f).setDuration(50);
                animator.playTogether(alpha);
            }
        }
    }
}
