package com.mct.base.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mct.base.demo.R;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.FadeFragmentTransition;

import java.util.Random;

public class NumberFragment extends BaseFragment {

    @NonNull
    public static NumberFragment newInstance() {
        return new NumberFragment();
    }

    int color = generateBrightColor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number, container, false);
        view.setBackgroundColor(color);
        TextView tv_number = view.findViewById(R.id.tv_number);
        tv_number.setText(String.valueOf(extraTransaction().getBackStackCount()));
        tv_number.setOnClickListener(v -> extraTransaction().replaceFragmentToStack(newInstance(), new FadeFragmentTransition()));
        return view;
    }

    public static int generateBrightColor() {
        Random random = new Random();
        // Generate a random bright color by setting the red, green, and blue components to values between 128 and 255
        int red = random.nextInt(128) + 128;
        int green = random.nextInt(128) + 128;
        int blue = random.nextInt(128) + 128;
        return Color.rgb(red, green, blue);
    }

}
