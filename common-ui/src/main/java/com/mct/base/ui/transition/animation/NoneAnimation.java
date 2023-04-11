package com.mct.base.ui.transition.animation;

import androidx.annotation.NonNull;

public class NoneAnimation extends ViewPropertyAnimation {

    @NonNull
    public static NoneAnimation create(int duration) {
        return new NoneAnimation(duration);
    }

    public NoneAnimation(int duration) {
        fading(1, 1);
        setDuration(duration);
    }

}
