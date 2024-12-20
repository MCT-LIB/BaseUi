package com.mct.base.ui.transition.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.os.Build;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ViewPropertyAnimator extends Animator {

    private final WeakReference<View> mTarget;
    private final AnimatorSet mAnimatorSet;
    private boolean mIsInit;

    private float mFromAlpha = -1.0f;
    private float mToAlpha = -1.0f;

    public ViewPropertyAnimator(View view) {
        mTarget = new WeakReference<>(view);
        mAnimatorSet = new AnimatorSet();
    }

    public ViewPropertyAnimator fading(@FloatRange(from = 0.0f, to = 1.0f) float fromAlpha, @FloatRange(from = 0.0f, to = 1.0f) float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
        return this;
    }

    protected View getTargetParent() {
        View view = mTarget.get();
        View parent = (View) view.getParent();
        if (parent != null) {
            return parent;
        }
        // try all way to get parent
        return view;
    }

    @CallSuper
    protected void initialAnimator(View target, AnimatorSet animator) {
        mIsInit = true;
        if (mFromAlpha >= 0 && mToAlpha >= 0) {
            animator.play(ObjectAnimator.ofFloat(target, "alpha", mFromAlpha, mToAlpha));
        }
    }

    @Override
    public void start() {
        View target = mTarget.get();
        if (target == null) {
            return;
        }
        if (target.getHandler() == null) {
            target.post(this::start);
            return;
        }
        if (!mIsInit) {
            initialAnimator(mTarget.get(), mAnimatorSet);
        }
        mAnimatorSet.start();
    }

    @Override
    public void cancel() {
        mAnimatorSet.cancel();
    }

    @Override
    public void end() {
        mAnimatorSet.end();
    }

    @Override
    public void pause() {
        mAnimatorSet.pause();
    }

    @Override
    public void resume() {
        mAnimatorSet.resume();
    }

    @Override
    public boolean isPaused() {
        return mAnimatorSet.isPaused();
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public long getTotalDuration() {
        return mAnimatorSet.getTotalDuration();
    }

    @Override
    public TimeInterpolator getInterpolator() {
        return mAnimatorSet.getInterpolator();
    }

    @Override
    public boolean isStarted() {
        return mAnimatorSet.isStarted();
    }

    @Override
    public void addListener(AnimatorListener listener) {
        mAnimatorSet.addListener(listener);
    }

    @Override
    public void removeListener(AnimatorListener listener) {
        mAnimatorSet.removeListener(listener);
    }

    @Override
    public ArrayList<AnimatorListener> getListeners() {
        return mAnimatorSet.getListeners();
    }

    @Override
    public void addPauseListener(AnimatorPauseListener listener) {
        mAnimatorSet.addPauseListener(listener);
    }

    @Override
    public void removePauseListener(AnimatorPauseListener listener) {
        mAnimatorSet.removePauseListener(listener);
    }

    @Override
    public void removeAllListeners() {
        mAnimatorSet.removeAllListeners();
    }

    @Override
    public void setupStartValues() {
        mAnimatorSet.setupStartValues();
    }

    @Override
    public void setupEndValues() {
        mAnimatorSet.setupEndValues();
    }

    @Override
    public void setTarget(@Nullable Object target) {
        mAnimatorSet.setTarget(target);
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
    public Animator setDuration(long duration) {
        return mAnimatorSet.setDuration(duration);
    }

    @Override
    public long getDuration() {
        return mAnimatorSet.getDuration();
    }

    @Override
    public void setInterpolator(TimeInterpolator interpolator) {
        mAnimatorSet.setInterpolator(interpolator);
    }

    @Override
    public boolean isRunning() {
        return mAnimatorSet.isRunning();
    }

}
