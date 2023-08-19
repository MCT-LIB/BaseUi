package com.mct.base.demo.fragment.test.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;

import com.mct.base.ui.BaseOverlayDialog;

public class NormalDialog extends BaseOverlayDialog {

    public NormalDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater) {
        return null;
    }

    @Override
    protected AppCompatDialog onCreateDialog(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle(getClass().getSimpleName())
                .setMessage("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
                .setPositiveButton("Ok", (dialog, which) -> dismiss())
                .create();
    }

    @Override
    protected void onDialogCreated(@NonNull AppCompatDialog dialog, View view) {
    }

    @Nullable
    @Override
    protected DialogOption onCreateDialogOption() {
        return null;
    }
}
