package com.mct.base.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.demo.R;
import com.mct.base.demo.fragment.test.animation.TestAnimFragment;
import com.mct.base.demo.fragment.test.dialog.TestDialogFragment;
import com.mct.base.demo.fragment.test.transaction.TestTransactionFragment;
import com.mct.base.demo.utils.Utils;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.FragmentTransitionFactory;
import com.mct.base.ui.transition.annotation.AnimatorStyle;
import com.mct.base.ui.transition.options.AnimOptions;

public class MainFragment extends BaseFragment {

    int color = Utils.generateBrightColor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        view.setBackgroundColor(color);
        view.findViewById(R.id.btn_test_backstack).setOnClickListener(v -> extraTransaction().replaceFragmentToStack(
                new TestTransactionFragment(),
                //FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE))
                FragmentTransitionFactory.createTransition(
                        AnimOptions.animator(AnimatorStyle.MOVE).left().build(),
                        AnimOptions.animator(AnimatorStyle.NONE).build(),
                        AnimOptions.animator(AnimatorStyle.NONE).build(),
                        AnimOptions.animator(AnimatorStyle.MOVE).right().build()
                )
        ));
        view.findViewById(R.id.btn_test_animation).setOnClickListener(v -> extraTransaction().replaceFragmentToStack(
                new TestAnimFragment(),
                FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ));
        view.findViewById(R.id.btn_test_dialog).setOnClickListener(v -> extraTransaction().replaceFragmentToStack(
                new TestDialogFragment(),
                FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        ));
        return view;
    }

}
