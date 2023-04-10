package com.mct.base.ui.transition.animator;

import static com.mct.base.ui.transition.annotation.AnimDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimDirection.RIGHT;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimDirection;

public class RotateAnimator extends ViewPropertyAnimator {

    protected final @AnimDirection int mDirection;
    protected final boolean mEnter;

    /**
     * Create new Animator.
     *
     * @param view      Target of animator
     * @param direction Direction of animator
     * @param enter     true for Enter / false for Exit
     * @param duration  Duration of animator
     * @return MoveAnimator
     */
    public static @NonNull RotateAnimator create(View view, @AnimDirection int direction, boolean enter, long duration) {
        return new RotateAnimator(view, direction, enter, duration);
    }

    public static @NonNull RotateAnimator createRotateUp(View view, @AnimDirection int direction, boolean enter, long duration) {
        if (direction == LEFT || direction == RIGHT) {
            return new RotateUpAnimator(view, direction, enter, duration);
        } else {
            return create(view, direction, enter, duration);
        }
    }

    public static @NonNull RotateAnimator createRotateDown(View view, @AnimDirection int direction, boolean enter, long duration) {
        if (direction == LEFT || direction == RIGHT) {
            return new RotateDownAnimator(view, direction, enter, duration);
        } else {
            return create(view, direction, enter, duration);
        }
    }

    public RotateAnimator(View view, @AnimDirection int direction, boolean enter, long duration) {
        super(view);
        mDirection = direction;
        mEnter = enter;
        setDuration(duration);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    protected boolean isInitAnimator() {
        return false;
    }

    @Override
    protected void initialAnimator(View target, AnimatorSet animator) {
        super.initialAnimator(target, animator);
        if (isInitAnimator()) {
            return;
        }
        int rotate = 180;
        animator.play(ObjectAnimator.ofFloat(target, "rotation", mEnter ? -rotate : 0, mEnter ? 0 : rotate));
    }

    private static class RotateUpAnimator extends RotateAnimator {

        public RotateUpAnimator(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        protected boolean isInitAnimator() {
            return true;
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            View parent = getTargetParent();
            float x = mDirection == LEFT ? parent.getPaddingLeft() : parent.getWidth() - parent.getPaddingRight();
            float y = parent.getHeight() - parent.getPaddingBottom();
            animator.playTogether(
                    ObjectAnimator.ofFloat(target, "pivotX", x, x),
                    ObjectAnimator.ofFloat(target, "pivotY", y, y)
            );
            animator.play(mEnter
                    ? ObjectAnimator.ofFloat(target, "rotation", mDirection == LEFT ? 90 : -90, 0)
                    : ObjectAnimator.ofFloat(target, "rotation", 0, mDirection == LEFT ? -90 : 90));
        }
    }

    private static class RotateDownAnimator extends RotateAnimator {

        public RotateDownAnimator(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        protected boolean isInitAnimator() {
            return true;
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            View parent = getTargetParent();
            float x = mDirection == RIGHT ? parent.getPaddingLeft() : parent.getWidth() - parent.getPaddingRight();
            float y = parent.getHeight() - parent.getPaddingBottom();
            animator.playTogether(
                    ObjectAnimator.ofFloat(target, "pivotX", x, x),
                    ObjectAnimator.ofFloat(target, "pivotY", y, y)
            );
            animator.play(mEnter
                    ? ObjectAnimator.ofFloat(target, "rotation", mDirection == RIGHT ? -90 : 90, 0)
                    : ObjectAnimator.ofFloat(target, "rotation", 0, mDirection == RIGHT ? 90 : -90));
        }
    }

}
