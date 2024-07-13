package com.mct.base.demo.fragment.test.animation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.demo.R;
import com.mct.base.demo.utils.Utils;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.FragmentTransitionFactory;

public class TestTransitBodyFragment extends BaseFragment implements View.OnClickListener {

    int color = Utils.generateBrightColor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_transit_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(color);

        view.findViewById(R.id.btn_transitOpen).setOnClickListener(this);
        view.findViewById(R.id.btn_transitClose).setOnClickListener(this);
        view.findViewById(R.id.btn_transitFade).setOnClickListener(this);
    }

    @Override
    public int getContainerId() {
        return R.id.test_container;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.btn_transitOpen) {
            parentExtraTransaction().replaceFragment(
                    new TestTransitBodyFragment(),
                    FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            );
        } else if (id == R.id.btn_transitClose) {
            parentExtraTransaction().replaceFragment(
                    new TestTransitBodyFragment(),
                    FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            );
        } else if (id == R.id.btn_transitFade) {
            parentExtraTransaction().replaceFragment(
                    new TestTransitBodyFragment(),
                    FragmentTransitionFactory.createTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            );
        }
    }
}
