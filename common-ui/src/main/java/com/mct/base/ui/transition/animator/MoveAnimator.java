package com.mct.base.ui.transition.animator;

import static com.mct.base.ui.transition.annotation.AnimDirection.DOWN;
import static com.mct.base.ui.transition.annotation.AnimDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimDirection.NONE;
import static com.mct.base.ui.transition.annotation.AnimDirection.RIGHT;
import static com.mct.base.ui.transition.annotation.AnimDirection.UP;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimDirection;

public class MoveAnimator extends ViewPropertyAnimator {

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
    public static @NonNull MoveAnimator create(View view, @AnimDirection int direction, boolean enter, long duration) {
        switch (direction) {
            case UP:
            case DOWN:
                return new VerticalMoveAnimator(view, direction, enter, duration);
            case LEFT:
            case RIGHT:
            case NONE:
            default:
                return new HorizontalMoveAnimator(view, direction, enter, duration);
        }
    }

    public MoveAnimator(View view, @AnimDirection int direction, boolean enter, long duration) {
        super(view);
        mDirection = direction;
        mEnter = enter;
        setDuration(duration);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private static class VerticalMoveAnimator extends MoveAnimator {

        public VerticalMoveAnimator(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            int value = getTargetParent().getHeight();
            animator.play(mDirection == UP
                    ? ObjectAnimator.ofFloat(target, "translationY", mEnter ? value : 0, mEnter ? 0 : -value)
                    : ObjectAnimator.ofFloat(target, "translationY", mEnter ? -value : 0, mEnter ? 0 : value));
        }
    }

    private static class HorizontalMoveAnimator extends MoveAnimator {

        public HorizontalMoveAnimator(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            int value = getTargetParent().getWidth();
            animator.play(mDirection == LEFT
                    ? ObjectAnimator.ofFloat(target, "translationX", mEnter ? value : 0, mEnter ? 0 : -value)
                    : ObjectAnimator.ofFloat(target, "translationX", mEnter ? -value : 0, mEnter ? 0 : value));
        }
    }

}
