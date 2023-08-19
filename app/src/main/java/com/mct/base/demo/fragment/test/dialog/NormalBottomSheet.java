package com.mct.base.demo.fragment.test.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mct.base.demo.R;
import com.mct.base.ui.BaseOverlayDialog;

public class NormalBottomSheet extends BaseOverlayDialog {

    public NormalBottomSheet(@NonNull Context context) {
        super(context);
    }

    @SuppressLint("InflateParams")
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater) {
        return inflater.inflate(R.layout.bts_test, null);
    }

    @Override
    protected AppCompatDialog onCreateDialog(Context context) {
        return new BottomSheetDialog(context);
    }

    @Override
    protected void onDialogCreated(@NonNull AppCompatDialog dialog, View view) {
    }

    @Nullable
    @Override
    protected DialogOption onCreateDialogOption() {
        return new DialogOption.Builder()
                .setBackgroundColor(Color.WHITE)
                .build();
    }

}
