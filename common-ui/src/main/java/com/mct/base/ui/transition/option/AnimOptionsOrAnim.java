package com.mct.base.ui.transition.option;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;

public class AnimOptionsOrAnim {

    @AnimRes
    @AnimatorRes
    public final Integer anim;
    public final AnimOptions options;

    public AnimOptionsOrAnim(@AnimRes @AnimatorRes int anim) {
        this.anim = anim;
        this.options = null;
    }

    public AnimOptionsOrAnim(AnimOptions options) {
        this.anim = null;
        this.options = options;
    }

    public int getValue() {
        if (anim != null) {
            return anim;
        }
        if (options != null) {
            return AnimOptions.toOptionsValue(options);
        }
        return 0;
    }
}
