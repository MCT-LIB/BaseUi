package com.mct.base.demo.fragment.test.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.mct.base.demo.R;
import com.mct.base.demo.utils.Utils;
import com.mct.base.ui.BaseFragment;

public class TestDialogFragment extends BaseFragment implements View.OnClickListener {

    private final int color = Utils.generateBrightColor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(color);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> extraTransaction().popFragment());

        view.findViewById(R.id.btn_dialog_Normal).setOnClickListener(this);
        view.findViewById(R.id.btn_dialog_Rounded).setOnClickListener(this);
        view.findViewById(R.id.btn_dialog_Transparent).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_Normal:
                new NormalDialog(requireContext()).create(null);
                break;
            case R.id.btn_dialog_Rounded:
                new RoundedCornerDialog(requireContext()).create(null);
                break;
            case R.id.btn_dialog_Transparent:
                new TransparentDialog(requireContext()).create(null);
                break;
        }
    }
}
