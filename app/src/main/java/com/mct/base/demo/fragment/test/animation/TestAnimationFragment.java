package com.mct.base.demo.fragment.test.animation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.mct.base.demo.R;
import com.mct.base.ui.BaseFragment;
import com.mct.base.ui.transition.annotation.AnimationStyle;

public class TestAnimationFragment extends BaseFragment {

    @AnimationStyle
    static int sAnimationStyle = AnimationStyle.NONE;
    static String sAnimationStyleName = "NONE";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_animation, container, false);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> extraTransaction().popFragment());
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.style_NONE:
                    sAnimationStyle = AnimationStyle.NONE;
                    sAnimationStyleName = "NONE";
                    break;
                case R.id.style_MOVE:
                    sAnimationStyle = AnimationStyle.MOVE;
                    sAnimationStyleName = "MOVE";
                    break;
                case R.id.style_CUBE:
                    sAnimationStyle = AnimationStyle.CUBE;
                    sAnimationStyleName = "CUBE";
                    break;
                case R.id.style_FLIP:
                    sAnimationStyle = AnimationStyle.FLIP;
                    sAnimationStyleName = "FLIP";
                    break;
                case R.id.style_PUSHPULL:
                    sAnimationStyle = AnimationStyle.PUSH_PULL;
                    sAnimationStyleName = "PUSHPULL";
                    break;
                case R.id.style_SIDES:
                    sAnimationStyle = AnimationStyle.SIDES;
                    sAnimationStyleName = "SIDES";
                    break;
                case R.id.style_CUBEMOVE:
                    sAnimationStyle = AnimationStyle.CUBE_MOVE;
                    sAnimationStyleName = "CUBEMOVE";
                    break;
                case R.id.style_MOVECUBE:
                    sAnimationStyle = AnimationStyle.MOVE_CUBE;
                    sAnimationStyleName = "MOVECUBE";
                    break;
                case R.id.style_PUSHMOVE:
                    sAnimationStyle = AnimationStyle.PUSH_MOVE;
                    sAnimationStyleName = "PUSHMOVE";
                    break;
                case R.id.style_MOVEPULL:
                    sAnimationStyle = AnimationStyle.MOVE_PULL;
                    sAnimationStyleName = "MOVEPULL";
                    break;
                case R.id.style_FLIPMOVE:
                    sAnimationStyle = AnimationStyle.FLIP_MOVE;
                    sAnimationStyleName = "FLIPMOVE";
                    break;
                case R.id.style_MOVEFLIP:
                    sAnimationStyle = AnimationStyle.MOVE_FLIP;
                    sAnimationStyleName = "MOVEFLIP";
                    break;
                case R.id.style_FLIPCUBE:
                    sAnimationStyle = AnimationStyle.FLIP_CUBE;
                    sAnimationStyleName = "FLIPCUBE";
                    break;
                case R.id.style_CUBEFLIP:
                    sAnimationStyle = AnimationStyle.CUBE_FLIP;
                    sAnimationStyleName = "CUBEFLIP";
                    break;
            }
            childExtraTransaction().replaceFragment(new TestAnimationBodyFragment());
            return true;
        });
        childExtraTransaction().replaceFragment(new TestAnimationBodyFragment());
    }

    @Override
    public int getContainerId() {
        return R.id.test_container;
    }

}
