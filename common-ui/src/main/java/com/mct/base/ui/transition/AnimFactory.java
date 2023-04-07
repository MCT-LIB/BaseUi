package com.mct.base.ui.transition;

import static com.mct.base.ui.transition.annotation.AnimBehavior.IN;
import static com.mct.base.ui.transition.annotation.AnimBehavior.OUT;
import static com.mct.base.ui.transition.annotation.AnimDirection.DOWN;
import static com.mct.base.ui.transition.annotation.AnimDirection.LEFT;
import static com.mct.base.ui.transition.annotation.AnimDirection.RIGHT;
import static com.mct.base.ui.transition.annotation.AnimDirection.UP;
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
import static com.mct.base.ui.transition.annotation.AnimationStyle.PUSH_MOVE;
import static com.mct.base.ui.transition.annotation.AnimationStyle.PUSH_PULL;
import static com.mct.base.ui.transition.annotation.AnimationStyle.SIDES;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.animation.CubeAnimation;
import com.mct.base.ui.transition.animation.FlipAnimation;
import com.mct.base.ui.transition.animation.MoveAnimation;
import com.mct.base.ui.transition.animation.PushPullAnimation;
import com.mct.base.ui.transition.animation.SidesAnimation;
import com.mct.base.ui.transition.animator.CircularAnimator;
import com.mct.base.ui.transition.animator.SlideAnimator;
import com.mct.base.ui.transition.annotation.AnimBehavior;
import com.mct.base.ui.transition.annotation.AnimDirection;
import com.mct.base.ui.transition.annotation.AnimatorStyle;

public abstract class AnimFactory {

    @NonNull
    public static Animation createAnimation(int nextAnim, boolean enter, int duration) {
        int style, direction;
        direction = getDirection(nextAnim);
        style = getStyle(nextAnim, direction);
        // @formatter:off
        switch (style) {
            case MOVE:      return MoveAnimation.create(direction, enter, duration);
            case CUBE:      return CubeAnimation.create(direction, enter, duration);
            case FLIP:      return FlipAnimation.create(direction, enter, duration);
            case PUSH_PULL: return PushPullAnimation.create(direction, enter, duration);
            case SIDES:     return SidesAnimation.create(direction, enter, duration);
            case CUBE_MOVE: return enter
                                ? MoveAnimation.create(direction, enter, duration).fading(0.3f, 1.0f)
                                : CubeAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
            case MOVE_CUBE: return enter
                                ? CubeAnimation.create(direction, enter, duration).fading(0.3f, 1.0f)
                                : MoveAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
            case PUSH_MOVE: return enter
                                ? MoveAnimation.create(direction, enter, duration)
                                : PushPullAnimation.create(direction, enter, duration);
            case MOVE_PULL: return enter
                                ? PushPullAnimation.create(direction, enter, duration)
                                : MoveAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
            case FLIP_MOVE: return enter
                                ? MoveAnimation.create(direction, enter, duration)
                                : FlipAnimation.create(direction, enter, duration);
            case MOVE_FLIP: return enter
                                ? FlipAnimation.create(direction, enter, duration)
                                : MoveAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
            case FLIP_CUBE: return enter
                                ? CubeAnimation.create(direction, enter, duration)
                                : FlipAnimation.create(direction, enter, duration);
            case CUBE_FLIP: return enter
                                ? FlipAnimation.create(direction, enter, duration)
                                : CubeAnimation.create(direction, enter, duration).fading(1.0f, 0.3f);
        }
        // @formatter:on
        return noneAnim(Animation.class);
    }

    @NonNull
    public static Animator createAnimator(View view, int nextAnim, boolean enter, int duration, Point center) {
        int style, direction, behavior;
        direction = getDirection(nextAnim);
        behavior = getBehavior(nextAnim);

        style = getStyle(nextAnim, direction);

        Log.e("ddd", "createAnimator: " + enter + " " + center);
        switch (style) {
            case AnimatorStyle.SLIDE:
                return SlideAnimator.create(view, direction, enter, duration);
            case AnimatorStyle.CIRCULAR:
                return CircularAnimator.create(view, behavior, enter, duration, center.x, center.y);
        }
        return noneAnim(Animator.class);
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> T noneAnim(Class<T> clazz) {
        if (clazz == Animation.class) {
            return (T) new Animation() {
            };
        }
        if (clazz == Animator.class) {
            return (T) new AnimatorSet();
        }
        throw new RuntimeException("Class invalid!");
    }

    private static int getStyle(int nextAnim, int direction) {
        return nextAnim & ~direction;
    }

    @AnimDirection
    private static int getDirection(int nextAnim) {
        int[] directions = new int[]{UP, DOWN, LEFT, RIGHT};
        for (int direction : directions) {
            if ((nextAnim & direction) == direction) {
                return direction;
            }
        }
        return AnimDirection.NONE;
    }

    @AnimBehavior
    private static int getBehavior(int nextAnim) {
        int[] behaviors = new int[]{IN, OUT};
        for (int behavior : behaviors) {
            if ((nextAnim & behavior) == behavior) {
                return behavior;
            }
        }
        return AnimBehavior.NONE;
    }

    private AnimFactory() {
        //no instance
    }
}
