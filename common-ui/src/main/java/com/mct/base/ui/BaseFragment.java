package com.mct.base.ui;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Supplier;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.core.IBaseActivity;
import com.mct.base.ui.core.IBaseFragment;
import com.mct.base.ui.core.IBaseView;
import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.core.IKeyboardManager;
import com.mct.base.ui.transition.AnimFactory;
import com.mct.base.ui.transition.animator.CircularAnimator;
import com.mct.base.ui.transition.annotation.Transit;

public abstract class BaseFragment extends Fragment implements IBaseFragment, IBaseView {

    private static final int ANIM_DURATION = 300;
    private IBaseActivity mIBaseActivity;
    private IExtraTransaction mIExtraTransaction;
    @Transit
    private int mTransit = FragmentTransaction.TRANSIT_NONE;

    @Nullable
    @Override
    public Animation onCreateAnimation(@Transit int transit, boolean enter, int nextAnim) {
        // The transit is TRANSIT_CUSTOM_ANIMATOR => stop create anim
        if (transit == Transit.TRANSIT_CUSTOM_ANIMATOR) {
            return super.onCreateAnimation(transit, enter, nextAnim);
        }
        if (transit == Transit.TRANSIT_CUSTOM_ANIMATION) {
            mTransit = Transit.TRANSIT_CUSTOM_ANIMATION;
        }
        if (mTransit == Transit.TRANSIT_CUSTOM_ANIMATION && nextAnim < 0) {
            return AnimFactory.createAnimation(nextAnim, enter, ANIM_DURATION);
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public Animator onCreateAnimator(@Transit int transit, boolean enter, int nextAnim) {
        // The transit is TRANSIT_CUSTOM_ANIMATION => stop create anim
        if (transit == Transit.TRANSIT_CUSTOM_ANIMATION) {
            return super.onCreateAnimator(transit, enter, nextAnim);
        }
        if (transit == Transit.TRANSIT_CUSTOM_ANIMATOR) {
            mTransit = Transit.TRANSIT_CUSTOM_ANIMATOR;
        }
        if (mTransit == Transit.TRANSIT_CUSTOM_ANIMATOR && nextAnim < 0) {
            View view = getView();
            Supplier<Animator> animator = () -> {
                Point position = onRequestCircularPosition();
                return AnimFactory.createAnimator(view, nextAnim, enter, ANIM_DURATION, position);
            };
            if (view == null) {
                return AnimFactory.noneAnim(Animator.class);
            } else {
                if (enter) {
                    // make sure the view attach to window
                    view.post(() -> animator.get().start());
                    return AnimFactory.noneAnim(Animator.class);
                }
            }
            return animator.get();
        }
        return super.onCreateAnimator(transit, enter, nextAnim);
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

    /**
     * This function support for {@link CircularAnimator} animator.<br/>
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
        Log.e("ddd", "onRequestCenterView: " + result);
        return result;
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
    public void postDelay(Runnable runnable, long delay) {
        if (mIBaseActivity != null) {
            mIBaseActivity.postDelay(runnable, delay);
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
}
