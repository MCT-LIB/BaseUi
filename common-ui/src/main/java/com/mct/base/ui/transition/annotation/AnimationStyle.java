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

@IntDef({NONE, MOVE, CUBE, FLIP, PUSH_PULL, SIDES, CUBE_MOVE, MOVE_CUBE, PUSH_MOVE, MOVE_PULL, FLIP_MOVE, MOVE_FLIP, FLIP_CUBE, CUBE_FLIP})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationStyle {
    // @formatter:off
    int NONE        = 0;
    int MOVE        = 1;
    int CUBE        = 2;
    int FLIP        = 3;
    int PUSH_PULL   = 4;
    int SIDES       = 5;
    int CUBE_MOVE   = 6;
    int MOVE_CUBE   = 7;
    int PUSH_MOVE   = 8;
    int MOVE_PULL   = 9;
    int FLIP_MOVE   = 10;
    int MOVE_FLIP   = 11;
    int FLIP_CUBE   = 12;
    int CUBE_FLIP   = 13;
    // @formatter:on

}
