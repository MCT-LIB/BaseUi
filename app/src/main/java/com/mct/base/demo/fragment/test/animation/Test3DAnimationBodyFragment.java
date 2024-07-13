package com.mct.base.demo.fragment.test.animation;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mct.base.demo.R;
import com.mct.base.demo.utils.Utils;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.transition.FragmentTransitionFactory;
import com.mct.base.ui.transition.annotation.AnimDirection;
import com.mct.base.ui.transition.options.AnimOptions;

public class Test3DAnimationBodyFragment extends BaseFragment implements View.OnClickListener {

    private final int color = Utils.generateBrightColor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_3d_animation_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(color);

        TextView tvStyle = view.findViewById(R.id.tv_AnimationStyle);
        tvStyle.setText(Test3DAnimationFragment.sAnimationStyleName);

        view.findViewById(R.id.btn_Left).setOnClickListener(this);
        view.findViewById(R.id.btn_Right).setOnClickListener(this);
        view.findViewById(R.id.btn_Up).setOnClickListener(this);
        view.findViewById(R.id.btn_Down).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        IExtraTransaction extraTransaction = parentExtraTransaction();
        int direction;
        int id = v.getId();
        if (id == R.id.btn_Left) {
            direction = AnimDirection.LEFT;
        } else if (id == R.id.btn_Right) {
            direction = AnimDirection.RIGHT;
        } else if (id == R.id.btn_Up) {
            direction = AnimDirection.UP;
        } else if (id == R.id.btn_Down) {
            direction = AnimDirection.DOWN;
        } else {
            direction = AnimDirection.NONE;
        }
        AnimOptions anim = AnimOptions.animation(Test3DAnimationFragment.sAnimationStyle).direction(direction).build();
        extraTransaction.replaceFragment(
                new Test3DAnimationBodyFragment(),
                FragmentTransitionFactory.createTransition(anim, anim)
        );
    }

}
