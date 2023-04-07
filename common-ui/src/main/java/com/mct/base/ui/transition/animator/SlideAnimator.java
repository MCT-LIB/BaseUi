package com.mct.base.ui.transition.animator;

import static com.mct.base.ui.transition.annotation.AnimDirection.DOWN;
import static com.mct.base.ui.transition.annotation.AnimDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimDirection.NONE;
import static com.mct.base.ui.transition.annotation.AnimDirection.RIGHT;
import static com.mct.base.ui.transition.annotation.AnimDirection.UP;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimDirection;

public class SlideAnimator extends ViewPropertyAnimator {

    protected final @AnimDirection int mDirection;
    protected final boolean mEnter;

    /**
     * Create new Animation.
     *
     * @param view      Target of animation
     * @param direction Direction of animation
     * @param enter     true for Enter / false for Exit
     * @param duration  Duration of Animation
     * @return SidesAnimation
     */
    public static @NonNull Animator create(View view, @AnimDirection int direction, boolean enter, long duration) {
        switch (direction) {
            case UP:
            case DOWN:
                return new VerticalMoveAnimation(view, direction, enter, duration).init();
            case LEFT:
            case RIGHT:
            case NONE:
            default:
                return new HorizontalMoveAnimation(view, direction, enter, duration).init();
        }
    }

    public SlideAnimator(View view, @AnimDirection int direction, boolean enter, long duration) {
        super(view);
        mDirection = direction;
        mEnter = enter;
        setDuration(duration);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private static class VerticalMoveAnimation extends SlideAnimator {

        public VerticalMoveAnimation(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            int value = target.getHeight();
            animator.play(mDirection == UP
                    ? ObjectAnimator.ofFloat(target, "translationY", mEnter ? value : -value)
                    : ObjectAnimator.ofFloat(target, "translationY", mEnter ? -value : value));
        }
    }

    private static class HorizontalMoveAnimation extends SlideAnimator {

        public HorizontalMoveAnimation(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            int value = target.getWidth();
            animator.play(mDirection == LEFT
                    ? ObjectAnimator.ofFloat(target, "translationX", mEnter ? value : -value)
                    : ObjectAnimator.ofFloat(target, "translationX", mEnter ? -value : value));
        }
    }

}
