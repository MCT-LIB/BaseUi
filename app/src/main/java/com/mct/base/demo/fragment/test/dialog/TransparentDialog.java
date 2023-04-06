package com.mct.base.demo.fragment.test.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.mct.base.ui.BaseOverlayDialog;

public class TransparentDialog extends BaseOverlayDialog {

    public TransparentDialog(@NonNull Context context) {
        super(context);
    }

    @NonNull
    @Override
    protected AlertDialog.Builder onCreateDialog(@NonNull LayoutInflater inflater) {
        return new AlertDialog.Builder(mContext)
                .setTitle(getClass().getSimpleName())
                .setMessage("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .setPositiveButton("Ok", (dialog, which) -> dismiss());
    }

    @Override
    protected void onDialogCreated(@NonNull AlertDialog dialog, @NonNull View root) {
    }

    @Nullable
    @Override
    protected DialogOption onCreateDialogOption() {
        return new DialogOption.Builder()
                .setBackgroundColor(Color.TRANSPARENT)
                .build();
    }
}
