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
import android.view.ViewGroup;
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
    public static @NonNull Animator create(View view, @AnimDirection int direction, boolean enter, long duration) {
        switch (direction) {
            case UP:
            case DOWN:
                return new VerticalMoveAnimator(view, direction, enter, duration).init();
            case LEFT:
            case RIGHT:
            case NONE:
            default:
                return new HorizontalMoveAnimator(view, direction, enter, duration).init();
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
            if (mDirection == UP) {
                if (mEnter) {
                    ViewGroup parent = (ViewGroup) target.getParent();
                    int distance = parent.getHeight() - target.getTop();
                    animator.playTogether(
                            ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                            ObjectAnimator.ofFloat(target, "translationY", distance, 0)
                    );
                } else {
                    animator.playTogether(
                            ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                            ObjectAnimator.ofFloat(target, "translationY", 0, -target.getBottom())
                    );
                }
            } else {
                if (mEnter) {
                    int distance = target.getTop() + target.getHeight();
                    animator.playTogether(
                            ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                            ObjectAnimator.ofFloat(target, "translationY", -distance, 0)
                    );
                } else {
                    ViewGroup parent = (ViewGroup) target.getParent();
                    int distance = parent.getHeight() - target.getTop();
                    animator.playTogether(
                            ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                            ObjectAnimator.ofFloat(target, "translationY", 0, distance)
                    );
                }
            }
        }
    }

    private static class HorizontalMoveAnimator extends MoveAnimator {

        public HorizontalMoveAnimator(View view, int direction, boolean enter, long duration) {
            super(view, direction, enter, duration);
        }

        @Override
        protected void initialAnimator(View target, AnimatorSet animator) {
            super.initialAnimator(target, animator);
            ViewGroup parent = (ViewGroup) target.getParent();
            int distance = parent.getWidth() - target.getLeft();
            if (mDirection == LEFT) {
                if (mEnter) {
                    animator.playTogether(
                            ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                            ObjectAnimator.ofFloat(target, "translationX", -distance, 0)
                    );
                } else {
                    animator.playTogether(
                            ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                            ObjectAnimator.ofFloat(target, "translationX", 0, distance)
                    );
                }
            } else {
                if (mEnter) {
                    animator.playTogether(
                            ObjectAnimator.ofFloat(target, "alpha", 0, 1),
                            ObjectAnimator.ofFloat(target, "translationX", distance, 0)
                    );
                } else {
                    animator.playTogether(
                            ObjectAnimator.ofFloat(target, "alpha", 1, 0),
                            ObjectAnimator.ofFloat(target, "translationX", 0, -distance)
                    );
                }
            }
        }
    }

}
