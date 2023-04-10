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
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.R;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.transition.animation.CubeAnimation;
import com.mct.base.ui.transition.animation.FlipAnimation;
import com.mct.base.ui.transition.animation.MoveAnimation;
import com.mct.base.ui.transition.animation.PushPullAnimation;
import com.mct.base.ui.transition.animation.SidesAnimation;
import com.mct.base.ui.transition.animator.CircularRevealAnimator;
import com.mct.base.ui.transition.animator.FadeAnimator;
import com.mct.base.ui.transition.animator.MoveAnimator;
import com.mct.base.ui.transition.animator.RotateAnimator;
import com.mct.base.ui.transition.annotation.AnimType;
import com.mct.base.ui.transition.annotation.AnimatorStyle;
import com.mct.base.ui.transition.option.AnimExtras;
import com.mct.base.ui.transition.option.AnimOptions;
import com.mct.base.ui.transition.option.AnimOptionsData;

public class FragmentTransitionAnimFactory {

    @NonNull
    public static AnimExtras create(@NonNull AnimOptionsData aod) {
        if (aod.getOptions().getAnimType() == AnimType.ANIMATION) {
            return new AnimExtras(createAnimation(aod));
        }
        if (aod.getOptions().getAnimType() == AnimType.ANIMATOR) {
            return new AnimExtras(createAnimator(aod));
        }
        return create(AnimExtras.class);
    }

    @NonNull
    public static AnimExtras create(Context context, int transit, boolean enter, int nextAnim) {
        if (nextAnim == 0 && transit != 0) {
            nextAnim = transitToAnimResourceId(transit, enter);
        }
        if (nextAnim != 0) {
            try {
                String dir = context.getResources().getResourceTypeName(nextAnim);
                if ("anim".equals(dir)) {
                    Animation animation = AnimationUtils.loadAnimation(context, nextAnim);
                    if (animation != null) {
                        return new AnimExtras(animation);
                    }
                }
                if ("animator".equals(dir)) {
                    Animator animator = AnimatorInflater.loadAnimator(context, nextAnim);
                    if (animator != null) {
                        return new AnimExtras(animator);
                    }
                }
            } catch (Resources.NotFoundException e) {
                // handle resource not found error
            } catch (Exception e) {
                // handle other exceptions
            }
        }
        return create(AnimExtras.class);
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

        View view = aod.getView();
        int duration = aod.getDuration();
        boolean enter = aod.isEnter();
        Point center = aod.getCircularPosition();

        // @formatter:off
        switch (style) {
            case AnimatorStyle.MOVE:            return MoveAnimator.create(view, direction, enter, duration);
            case AnimatorStyle.FADE:            return FadeAnimator.create(view, enter, duration);
            case AnimatorStyle.CIRCULAR_REVEAL: return CircularRevealAnimator.create(view, enter, duration, center.x, center.y);
            case AnimatorStyle.ROTATE:          return RotateAnimator.create(view, direction, enter,duration).fading(enter ? 0f : 1f, enter ? 1f : 0f);
            case AnimatorStyle.ROTATE_UP:       return RotateAnimator.createRotateUp(view, direction, enter,duration).fading(enter ? 0.3f : 1f, enter ? 1f : 0.3f);
            case AnimatorStyle.ROTATE_DOWN:     return RotateAnimator.createRotateDown(view, direction, enter,duration).fading(enter ? 0.3f : 1f, enter ? 1f : 0.3f);
        }
        // @formatter:on
        return create(Animator.class);
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
        if (clazz == AnimExtras.class) {
            return (T) new AnimExtras(new AnimatorSet());
        }
        throw new RuntimeException("Class invalid!");
    }

    @SuppressLint("PrivateResource")
    private static int transitToAnimResourceId(int transit, boolean enter) {
        int animAttr = -1;
        switch (transit) {
            case FragmentTransaction.TRANSIT_FRAGMENT_OPEN:
                animAttr = enter ? R.animator.fragment_open_enter : R.animator.fragment_open_exit;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_CLOSE:
                animAttr = enter ? R.animator.fragment_close_enter : R.animator.fragment_close_exit;
                break;
            case FragmentTransaction.TRANSIT_FRAGMENT_FADE:
                animAttr = enter ? R.animator.fragment_fade_enter : R.animator.fragment_fade_exit;
                break;
        }
        return animAttr;
    }

    private FragmentTransitionAnimFactory() {
        //no instance
    }
}
