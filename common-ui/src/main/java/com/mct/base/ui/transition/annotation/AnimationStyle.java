package com.mct.base.ui.transition.annotation;

import static com.mct.base.ui.transition.annotation.AnimationStyle.CUBE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.CUBEFLIP;
import static com.mct.base.ui.transition.annotation.AnimationStyle.CUBEMOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.FLIP;
import static com.mct.base.ui.transition.annotation.AnimationStyle.FLIPCUBE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.FLIPMOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.MOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.MOVECUBE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.MOVEFLIP;
import static com.mct.base.ui.transition.annotation.AnimationStyle.MOVEPULL;
import static com.mct.base.ui.transition.annotation.AnimationStyle.NONE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.PUSHMOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.PUSHPULL;
import static com.mct.base.ui.transition.annotation.AnimationStyle.SIDES;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Note: new AnimationStyle define value will < -1 << 4 {@link AnimationDirection#RIGHT}
 */
@IntDef({NONE, MOVE, CUBE, FLIP, PUSHPULL, SIDES, CUBEMOVE, MOVECUBE, PUSHMOVE, MOVEPULL, FLIPMOVE, MOVEFLIP, FLIPCUBE, CUBEFLIP})
@Retention(RetentionPolicy.SOURCE)
public @interface AnimationStyle {
    // @formatter:off
    int NONE        = -1 << 31;
    int MOVE        = -1 << 30;
    int CUBE        = -1 << 29;
    int FLIP        = -1 << 28;
    int PUSHPULL    = -1 << 27;
    int SIDES       = -1 << 26;
    int CUBEMOVE    = -1 << 25;
    int MOVECUBE    = -1 << 24;
    int PUSHMOVE    = -1 << 23;
    int MOVEPULL    = -1 << 22;
    int FLIPMOVE    = -1 << 21;
    int MOVEFLIP    = -1 << 20;
    int FLIPCUBE    = -1 << 19;
    int CUBEFLIP    = -1 << 18;
    // @formatter:on

}
