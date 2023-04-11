package com.mct.base.ui.swipeback;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mct.base.ui.BaseFragment;

@SuppressWarnings("unused")
public class SwipeBackFragment extends BaseFragment {

    private SwipeBackLayout mSwipeBackLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onFragmentCreate();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view instanceof SwipeBackLayout) {
            setBackground(((SwipeBackLayout) view).getChildAt(0));
        } else {
            setBackground(view);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && mSwipeBackLayout != null) {
            mSwipeBackLayout.hiddenFragment();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSwipeBackLayout.internalCallOnDestroyView();
    }

    public View attachToSwipeBack(View view) {
        mSwipeBackLayout.attachToFragment(this, view);
        return mSwipeBackLayout;
    }

    public void setEdgeLevel(SwipeBackLayout.EdgeLevel edgeLevel) {
        mSwipeBackLayout.setEdgeLevel(edgeLevel);
    }

    public void setEdgeLevel(int widthPixel) {
        mSwipeBackLayout.setEdgeLevel(widthPixel);
    }

    /**
     * Set the offset of the parallax slip.
     */
    public void setParallaxOffset(@FloatRange(from = 0.0f, to = 1.0f) float offset) {
        mSwipeBackLayout.setParallaxOffset(offset);
    }

    public void setEdgeOrientation(@SwipeBackLayout.EdgeOrientation int orientation) {
        mSwipeBackLayout.setEdgeOrientation(orientation);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }

    public void setSwipeBackEnable(boolean enable) {
        mSwipeBackLayout.setEnableGesture(enable);
    }

    private void setBackground(View view) {
        if (view != null && view.getBackground() == null) {
            view.setBackgroundResource(getWindowBackground());
        }
    }

    @SuppressWarnings("resource")
    protected int getWindowBackground() {
        TypedArray a = getActivity().getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();
        return background;
    }

    private void onFragmentCreate() {
        mSwipeBackLayout = new SwipeBackLayout(getContext());
        mSwipeBackLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mSwipeBackLayout.setBackgroundColor(Color.TRANSPARENT);
    }

}