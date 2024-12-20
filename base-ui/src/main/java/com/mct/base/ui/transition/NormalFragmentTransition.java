package com.mct.base.ui.transition;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.transition.annotation.AnimatorStyle;
import com.mct.base.ui.transition.options.AnimOptions;

public class NormalFragmentTransition implements FragmentTransition {

    private final int enter;
    private final int exit;
    private final int popEnter;
    private final int popExit;

    private NormalFragmentTransition(@NonNull Builder builder) {
        this.enter = builder.enter;
        this.exit = builder.exit;
        this.popEnter = builder.popEnter;
        this.popExit = builder.popExit;
    }

    @Override
    public void applyTransition(@NonNull FragmentTransaction transaction) {
        transaction.setCustomAnimations(enter, exit, popEnter, popExit);
    }

    @Override
    public boolean couldPopImmediate() {
        AnimOptions options = AnimOptions.fromOptionsValue(enter);
        // do not pop immediate when style is CIRCULAR_REVEAL
        if (options.getAnimStyle() == AnimatorStyle.CIRCULAR_REVEAL) {
            return false;
        }
        return FragmentTransition.super.couldPopImmediate();
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int enter;
        private int exit;
        private int popEnter;
        private int popExit;

        private Builder() {
        }

        public Builder enter(int enter) {
            this.enter = enter;
            return this;
        }

        public Builder enter(AnimOptions options) {
            this.enter = options == null ? 0 : AnimOptions.toOptionsValue(options);
            return this;
        }

        public Builder exit(int exit) {
            this.exit = exit;
            return this;
        }

        public Builder exit(AnimOptions options) {
            this.exit = options == null ? 0 : AnimOptions.toOptionsValue(options);
            return this;
        }

        public Builder popEnter(int popEnter) {
            this.popEnter = popEnter;
            return this;
        }

        public Builder popEnter(AnimOptions options) {
            this.popEnter = options == null ? 0 : AnimOptions.toOptionsValue(options);
            return this;
        }

        public Builder popExit(int popExit) {
            this.popExit = popExit;
            return this;
        }

        public Builder popExit(AnimOptions options) {
            this.popExit = options == null ? 0 : AnimOptions.toOptionsValue(options);
            return this;
        }

        public NormalFragmentTransition build() {
            return new NormalFragmentTransition(this);
        }

    }
}
