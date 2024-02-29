package com.mct.base.demo;

import android.os.Bundle;

import com.mct.base.demo.fragment.MainFragment;
import com.mct.base.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extraTransaction().addFragment(new MainFragment());
    }

}