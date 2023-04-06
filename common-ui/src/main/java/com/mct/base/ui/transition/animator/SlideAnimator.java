package com.mct.base.ui.transition.animator;

import static com.mct.base.ui.transition.annotation.AnimDirection.DOWN;
import static com.mct.base.ui.transition.annotation.AnimDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimDirection.RIGHT;
import static com.mct.base.ui.transition.annotation.AnimDirection.UP;

import android.animation.ObjectAnimator;
import android.view.View;

import com.mct.base.ui.transition.annotation.AnimDirection;

public class SlideAnimator extends ViewPropertyAnimator {

    protected final @AnimDirection int mDirection;
    protected final boolean mEnter;

    public SlideAnimator(View view, @AnimDirection int direction, boolean enter, long duration) {
        super(view);
        mDirection = direction;
        mEnter = enter;
        setDuration(duration);
        setValues();
    }

    private void setValues() {
        switch (mDirection) {
            case LEFT:
                mAnimatorSet.play(ObjectAnimator.ofFloat(mView, "translationX", mEnter ? mView.getWidth() : -mView.getWidth()));
                break;
            case UP:
                mAnimatorSet.play(ObjectAnimator.ofFloat(mView, "translationY", mEnter ? mView.getHeight() : -mView.getHeight()));
                break;
            case RIGHT:
                mAnimatorSet.play(ObjectAnimator.ofFloat(mView, "translationX", mEnter ? -mView.getWidth() : mView.getWidth()));
                break;
            case DOWN:
                mAnimatorSet.play(ObjectAnimator.ofFloat(mView, "translationY", mEnter ? -mView.getHeight() : mView.getHeight()));
                break;
            case AnimDirection.NONE:
                break;
        }
    }
}
