package com.mct.base.ui.transition.option;

import android.animation.Animator;
import android.view.animation.Animation;

public class AnimExtras {

    public final Animation animation;
    public final Animator animator;

    public AnimExtras(Animation animation) {
        this.animation = animation;
        this.animator = null;
        if (animation == null) {
            throw new IllegalStateException("Animation cannot be null");
        }
    }

    public AnimExtras(Animator animator) {
        this.animation = null;
        this.animator = animator;
        if (animator == null) {
            throw new IllegalStateException("Animator cannot be null");
        }
    }

    public long getDuration() {
        if (animation != null) {
            return animation.getDuration();
        }
        if (animator != null) {
            return animator.getDuration();
        }
        return 0;
    }

}
