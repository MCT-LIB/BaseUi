package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimatorStyle.CIRCULAR_REVEAL;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.FADE;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.MOVE;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.NONE;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.ROTATE;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.ROTATE_DOWN;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.ROTATE_UP;
import static com.mct.base.ui.transition.annotation.AnimatorStyle.SCALE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NONE, MOVE, FADE, CIRCULAR_REVEAL, ROTATE, ROTATE_UP, ROTATE_DOWN, SCALE})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimatorStyle {
    // @formatter:off
    int NONE            = 0;
    int MOVE            = 1;
    int FADE            = 2; // note: no need direction
    int CIRCULAR_REVEAL = 3; // note: no need direction
    int ROTATE          = 4; // note: no need direction
    int ROTATE_UP       = 5; // note: direction only in [ LEFT || RIGHT ]
    int ROTATE_DOWN     = 6; // note: direction only in [ LEFT || RIGHT ]
    int SCALE           = 7; // note: direction only in [ UP   || DOWN  ]
    // @formatter:on

}
