package com.mct.base.demo.fragment.test.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mct.base.demo.R;
import com.mct.base.ui.BaseOverlayDialog;
import com.mct.base.ui.utils.ScreenUtils;

public class RoundedCornerBottomSheet extends BaseOverlayDialog {

    public RoundedCornerBottomSheet(@NonNull Context context) {
        super(context);
    }

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
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.WHITE);
        drawable.setCornerRadius(ScreenUtils.dp2px(16f));
        view.setBackground(drawable);
    }

    @Nullable
    @Override
    protected DialogOption onCreateDialogOption() {
        return null;
    }

}
