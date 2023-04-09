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
import com.mct.base.ui.transition.option.AnimExtras;
import com.mct.base.ui.transition.option.AnimOptions;
import com.mct.base.ui.transition.option.AnimOptionsData;

public abstract class BaseFragment extends Fragment implements IBaseFragment, IBaseView {

    private static final int ANIM_DURATION = 300;
    private IBaseActivity mIBaseActivity;
    private IExtraTransaction mIExtraTransaction;
    private AnimExtras mAnimExtras;

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim < 0) {
            AnimOptions options = AnimOptions.fromOptionsValue(nextAnim);
            AnimOptionsData aod = onRequestAnimOptionsData(options, enter);
            mAnimExtras = FragmentTransitionAnimFactory.create(aod);
        } else {
            mAnimExtras = FragmentTransitionAnimFactory.create(getContext(), transit, enter, nextAnim);
        }
        if (mAnimExtras.animation != null) {
            if (enter && onDisableTouchEventWhenAnimRunning()) {
                disableFragmentTouchInDuration(mAnimExtras.animation.getDuration());
            }
            return mAnimExtras.animation;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (nextAnim < 0) {
            AnimOptions options = AnimOptions.fromOptionsValue(nextAnim);
            if (enter) getView().post(() -> {
                AnimOptionsData aod = onRequestAnimOptionsData(options, enter);
                mAnimExtras = FragmentTransitionAnimFactory.create(aod);
                mAnimExtras.animator.start();
            });
        }
        if (enter && onDisableTouchEventWhenAnimRunning()) {
            disableFragmentTouchInDuration(mAnimExtras.animator.getDuration());
        }
        return mAnimExtras.animator;
    }

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

    public int getContainerId() {
        return 0;
    }

    protected AnimOptionsData onRequestAnimOptionsData(AnimOptions options, boolean enter) {
        AnimOptionsData aod = new AnimOptionsData();
        aod.setOptions(options);
        aod.setDuration(onRequestAnimDuration());
        aod.setEnter(enter);
        if (options.getAnimType() == AnimType.ANIMATOR) {
            aod.setView(getView());
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
        View view = getView();
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
    public void onError(Throwable t) {
    }

    public IExtraTransaction parentExtraTransaction() {
        if (getParentFragment() instanceof BaseFragment) {
            return ((BaseFragment) getParentFragment()).childExtraTransaction();
        } else {
            return childExtraTransaction();
        }
    }

    private void disableFragmentTouchInDuration(long duration) {
        setFragmentTouch(true);
        postDelayed(() -> setFragmentTouch(false), duration);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setFragmentTouch(boolean disable) {
        String tag = "overlayTagView";
        if (getView() != null && getView().getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) getView().getParent();
            View overlay = parent.findViewWithTag(tag);
            if (disable) {
                if (overlay == null) {
                    overlay = new View(getContext());
                    overlay.setTag(tag);
                    parent.addView(overlay, new ViewGroup.LayoutParams(-1, -1));
                }
                overlay.setElevation(Float.MAX_VALUE);
                overlay.setOnTouchListener((v, event) -> true);
            } else {
                if (overlay != null) {
                    overlay.setOnTouchListener(null);
                    parent.removeView(overlay);
                }
            }
        }
    }

}
