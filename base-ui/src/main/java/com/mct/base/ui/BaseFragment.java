package com.mct.base.ui;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mct.base.ui.core.IBaseActivity;
import com.mct.base.ui.core.IBaseFragment;
import com.mct.base.ui.core.IBaseView;
import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.core.IKeyboardManager;
import com.mct.base.ui.transition.FragmentTransitionAnimFactory;
import com.mct.base.ui.transition.animation.NoneAnimation;
import com.mct.base.ui.transition.animator.CircularRevealAnimator;
import com.mct.base.ui.transition.annotation.AnimType;
import com.mct.base.ui.transition.annotation.AnimatorStyle;
import com.mct.base.ui.transition.options.AnimExtras;
import com.mct.base.ui.transition.options.AnimOptions;
import com.mct.base.ui.transition.options.AnimOptionsData;

import java.lang.reflect.Method;

public abstract class BaseFragment extends Fragment implements IBaseFragment, IBaseView, AnimExtras.AnimExtrasListener {

    private static final int VIEW_ELEVATION = 1;
    private static final int OVERLAY_VIEW_ELEVATION = 0;
    private static final int DISABLE_TOUCH_OVERLAY_VIEW_ELEVATION = 9999;
    private static final int ANIMATION_DURATION = 300;

    private IBaseActivity mIBaseActivity;
    private IExtraTransaction mIExtraTransaction;
    private AnimExtras mAnimExtras;
    private boolean mPendingPreventAnimation;

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle area
    ///////////////////////////////////////////////////////////////////////////

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
        if (mPendingPreventAnimation) {
            mPendingPreventAnimation = false;
            return NoneAnimation.create(0);
        }
        if (transit == 0 && nextAnim <= 0) {
            mAnimExtras = FragmentTransitionAnimFactory.create(createAnimOptionsData(nextAnim, enter));
        } else {
            mAnimExtras = FragmentTransitionAnimFactory.create(getContext(), transit, enter, nextAnim);
        }

        mAnimExtras.setAnimInfo(transit, enter, nextAnim);
        mAnimExtras.addAnimationListener(this);

