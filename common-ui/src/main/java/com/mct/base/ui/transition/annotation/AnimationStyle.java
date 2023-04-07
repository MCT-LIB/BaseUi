package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimationStyle.CUBE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.CUBE_FLIP;
import static com.mct.base.ui.transition.annotation.AnimationStyle.CUBE_MOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.FLIP;
import static com.mct.base.ui.transition.annotation.AnimationStyle.FLIP_CUBE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.FLIP_MOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.MOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.MOVE_CUBE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.MOVE_FLIP;
import static com.mct.base.ui.transition.annotation.AnimationStyle.MOVE_PULL;
import static com.mct.base.ui.transition.annotation.AnimationStyle.NONE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.PUSH_MOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.PUSH_PULL;
import static com.mct.base.ui.transition.annotation.AnimationStyle.SIDES;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Note: new AnimationStyle define value will < -1 << 4 {@link AnimDirection#RIGHT}
 */
@IntDef({NONE, MOVE, CUBE, FLIP, PUSH_PULL, SIDES, CUBE_MOVE, MOVE_CUBE, PUSH_MOVE, MOVE_PULL, FLIP_MOVE, MOVE_FLIP, FLIP_CUBE, CUBE_FLIP})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationStyle {
    // @formatter:off
    int NONE        = -1 << 31;
    int MOVE        = -1 << 30;
    int CUBE        = -1 << 29;
    int FLIP        = -1 << 28;
    int PUSH_PULL   = -1 << 27;
    int SIDES       = -1 << 26;
    int CUBE_MOVE   = -1 << 25;
    int MOVE_CUBE   = -1 << 24;
    int PUSH_MOVE   = -1 << 23;
    int MOVE_PULL   = -1 << 22;
    int FLIP_MOVE   = -1 << 21;
    int MOVE_FLIP   = -1 << 20;
    int FLIP_CUBE   = -1 << 19;
    int CUBE_FLIP   = -1 << 18;
    // @formatter:on

}
