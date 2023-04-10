package com.mct.base.ui.transition.option;

import androidx.annotation.NonNull;

import com.mct.base.ui.transition.annotation.AnimDirection;
import com.mct.base.ui.transition.annotation.AnimType;
import com.mct.base.ui.transition.annotation.AnimationStyle;
import com.mct.base.ui.transition.annotation.AnimatorStyle;

public class AnimOptions {

    @NonNull
    public static AnimOptions fromOptionsValue(int value) {
        value = Math.abs(value);
        return new AnimOptions.Builder()
                .animType(AnimOptionsStorage.TYPE.get(value))
                .animStyle(AnimOptionsStorage.STYLE.get(value))
                .animDirection(AnimOptionsStorage.DIRECTION.get(value))
                .build();
    }

    public static int toOptionsValue(@NonNull AnimOptions options) {
        int value = 0;
        value = AnimOptionsStorage.TYPE.set(value, options.animType);
        value = AnimOptionsStorage.STYLE.set(value, options.animStyle);
        value = AnimOptionsStorage.DIRECTION.set(value, options.animDirection);
        return -Math.abs(value);
    }

    private final int animType;
    private final int animStyle;
    private final int animDirection;

    private AnimOptions(@NonNull Builder builder) {
        this.animType = builder.animType;
        this.animStyle = builder.animStyle;
        this.animDirection = builder.animDirection;
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

    public static class Builder {
        private int animType;
        private int animStyle;
        private int animDirection;

        public Builder animType(@AnimType int animType) {
            this.animType = animType;
            return this;
        }

        public Builder animStyle(@AnimationStyle @AnimatorStyle int animStyle) {
            this.animStyle = animStyle;
            return this;
        }

        public Builder animDirection(@AnimDirection int animDirection) {
            this.animDirection = animDirection;
            return this;
        }

        public AnimOptions build() {
            return new AnimOptions(this);
        }
    }

}
