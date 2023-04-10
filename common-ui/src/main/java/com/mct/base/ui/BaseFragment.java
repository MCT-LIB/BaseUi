package com.mct.base.ui;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mct.base.ui.core.IBaseActivity;
import com.mct.base.ui.core.IBaseFragment;
import com.mct.base.ui.core.IBaseView;
import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.core.IKeyboardManager;
import com.mct.base.ui.transition.FragmentTransitionAnimFactory;
import com.mct.base.ui.transition.animator.CircularRevealAnimator;
import com.mct.base.ui.transition.annotation.AnimType;
import com.mct.base.ui.transition.annotation.AnimatorStyle;
import com.mct.base.ui.transition.options.AnimExtras;
import com.mct.base.ui.transition.options.AnimOptions;
import com.mct.base.ui.transition.options.AnimOptionsData;

import java.lang.reflect.Method;

public abstract class BaseFragment extends Fragment implements IBaseFragment, IBaseView {

    private static final int ANIM_DURATION = 300;
    private IBaseActivity mIBaseActivity;
    private IExtraTransaction mIExtraTransaction;
    private AnimExtras mAnimExtras;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mIBaseActivity = ((BaseActivity) context).getBaseActivity();
            mIExtraTransaction = new ExtraTransaction(getContainerId(), getChildFragmentManager(), mIBaseActivity.keyboardManager());
        } else {
            throw new RuntimeException("The activity must extends BaseActivity!");
        }
    }

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        // update elevation before running animation
        getView().setElevation(getPopDirection() ? enter ? -1 : 1 : enter ? 1 : -1);
        if (nextAnim < 0) {
            mAnimExtras = FragmentTransitionAnimFactory.create(onRequestAnimOptionsData(nextAnim, enter));
        } else {
            mAnimExtras = FragmentTransitionAnimFactory.create(getContext(), transit, enter, nextAnim);
        }
        if (onDisableTouchEventWhenAnimRunning()) {
            disableFragmentTouchInDuration(mAnimExtras.getDuration() / 2);
        }
        return mAnimExtras.animation != null ? mAnimExtras.animation : null;
    }

    @Nullable
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return mAnimExtras.animator;
    }

    public int getContainerId() {
        return 0;
    }

    protected final boolean getPopDirection() {
        try {
            Method method = Fragment.class.getDeclaredMethod("getPopDirection");
            method.setAccessible(true);
            return (boolean) method.invoke(this);
        } catch (Throwable ignored) {
            return false;
        }
    }

    protected AnimOptionsData onRequestAnimOptionsData(int nextAnim, boolean enter) {
        AnimOptions options = AnimOptions.fromOptionsValue(nextAnim);
        AnimOptionsData aod = new AnimOptionsData();
        aod.setOptions(options);
        aod.setDuration(onRequestAnimDuration());
        aod.setEnter(enter);
        if (options.getAnimType() == AnimType.ANIMATOR) {
            View view = getView();
            if (view.getParent() == null && view.getTag(R.id.tag_parent_view) == null) {
                view.setTag(R.id.tag_parent_view, getParentView());
            }
            aod.setView(view);
            if (options.getAnimStyle() == AnimatorStyle.CIRCULAR_REVEAL) {
                aod.setCircularPosition(onRequestCircularPosition());
            }
        }
        return aod;
    }

    /**
     * This function support for {@link CircularRevealAnimator} animator.<br/>
     * You can override to modify the initial position of anim.<br/>
     * Default center of the view.
     */
    @NonNull
    protected Point onRequestCircularPosition() {
        Point result = new Point();
        View view = getParentView();
        if (view != null) {
            int[] positions = new int[2];
            view.getLocationInWindow(positions);
            result.x = positions[0] + view.getWidth() / 2;
            result.y = positions[1] + view.getHeight() / 2;
        }
        return result;
    }

    protected int onRequestAnimDuration() {
        return ANIM_DURATION;
    }

    protected boolean onDisableTouchEventWhenAnimRunning() {
        return true;
    }

    @Override
    public IKeyboardManager keyboardManager() {
        return mIBaseActivity.keyboardManager();
    }

    @Override
    public IExtraTransaction extraTransaction() {
        return mIBaseActivity.extraTransaction();
    }

    @Override
    public IExtraTransaction childExtraTransaction() {
        return mIExtraTransaction;
    }

    @Override
    public IExtraTransaction parentExtraTransaction() {
        if (getParentFragment() instanceof BaseFragment) {
            return ((BaseFragment) getParentFragment()).childExtraTransaction();
        } else {
            return null;
        }
    }

    @Override
    public IExtraTransaction parentExtraTransaction(Class<? extends BaseFragment> parent) {
        if (getParentFragment() instanceof BaseFragment) {
            if (getParentFragment().getClass() == parent) {
                return ((BaseFragment) getParentFragment()).childExtraTransaction();
            }
            return ((BaseFragment) getParentFragment()).parentExtraTransaction(parent);
        }
        return null;
    }

    @Override
    public View getParentView() {
        if (getView() != null && getView().getParent() instanceof ViewGroup) {
            return (View) getView().getParent();
        }
        if (getParentFragment() != null) {
            return getParentFragment().getView();
        }
        if (getActivity() != null) {
            return getActivity().findViewById(extraTransaction().getContainerId());
        }
        return null;
    }

    @Override
    public void post(Runnable runnable) {
        if (mIBaseActivity != null) {
            mIBaseActivity.post(runnable);
        }
    }

    @Override
    public void postDelayed(Runnable runnable, long delay) {
        if (mIBaseActivity != null) {
            mIBaseActivity.postDelayed(runnable, delay);
        }
    }

    @Override
    public void removeCallbacks(Runnable runnable) {
        if (mIBaseActivity != null) {
            mIBaseActivity.removeCallbacks(runnable);
        }
    }

    @Override
    public boolean onBackPressed() {
        Fragment fragment = childExtraTransaction().getCurrentFragment();
        if (fragment instanceof IBaseFragment && ((IBaseFragment) fragment).onBackPressed()) {
            return true;
        }
        if (childExtraTransaction().getBackStackCount() != 0) {
            childExtraTransaction().popFragment();
            return true;
        }
        return false;
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError(Throwable t) {
    }

    private void disableFragmentTouchInDuration(long duration) {
        if (duration <= 0) {
            return;
        }
        setFragmentTouch(true);
        postDelayed(() -> setFragmentTouch(false), duration);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setFragmentTouch(boolean disable) {
        String tag = "disableTouchOverlayTagView";
        View view = getParentView();
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            View overlay = parent.findViewWithTag(tag);
            if (disable) {
                if (overlay == null) {
                    overlay = new View(getContext());
                    overlay.setTag(tag);
                    overlay.setElevation(Float.MAX_VALUE);
                    overlay.setOnTouchListener((v, event) -> true);
                    parent.addView(overlay, new ViewGroup.LayoutParams(-1, -1));
                }
            } else {
                if (overlay != null) {
                    parent.removeView(overlay);
                }
            }
        }
    }

}
