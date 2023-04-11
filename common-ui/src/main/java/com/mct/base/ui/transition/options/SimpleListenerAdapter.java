package com.mct.base.ui.transition.options;

import android.animation.Animator;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

abstract class SimpleListenerAdapter implements Animation.AnimationListener, Animator.AnimatorListener {

    protected abstract void onAnimationStart();

    protected abstract void onAnimationEnd();

    @Override
    public void onAnimationStart(@NonNull Animator animation) {
        onAnimationStart();
    }

    @Override
    public void onAnimationEnd(@NonNull Animator animation) {
        onAnimationEnd();
    }

    @Override
    public void onAnimationCancel(@NonNull Animator animation) {
    }

    @Override
    public void onAnimationRepeat(@NonNull Animator animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
        onAnimationStart();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        onAnimationEnd();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

}
