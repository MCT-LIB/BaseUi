package com.mct.base.ui.transition.animator;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.mct.base.ui.utils.ScreenUtils;

public class CircularAnimator extends ViewPropertyAnimator {

    protected final int centerX;
    protected final int centerY;
    protected final boolean mEnter;

    public CircularAnimator(View view, int centerX, int centerY, boolean enter, long duration) {
        super(view);
        this.centerX = centerX;
        this.centerY = centerY;
        this.mEnter = enter;
        setValues();
    }

    private void setValues() {
        Animator animator;
        int screenHeight = ScreenUtils.getScreenHeight(mView.getContext());
        if (mEnter) {
            animator = ViewAnimationUtils.createCircularReveal(mView, centerX, centerY, 0, screenHeight);
        } else {
            animator = ViewAnimationUtils.createCircularReveal(mView, centerX, centerY, screenHeight, 0);
        }
        setDuration(animator.getDuration());
        mAnimatorSet.play(animator);
    }
}