        return mAnimExtras.animation != null ? mAnimExtras.animation : null;
    }

    @Nullable
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return mAnimExtras.animator;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!canTouchThroughBelowFragment()) {
            view.setClickable(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        clearOverlay();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Impl area
    ///////////////////////////////////////////////////////////////////////////

    /* ---------- AnimExtrasListener ---------- */
    @Override
    public void onAnimationStart(@NonNull AnimExtras animExtras) {
        int transit = animExtras.getTransit();
        boolean enter = animExtras.isEnter();
        int nextAnim = animExtras.getNextAnim();

        // update elevation before running animation
        int e = VIEW_ELEVATION;
        if (getView() != null) {
            getView().setElevation(getPopDirection() ? enter ? -e : e : enter ? e : -e);
        }

        if (transit == 0 && nextAnim <= 0) {
            AnimOptions options = AnimOptions.fromOptionsValue(nextAnim);
            if (options.hasOverlay() && canShowOverlayWhileRunningAnimation()) {
                setFragmentOverlay(true);
            }
        }

        if (!canTouchFragmentWhileRunningAnimation()) {
            setDisableFragmentTouch(true);
        }
    }

    @Override
    public void onAnimationEnd(@NonNull AnimExtras animExtras) {
        setDisableFragmentTouch(false);
        setFragmentOverlay(false);
        if (mAnimExtras != null) {
            mAnimExtras.removeAllListeners();
            mAnimExtras = null;
        }
    }

    /* ---------- IBaseFragment ---------- */
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
        if (getParentFragment() instanceof IBaseFragment) {
            return ((IBaseFragment) getParentFragment()).childExtraTransaction();
        } else {
            return null;
        }
    }

    @Override
    public IExtraTransaction parentExtraTransaction(Class<? extends IBaseFragment> parent) {
        if (getParentFragment() instanceof IBaseFragment) {
            if (getParentFragment().getClass().isInstance(parent)) {
                return ((IBaseFragment) getParentFragment()).childExtraTransaction();
            }
            return ((IBaseFragment) getParentFragment()).parentExtraTransaction(parent);
        }
        return null;
    }

    @Override
    public View getParentView() {
        View view = getView();
        if (view != null && view.getParent() != null) {
            return (View) view.getParent();
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment.getView() != null) {
            View parentView = parentFragment.getView();
            IExtraTransaction parentExtraTransaction = parentExtraTransaction();
            if (parentExtraTransaction != null) {
                return parentView.findViewById(parentExtraTransaction.getContainerId());
            }
            return parentView;
        }
        if (getActivity() != null) {
            return getActivity().findViewById(extraTransaction().getContainerId());
        }
        return null;
    }

    @Override
    public void pendingPreventAnimation() {
        this.mPendingPreventAnimation = true;
    }

    @Override
    public void removePendingPreventAnimation() {
        this.mPendingPreventAnimation = false;
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
    public void requestBackPress() {
        if (mIBaseActivity != null) {
            mIBaseActivity.requestBackPress();
        }
    }

    @Override
    public final boolean onBackPressed() {
        if (mAnimExtras != null) {
            return true; // block when animation is running.
        }
        Fragment fragment = childExtraTransaction().getCurrentFragment();
        if (fragment instanceof IBaseFragment && ((IBaseFragment) fragment).onBackPressed()) {
            return true;
        }
        if (childExtraTransaction().getBackStackCount() != 0) {
            childExtraTransaction().popFragment();
            return true;
        }
        return onHandleBackPressed(); // has handle back press.
    }

    /* ---------- IBaseView ---------- */
    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError(Throwable t) {
    }

    ///////////////////////////////////////////////////////////////////////////
    // Protected area
    ///////////////////////////////////////////////////////////////////////////

    protected int getContainerId() {
        return 0;
    }

    protected boolean onHandleBackPressed() {
        return false;
    }

    protected boolean canShowOverlayWhileRunningAnimation() {
        return true;
    }

    protected boolean canTouchFragmentWhileRunningAnimation() {
        return false;
    }

    protected boolean canTouchThroughBelowFragment() {
        return false;
    }

    protected int getAnimationDuration() {
        return ANIMATION_DURATION;
    }

    /**
     * This function support for {@link CircularRevealAnimator} animator.<br/>
     * You can override to modify the initial position of anim.<br/>
     * Default center of the view.
     */
    @NonNull
    protected Point getCircularPosition() {
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

    @ColorInt
    protected int getOverlayColor() {
        return Color.argb(128, 0, 0, 0);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private area
    ///////////////////////////////////////////////////////////////////////////

    @NonNull
    private AnimOptionsData createAnimOptionsData(int nextAnim, boolean enter) {
        AnimOptions options = AnimOptions.fromOptionsValue(nextAnim);
        AnimOptionsData aod = new AnimOptionsData();
        aod.setOptions(options);
        aod.setDuration(getAnimationDuration());
        aod.setEnter(enter);
        if (options.getAnimType() == AnimType.ANIMATOR) {
            aod.setView(getView());
            if (options.getAnimStyle() == AnimatorStyle.CIRCULAR_REVEAL) {
                aod.setCircularPosition(getCircularPosition());
            }
        }
        return aod;
    }

    private boolean getPopDirection() {
        try {
            Method method = Fragment.class.getDeclaredMethod("getPopDirection");
            method.setAccessible(true);
            Boolean result = (Boolean) method.invoke(this);
            return result != null && result;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /* ---------- Overlays ---------- */
    private View overlay;
    private View touchOverlay;

    private void setFragmentOverlay(boolean show) {
        if (show) {
            if (overlay == null) {
                View parentView = getParentView();
                if (!(parentView instanceof ViewGroup)) {
                    return;
                }
                overlay = getOverlayByTag((ViewGroup) parentView, "Overlay", OVERLAY_VIEW_ELEVATION);
                boolean isPop = getPopDirection();
                overlay.setAlpha(isPop ? 1 : 0);
                overlay.animate().alpha(isPop ? 0 : 1).start();
                overlay.setBackgroundColor(getOverlayColor());
            }
        } else {
            if (overlay != null) {
                removeInParent(overlay);
                overlay = null;
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDisableFragmentTouch(boolean disable) {
        if (disable) {
            if (touchOverlay == null) {
                View parentView = getParentView();
                if (!(parentView instanceof ViewGroup)) {
                    return;
                }
                touchOverlay = getOverlayByTag((ViewGroup) parentView, "TouchOverlay", DISABLE_TOUCH_OVERLAY_VIEW_ELEVATION);
                touchOverlay.setOnTouchListener((v, event) -> true);
            }
        } else {
            if (touchOverlay != null) {
                removeInParent(touchOverlay);
                touchOverlay = null;
            }
        }
    }

    private void clearOverlay() {
        removeInParent(overlay);
        removeInParent(touchOverlay);
        overlay = null;
        touchOverlay = null;
    }

    @Nullable
    private static View getOverlayByTag(@NonNull ViewGroup view, String tag, int elevation) {
        View overlay = view.findViewWithTag(tag);
        if (overlay == null) {
            overlay = new View(view.getContext());
            overlay.setTag(tag);
            overlay.setElevation(elevation);
            view.addView(overlay, new ViewGroup.LayoutParams(-1, -1));
        }
        return overlay;
    }

    private static void removeInParent(View view) {
        if (view != null && view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

}
