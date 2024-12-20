package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimType.ANIMATION;
import static com.mct.base.ui.transition.annotation.AnimType.ANIMATOR;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ANIMATION, ANIMATOR})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimType {
    // @formatter:off
    int ANIMATION   = 0;
    int ANIMATOR    = 1;
    // @formatter:on

}
