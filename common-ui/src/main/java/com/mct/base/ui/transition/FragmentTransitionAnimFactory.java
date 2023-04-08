package com.mct.base.ui.transition;

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
import android.graphics.Point;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.animation.CubeAnimation;
import com.mct.base.ui.transition.animation.FlipAnimation;
import com.mct.base.ui.transition.animation.MoveAnimation;
import com.mct.base.ui.transition.animation.PushPullAnimation;
import com.mct.base.ui.transition.animation.SidesAnimation;
import com.mct.base.ui.transition.animator.CircularRevealAnimator;
import com.mct.base.ui.transition.animator.FadeAnimator;
import com.mct.base.ui.transition.animator.MoveAnimator;
import com.mct.base.ui.transition.annotation.AnimatorStyle;
import com.mct.base.ui.transition.option.AnimOptions;
import com.mct.base.ui.transition.option.AnimOptionsData;

public class FragmentTransitionAnimFactory {

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> T create(@NonNull AnimOptionsData aod, Class<T> clazz) {
        if (clazz == Animation.class) {
            return (T) createAnimation(aod);
        }
        if (clazz == Animator.class) {
            return (T) createAnimator(aod);
        }
        throw new RuntimeException("Class invalid!");
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> T create(Class<T> clazz) {
        if (clazz == Animation.class) {
            return (T) new Animation() {
            };
        }
        if (clazz == Animator.class) {
            return (T) new AnimatorSet();
        }
        throw new RuntimeException("Class invalid!");
    }

    @NonNull
    private static Animation createAnimation(@NonNull AnimOptionsData aod) {
        AnimOptions options = aod.getOptions();
        int style = options.getAnimStyle();
        int direction = options.getAnimDirection();
        int duration = aod.getDuration();
        boolean enter = aod.isEnter();

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
        return create(Animation.class);
    }

    @NonNull
    private static Animator createAnimator(@NonNull AnimOptionsData aod) {
        AnimOptions options = aod.getOptions();
        int style = options.getAnimStyle();
        int direction = options.getAnimDirection();
        int behavior = options.getAnimBehavior();

        View view = aod.getView();
        int duration = aod.getDuration();
        boolean enter = aod.isEnter();
        Point center = aod.getCircularPosition();

        // @formatter:off
        switch (style) {
            case AnimatorStyle.MOVE:            return MoveAnimator.create(view, direction, enter, duration);
            case AnimatorStyle.FADE:            return FadeAnimator.create(view, behavior, enter,duration);
            case AnimatorStyle.CIRCULAR_REVEAL: return CircularRevealAnimator.create(view, behavior, enter, duration, center.x, center.y);
        }
        // @formatter:on
        return create(Animator.class);
    }

    private FragmentTransitionAnimFactory() {
        //no instance
    }
}
