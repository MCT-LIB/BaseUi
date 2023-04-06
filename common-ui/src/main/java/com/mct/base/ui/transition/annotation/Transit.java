package com.mct.base.ui.transition.annotation;

import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;
import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;
import static androidx.fragment.app.FragmentTransaction.TRANSIT_NONE;
import static com.mct.base.ui.transition.annotation.Transit.TRANSIT_CUSTOM_ANIMATION;
import static com.mct.base.ui.transition.annotation.Transit.TRANSIT_CUSTOM_ANIMATOR;

import androidx.annotation.IntDef;

import com.mct.base.ui.transition.FragmentTransitionFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TRANSIT_NONE,
        TRANSIT_FRAGMENT_OPEN,
        TRANSIT_FRAGMENT_CLOSE,
        TRANSIT_FRAGMENT_FADE,
        TRANSIT_CUSTOM_ANIMATION,
        TRANSIT_CUSTOM_ANIMATOR,
})
@Retention(RetentionPolicy.SOURCE)
public @interface Transit {

    /**
     * Note:<br/>
     * Can't use this constance for {@link FragmentTransitionFactory#createTransition(int)}<br/>
     * No have any effect if set.
     */
    int TRANSIT_CUSTOM_ANIMATION = 4;

    /**
     * Note:<br/>
     * Can't use this constance for {@link FragmentTransitionFactory#createTransition(int)}<br/>
     * No have any effect if set.
     */
    int TRANSIT_CUSTOM_ANIMATOR = 5;

}
