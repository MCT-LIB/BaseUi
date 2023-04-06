package com.mct.base.demo.fragment.test;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mct.base.demo.R;
import com.mct.base.demo.utils.Utils;
import com.mct.base.ui.BaseFragment;

public class TestBackstackFragment extends BaseFragment {

    int color = Utils.generateBrightColor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_backstack, container, false);
        view.setBackgroundColor(color);
        return view;
    }
}
