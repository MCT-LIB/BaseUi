package com.mct.base.ui.transition.animator;

import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.view.View;

import androidx.annotation.CallSuper;

import java.lang.ref.WeakReference;

public class ViewPropertyAnimator {

    private final WeakReference<View> mTarget;
    private final AnimatorSet mAnimatorSet;
    private boolean mIsInit;

    public ViewPropertyAnimator(View view) {
        mTarget = new WeakReference<>(view);
        mAnimatorSet = new AnimatorSet();
    }

    protected final AnimatorSet init() {
        if (!mIsInit) {
            initialAnimator(getTarget(), getAnimator());
        }
        return getAnimator();
    }

    @CallSuper
    protected void initialAnimator(View target, AnimatorSet animator) {
        mIsInit = true;
    }

    protected View getTarget() {
        return mTarget.get();
    }

    protected AnimatorSet getAnimator() {
        return mAnimatorSet;
    }

    protected void setStartDelay(long startDelay) {
        mAnimatorSet.setStartDelay(startDelay);
    }

    protected void setDuration(long duration) {
        mAnimatorSet.setDuration(duration);
    }

    protected void setInterpolator(TimeInterpolator interpolator) {
        mAnimatorSet.setInterpolator(interpolator);
    }

}
