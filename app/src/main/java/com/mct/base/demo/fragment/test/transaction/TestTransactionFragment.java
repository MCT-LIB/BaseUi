package com.mct.base.demo.fragment.test.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.mct.base.demo.R;
import com.mct.base.ui.swipeback.SwipeBackFragment;
import com.mct.base.ui.swipeback.SwipeBackLayout;

public class TestTransactionFragment extends SwipeBackFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_transaction, container, false);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> extraTransaction().popFragment());

        childExtraTransaction().replaceFragment(new TestTransactionBodyFragment());

        setEdgeLevel(SwipeBackLayout.EdgeLevel.MAX);
        setParallaxOffset(0.5f);
    }

    @Override
    public int getContainerId() {
        return R.id.test_container;
    }

}
