package com.mct.base.demo;

import android.os.Bundle;
import android.view.Window;

import com.mct.base.demo.fragment.NumberFragment;
import com.mct.base.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extraTransaction().replaceFragment(NumberFragment.newInstance());
    }

    @Override
    protected int getContainerId() {
        return Window.ID_ANDROID_CONTENT;
    }

    @Override
    protected boolean showToastOnBackPressed() {
        return false;
    }
}