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
    public final boolean onBackPressed() {
        if (mAnimExtras != null) {
            return true; // block when animation is running.
        }
        Fragment fragment = childExtraTransaction().getCurrentFragment();
        if (fragment instanceof IBaseFragment && ((IBaseFragment) fragment).onBackPressed()) {
            return true;
        }
        if (onHandleBackPressed()) {
            return true; // has handle back press.
        }
        if (childExtraTransaction().getBackStackCount() != 0) {
            childExtraTransaction().popFragment();
            return true;
        }
        return false;
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
            View view = getView();
            assert view != null;
            if (view.getParent() == null && view.getTag(R.id.tag_parent_view) == null) {
                view.setTag(R.id.tag_parent_view, getParentView());
            }
            aod.setView(view);
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
        View overlay = getOverlay();
        if (overlay != null) {
            if (show) {
                boolean isPop = getPopDirection();
                overlay.setAlpha(isPop ? 1 : 0);
                overlay.animate().alpha(isPop ? 0 : 1).start();
                overlay.setVisibility(View.VISIBLE);
            } else {
                overlay.setVisibility(View.GONE);
            }
        }
    }

    private void setDisableFragmentTouch(boolean disable) {
        View touchOverlay = getTouchOverlay();
        if (touchOverlay != null) {
            touchOverlay.setVisibility(disable ? View.VISIBLE : View.GONE);
        }
    }

    private View getOverlay() {
        if (overlay != null) {
            return overlay;
        }
        View view = getParentView();
        if (view.getTag(R.id.tag_overlay_view) != null) {
            overlay = (View) view.getTag(R.id.tag_overlay_view);
        } else {
            overlay = getOverlayByTag("OverlayTag");
            view.setTag(R.id.tag_overlay_view, overlay);
            if (overlay != null) {
                overlay.setBackgroundColor(getOverlayColor());
                overlay.setElevation(OVERLAY_VIEW_ELEVATION);
            }
        }
        return overlay;
    }

    @SuppressLint("ClickableViewAccessibility")
    private View getTouchOverlay() {
        if (touchOverlay != null) {
            return touchOverlay;
        }
        View view = getParentView();
        if (view.getTag(R.id.tag_touch_overlay_view) != null) {
            touchOverlay = (View) view.getTag(R.id.tag_touch_overlay_view);
        } else {
            touchOverlay = getOverlayByTag("TouchOverlayTag");
            view.setTag(R.id.tag_touch_overlay_view, touchOverlay);
            if (touchOverlay != null) {
                touchOverlay.setElevation(DISABLE_TOUCH_OVERLAY_VIEW_ELEVATION);
                touchOverlay.setOnTouchListener((v, event) -> true);
            }
        }
        return touchOverlay;
    }

    private void clearOverlay() {
        overlay = null;
        touchOverlay = null;
    }

    @Nullable
    private View getOverlayByTag(String tag) {
        View view = getParentView();
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            View overlay = parent.findViewWithTag(tag);
            if (overlay == null) {
                overlay = new View(getContext());
                overlay.setTag(tag);
                overlay.setVisibility(View.GONE);
                parent.addView(overlay, new ViewGroup.LayoutParams(-1, -1));
            }
            return overlay;
        }
        return null;
    }

}
