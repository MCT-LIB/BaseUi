package com.mct.base.ui.transition.animation;

import static com.mct.base.ui.transition.annotation.AnimDirection.DOWN;
import static com.mct.base.ui.transition.annotation.AnimDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimDirection.NONE;
import static com.mct.base.ui.transition.annotation.AnimDirection.RIGHT;
import static com.mct.base.ui.transition.annotation.AnimDirection.UP;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Transformation;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimDirection;

/**
 * 3D Cube Animation
 */
public class SidesAnimation extends ViewPropertyAnimation {

    protected final @AnimDirection int mDirection;
    protected final boolean mEnter;

    /**
     * Create new Animation.
     *
     * @param direction Direction of animation
     * @param enter     true for Enter / false for Exit
     * @param duration  Duration of Animation
     * @return SidesAnimation
     */
    public static @NonNull SidesAnimation create(@AnimDirection int direction, boolean enter, long duration) {
        switch (direction) {
            case UP:
            case DOWN:
                return new VerticalCubeAnimation(direction, enter, duration);
            case LEFT:
            case RIGHT:
            case NONE:
            default:
                return new HorizontalCubeAnimation(direction, enter, duration);
        }
    }

    private SidesAnimation(@AnimDirection int direction, boolean enter, long duration) {
        mDirection = direction;
        mEnter = enter;
        setDuration(duration);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private static class VerticalCubeAnimation extends SidesAnimation {

        private VerticalCubeAnimation(@AnimDirection int direction, boolean enter, long duration) {
            super(direction, enter, duration);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mPivotX = width * 0.5f;
            mPivotY = (mEnter == (mDirection == DOWN)) ? 0.0f : height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float value = mEnter ? (interpolatedTime - 1.0f) : interpolatedTime;
            if (mDirection == UP) value *= -1.0f;
            mRotationX = value * 90.0f;
            mAlpha = mEnter ? interpolatedTime : (1.0f - interpolatedTime);
            mTranslationZ = (1.0f - mAlpha) * mWidth;

            super.applyTransformation(interpolatedTime, t);
            applyTransformation(t);
        }

    }

    private static class HorizontalCubeAnimation extends SidesAnimation {

        private HorizontalCubeAnimation(@AnimDirection int direction, boolean enter, long duration) {
            super(direction, enter, duration);
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mPivotX = (mEnter == (mDirection == RIGHT)) ? 0.0f : width;
            mPivotY = height * 0.5f;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float value = mEnter ? (interpolatedTime - 1.0f) : interpolatedTime;
            if (mDirection == LEFT) value *= -1.0f;
            mRotationY = -value * 90.0f;
            mAlpha = mEnter ? interpolatedTime : (1.0f - interpolatedTime);
            mTranslationZ = (1.0f - mAlpha) * mHeight;

            super.applyTransformation(interpolatedTime, t);
            applyTransformation(t);
        }

    }

}