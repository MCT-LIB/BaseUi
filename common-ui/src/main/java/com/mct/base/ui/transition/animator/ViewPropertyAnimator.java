package com.mct.base.ui.transition.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;

public class ViewPropertyAnimator extends Animator {

    protected final View mView;
    protected final AnimatorSet mAnimatorSet;

    public ViewPropertyAnimator(View view) {
        mView = view;
        mAnimatorSet = new AnimatorSet();
    }

    @Override
    public void start() {
        mAnimatorSet.start();
    }

    @Override
    public void cancel() {
        mAnimatorSet.cancel();
    }

    @Override
    public long getStartDelay() {
        return mAnimatorSet.getStartDelay();
    }

    @Override
    public void setStartDelay(long startDelay) {
        mAnimatorSet.setStartDelay(startDelay);
    }

    @Override
    public boolean isRunning() {
        return mAnimatorSet.isRunning();
    }

    public void setInterpolator(TimeInterpolator interpolator) {
        mAnimatorSet.setInterpolator(interpolator);
    }

    @Override
    public Animator setDuration(long duration) {
        mAnimatorSet.setDuration(duration);
        return this;
    }

    @Override
    public long getDuration() {
        return mAnimatorSet.getDuration();
    }
}
