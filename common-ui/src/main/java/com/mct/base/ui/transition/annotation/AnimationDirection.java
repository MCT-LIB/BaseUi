package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimationDirection.DOWN;
import static com.mct.base.ui.transition.annotation.AnimationDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimationDirection.NONE;
import static com.mct.base.ui.transition.annotation.AnimationDirection.RIGHT;
import static com.mct.base.ui.transition.annotation.AnimationDirection.UP;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NONE, UP, DOWN, LEFT, RIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationDirection {
    // @formatter:off
    int NONE    = 1;
    int UP      = 1 << 1;
    int DOWN    = 1 << 2;
    int LEFT    = 1 << 3;
    int RIGHT   = 1 << 4;
    // @formatter:on

}
