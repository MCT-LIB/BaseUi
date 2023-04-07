package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimBehavior.IN;
import static com.mct.base.ui.transition.annotation.AnimBehavior.NONE;
import static com.mct.base.ui.transition.annotation.AnimBehavior.OUT;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NONE, IN, OUT})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimBehavior {
    // @formatter:off
    int NONE    = 1;
    int IN      = 1 << 1;
    int OUT     = 1 << 2;
    // @formatter:on

}
