package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimatorStyle.CIRCULAR_REVEAL;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.FADE;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.MOVE;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.NONE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NONE, MOVE, FADE, CIRCULAR_REVEAL})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimatorStyle {
    // @formatter:off
    int NONE            = 0;
    int MOVE            = 1;
    int FADE            = 2;
    int CIRCULAR_REVEAL = 3;
    // @formatter:on

}
