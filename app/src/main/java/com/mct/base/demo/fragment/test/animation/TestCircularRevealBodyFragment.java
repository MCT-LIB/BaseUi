package com.mct.base.demo.fragment.test.animation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
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
import com.mct.base.ui.transition.FragmentTransitionFactory;

public class TestCircularRevealBodyFragment extends BaseFragment implements View.OnClickListener {
    public static final String KEY_ARGS_CIRCULAR_POSITION = "circularPosition";

    @NonNull
    public static TestCircularRevealBodyFragment newInstance(Point circularPosition) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_ARGS_CIRCULAR_POSITION, circularPosition);
        TestCircularRevealBodyFragment fragment = new TestCircularRevealBodyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private Point point;
    private final int color = Utils.generateBrightColor();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            point = bundle.getParcelable(KEY_ARGS_CIRCULAR_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_circular_reveal_body, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(color);

        TextView tvBackstackCount = view.findViewById(R.id.tv_backstack_count);
        tvBackstackCount.setText(String.valueOf(parentExtraTransaction().getBackStackCount()));

        view.findViewById(R.id.btn_top_left).setOnClickListener(this);
        view.findViewById(R.id.btn_top_center).setOnClickListener(this);
        view.findViewById(R.id.btn_top_right).setOnClickListener(this);
        view.findViewById(R.id.btn_center_left).setOnClickListener(this);
        view.findViewById(R.id.btn_center).setOnClickListener(this);
        view.findViewById(R.id.btn_center_right).setOnClickListener(this);
        view.findViewById(R.id.btn_bottom_left).setOnClickListener(this);
        view.findViewById(R.id.btn_bottom_center).setOnClickListener(this);
        view.findViewById(R.id.btn_bottom_right).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        int x = (int) (v.getX() + v.getWidth() / 2);
        int y = (int) (v.getY() + v.getHeight() / 2);
        Point point = new Point(x, y);
        parentExtraTransaction().replaceFragmentToStack(
                TestCircularRevealBodyFragment.newInstance(point),
                FragmentTransitionFactory.createCircularAnimator()
        );
    }

    @Override
    public int getContainerId() {
        return R.id.test_container;
    }

    @NonNull
    @Override
    protected Point onRequestCircularPosition() {
        if (point != null) {
            return point;
        }
        return super.onRequestCircularPosition();
    }
}
