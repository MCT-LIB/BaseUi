package com.mct.base.ui.transition.option;

import android.graphics.Point;
import android.view.View;

public class AnimOptionsData {

    private AnimOptions options;
    private View view;
    private int duration;
    private boolean enter;
    private Point circularPosition;

    public AnimOptions getOptions() {
        return options;
    }

    public void setOptions(AnimOptions options) {
        this.options = options;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public Point getCircularPosition() {
        return circularPosition;
    }

    public void setCircularPosition(Point circularPosition) {
        this.circularPosition = circularPosition;
    }
}
