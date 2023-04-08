package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimDirection.DOWN;
import static com.mct.base.ui.transition.annotation.AnimDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimDirection.NONE;
import static com.mct.base.ui.transition.annotation.AnimDirection.RIGHT;
import static com.mct.base.ui.transition.annotation.AnimDirection.UP;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NONE, UP, DOWN, LEFT, RIGHT})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimDirection {
    // @formatter:off
    int NONE    = 0;
    int UP      = 1;
    int DOWN    = 2;
    int LEFT    = 3;
    int RIGHT   = 4;
    // @formatter:on

}
