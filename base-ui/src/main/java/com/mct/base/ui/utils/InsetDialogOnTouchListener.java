package com.mct.base.ui.utils;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;

public class InsetDialogOnTouchListener implements View.OnTouchListener {

    @NonNull
    private final Dialog dialog;
    private final int leftInset;
    private final int topInset;
    private final int prePieSlop;

    public InsetDialogOnTouchListener(@NonNull Dialog dialog, @NonNull Rect insets) {
        this.dialog = dialog;
        this.leftInset = insets.left;
        this.topInset = insets.top;
        this.prePieSlop = ViewConfiguration.get(dialog.getContext()).getScaledWindowTouchSlop();
    }

    @Override
    public boolean onTouch(@NonNull View view, @NonNull MotionEvent event) {
        View insetView = view.findViewById(android.R.id.content);

        int insetLeft = leftInset + insetView.getLeft();
        int insetRight = insetLeft + insetView.getWidth();
        int insetTop = topInset + insetView.getTop();
        int insetBottom = insetTop + insetView.getHeight();

        RectF dialogWindow = new RectF(insetLeft, insetTop, insetRight, insetBottom);
        if (dialogWindow.contains(event.getX(), event.getY())) {
            return false;
        }
        MotionEvent outsideEvent = MotionEvent.obtain(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            outsideEvent.setAction(MotionEvent.ACTION_OUTSIDE);
        }
        // Window.shouldCloseOnTouch does not respect MotionEvent.ACTION_OUTSIDE until Pie, so we fix
        // the coordinates outside the view and use MotionEvent.ACTION_DOWN
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            outsideEvent.setAction(MotionEvent.ACTION_DOWN);
            outsideEvent.setLocation(-prePieSlop - 1, -prePieSlop - 1);
        }
        view.performClick();
        return dialog.onTouchEvent(outsideEvent);
    }
}
