package com.mct.base.ui.transition.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.annotation.NonNull;

public class NoneAnimator extends ViewPropertyAnimator {

    @NonNull
    public static NoneAnimator create(View view, int duration) {
        return new NoneAnimator(view, duration);
    }

    public NoneAnimator(View view, int duration) {
        super(view);
        setDuration(duration);
    }

    @Override
    protected void initialAnimator(View target, AnimatorSet animator) {
        super.initialAnimator(target, animator);
        animator.play(ObjectAnimator.ofFloat(target, "alpha", 1, 1));
    }
}
