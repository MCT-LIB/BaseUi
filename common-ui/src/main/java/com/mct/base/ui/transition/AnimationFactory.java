package com.mct.base.ui.transition;

import static com.mct.base.ui.transition.annotation.AnimationDirection.DOWN;
import static com.mct.base.ui.transition.annotation.AnimationDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimationDirection.NONE;
import static com.mct.base.ui.transition.annotation.AnimationDirection.RIGHT;
import static com.mct.base.ui.transition.annotation.AnimationDirection.UP;
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
import static com.mct.base.ui.transition.annotation.AnimationStyle.PUSHMOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.PUSHPULL;
import static com.mct.base.ui.transition.annotation.AnimationStyle.SIDES;

import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.animation.CubeAnimation;
import com.mct.base.ui.transition.animation.FlipAnimation;
import com.mct.base.ui.transition.animation.MoveAnimation;
import com.mct.base.ui.transition.animation.PushPullAnimation;
import com.mct.base.ui.transition.animation.SidesAnimation;
import com.mct.base.ui.transition.annotation.AnimationDirection;

public abstract class AnimationFactory {

    private static final Animation NONE_ANIMATION = new Animation() {
    };

    @NonNull
    public static Animation createAnimation(int nextAnim, boolean enter, int duration) {
        int style, direction;
        direction = getDirection(nextAnim);
        style = nextAnim & ~direction;
        // @formatter:off
        switch (style) {
            case NONE:      return NONE_ANIMATION;
            case MOVE:      return MoveAnimation.create(direction, enter, duration);
            case CUBE:      return CubeAnimation.create(direction, enter, duration);
            case FLIP:      return FlipAnimation.create(direction, enter, duration);
            case PUSHPULL:  return PushPullAnimation.create(direction, enter, duration);
            case SIDES:     return SidesAnimation.create(direction, enter, duration);
            case CUBEMOVE:  return enter
                                ? MoveAnimation.create(direction, enter, duration).fading(0.3f, 1.0f)
                                : CubeAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
            case MOVECUBE:  return enter
                                ? CubeAnimation.create(direction, enter, duration).fading(0.3f, 1.0f)
                                : MoveAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
            case PUSHMOVE:  return enter
                                ? MoveAnimation.create(direction, enter, duration)
                                : PushPullAnimation.create(direction, enter, duration);
            case MOVEPULL:  return enter
                                ? PushPullAnimation.create(direction, enter, duration)
                                : MoveAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
            case FLIPMOVE:  return enter
                                ? MoveAnimation.create(direction, enter, duration)
                                : FlipAnimation.create(direction, enter, duration);
            case MOVEFLIP:  return enter
                                ? FlipAnimation.create(direction, enter, duration)
                                : MoveAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
            case FLIPCUBE:  return enter
                                ? CubeAnimation.create(direction, enter, duration)
                                : FlipAnimation.create(direction, enter, duration);
            case CUBEFLIP:  return enter
                                ? FlipAnimation.create(direction, enter, duration)
                                : CubeAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
        }
        // @formatter:on
        return NONE_ANIMATION;
    }

    @AnimationDirection
    private static int getDirection(int nextAnim) {
        int[] directions = new int[]{UP, DOWN, LEFT, RIGHT};
        for (int direction : directions) {
            if ((nextAnim & direction) == direction) {
                return direction;
            }
        }
        return NONE;
    }

    private AnimationFactory() {
        //no instance
    }
}
