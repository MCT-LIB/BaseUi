package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimatorStyle.CIRCULAR;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.NONE;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.SLIDE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NONE, SLIDE, CIRCULAR})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimatorStyle {
    // @formatter:off
    int NONE        = -1 << 31;
    int SLIDE       = -1 << 30;
    int CIRCULAR    = -1 << 29;
    // @formatter:on

}
