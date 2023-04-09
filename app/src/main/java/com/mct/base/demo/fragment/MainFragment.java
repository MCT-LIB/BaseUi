package com.mct.base.demo.fragment;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.demo.R;
import com.mct.base.demo.fragment.test.animation.TestAnimationFragment;
import com.mct.base.demo.fragment.test.dialog.TestDialogFragment;
import com.mct.base.demo.fragment.test.transaction.TestTransactionFragment;
import com.mct.base.demo.utils.Utils;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.FragmentTransitionFactory;

public class MainFragment extends BaseFragment {

    int color = Utils.generateBrightColor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.setBackgroundColor(color);
        view.findViewById(R.id.btn_test_backstack).setOnClickListener(v -> extraTransaction().replaceFragmentToStack(
                new TestTransactionFragment(),
                FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE))
        );
        view.findViewById(R.id.btn_test_animation).setOnClickListener(v -> extraTransaction().replaceFragmentToStack(
                new TestAnimationFragment(),
                FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE))
        );
        view.findViewById(R.id.btn_test_dialog).setOnClickListener(v -> extraTransaction().replaceFragmentToStack(
                new TestDialogFragment(),
                FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE))
        );
        return view;
    }

}
