package com.mct.base.ui.transition.options;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimDirection;
import com.mct.base.ui.transition.annotation.AnimType;
import com.mct.base.ui.transition.annotation.AnimationStyle;
import com.mct.base.ui.transition.annotation.AnimatorStyle;

public class AnimOptions {

    ///////////////////////////////////////////////////////////////////////////
    // Converter area
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    public static AnimOptions fromOptionsValue(int value) {
        value = Math.abs(value);
        return new AnimOptions.Builder()
                .type(AnimOptionsStorage.TYPE.get(value))
                .style(AnimOptionsStorage.STYLE.get(value))
                .direction(AnimOptionsStorage.DIRECTION.get(value))
                .overlay(AnimOptionsStorage.OVERLAY.get(value) != 0)
                .build();
    }

    public static int toOptionsValue(@NonNull AnimOptions options) {
        int value = 0;
        value = AnimOptionsStorage.TYPE.set(value, options.animType);
        value = AnimOptionsStorage.STYLE.set(value, options.animStyle);
        value = AnimOptionsStorage.DIRECTION.set(value, options.animDirection);
        value = AnimOptionsStorage.OVERLAY.set(value, options.animOverlay);
        return -Math.abs(value);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Class area
    ///////////////////////////////////////////////////////////////////////////

    private final int animType;
    private final int animStyle;
    private final int animDirection;
    private final int animOverlay;

    private AnimOptions(@NonNull Builder builder) {
        this.animType = builder.animType;
        this.animStyle = builder.animStyle;
        this.animDirection = builder.animDirection;
        this.animOverlay = builder.animOverlay;
    }

    @AnimType
    public int getAnimType() {
        return animType;
    }

    public int getAnimStyle() {
        return animStyle;
    }

    @AnimDirection
    public int getAnimDirection() {
        return animDirection;
    }

    public boolean hasOverlay() {
        return animOverlay != 0;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Builder area
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    public static Builder animation(@AnimationStyle int style) {
        return new Builder().animation().style(style);
    }

    @NonNull
    public static Builder animator(@AnimatorStyle int style) {
        return new Builder().animator().style(style);
    }

    public static class Builder {
        private int animType;
        private int animStyle;
        private int animDirection;
        private int animOverlay;

        private Builder() {
        }

        public Builder type(@AnimType int animType) {
            this.animType = animType;
            return this;
        }

        public Builder style(int animStyle) {
            this.animStyle = animStyle;
            return this;
        }

        public Builder direction(@AnimDirection int animDirection) {
            this.animDirection = animDirection;
            return this;
        }

        public Builder overlay(boolean animOverlay) {
            this.animOverlay = animOverlay ? 1 : 0;
            return this;
        }

        public Builder animation() {
            return this.type(AnimType.ANIMATION);
        }

        public Builder animator() {
            return this.type(AnimType.ANIMATOR);
        }

        public Builder up() {
            return this.direction(AnimDirection.UP);
        }

        public Builder down() {
            return this.direction(AnimDirection.DOWN);
        }

        public Builder left() {
            return this.direction(AnimDirection.LEFT);
        }

        public Builder right() {
            return this.direction(AnimDirection.RIGHT);
        }

        public Builder overlay() {
            return this.overlay(true);
        }

        public AnimOptions build() {
            return new AnimOptions(this);
        }
    }

}
