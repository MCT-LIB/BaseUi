package com.mct.base.ui.transition.options;

import android.animation.Animator;
import android.view.animation.Animation;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AnimExtras extends SimpleListenerAdapter {

    public final Animation animation;
    public final Animator animator;
    private int transit;
    private boolean enter;
    private int nextAnim;
    private List<AnimExtrasListener> mListeners;

    public AnimExtras(Animation animation) {
        this.animation = animation;
        this.animator = null;
        if (animation == null) {
            throw new IllegalStateException("Animation cannot be null");
        }
        animation.setAnimationListener(this);
    }

    public AnimExtras(Animator animator) {
        this.animation = null;
        this.animator = animator;
        if (animator == null) {
            throw new IllegalStateException("Animator cannot be null");
        }
        animator.addListener(this);
    }

    public long getDuration() {
        if (animation != null) {
            return animation.getDuration();
        }
        if (animator != null) {
            return animator.getDuration();
        }
        return 0;
    }

    public void setAnimInfo(int transit, boolean enter, int nextAnim) {
        this.transit = transit;
        this.enter = enter;
        this.nextAnim = nextAnim;
    }

    public int getTransit() {
        return transit;
    }

    public boolean isEnter() {
        return enter;
    }

    public int getNextAnim() {
        return nextAnim;
    }

    public void addAnimationListener(AnimExtrasListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void removeAnimationListener(AnimExtrasListener listener) {
        if (mListeners == null) {
            return;
        }
        mListeners.remove(listener);
        if (mListeners.size() == 0) {
            mListeners = null;
        }
    }

    public void removeAllListeners() {
        if (mListeners != null) {
            mListeners.clear();
            mListeners = null;
        }
    }

    @Override
    protected void onAnimationStart() {
        List<AnimExtrasListener> listeners = mListeners;
        if (listeners != null) for (AnimExtrasListener listener : listeners) {
            if (listener != null) {
                listener.onAnimationStart(AnimExtras.this);
            }
        }
    }

    @Override
    protected void onAnimationEnd() {
        List<AnimExtrasListener> listeners = mListeners;
        if (listeners != null) for (AnimExtrasListener listener : listeners) {
            if (listener != null) {
                listener.onAnimationEnd(AnimExtras.this);
            }
        }
    }

    public interface AnimExtrasListener {
        void onAnimationStart(@NonNull AnimExtras animExtras);

        void onAnimationEnd(@NonNull AnimExtras animExtras);
    }
}
