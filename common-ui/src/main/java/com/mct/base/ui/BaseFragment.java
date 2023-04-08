package com.mct.base.ui;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Supplier;
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
import com.mct.base.ui.transition.option.AnimOptions;
import com.mct.base.ui.transition.option.AnimOptionsData;

public abstract class BaseFragment extends Fragment implements IBaseFragment, IBaseView {

    private static final int ANIM_DURATION = 300;
    private IBaseActivity mIBaseActivity;
    private IExtraTransaction mIExtraTransaction;

    @Nullable
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (nextAnim < 0) {
            AnimOptionsData aod = new AnimOptionsData();
            aod.setOptions(AnimOptions.fromOptionsValue(nextAnim));
            aod.setDuration(getAnimDuration());
            aod.setEnter(enter);
            if (aod.getOptions().getAnimType() == AnimType.ANIMATION) {
                return FragmentTransitionAnimFactory.create(aod, Animation.class);
            }
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (nextAnim < 0) {
            AnimOptionsData aod = new AnimOptionsData();
            aod.setOptions(AnimOptions.fromOptionsValue(nextAnim));
            aod.setDuration(getAnimDuration());
            aod.setEnter(enter);
            if (aod.getOptions().getAnimType() == AnimType.ANIMATOR) {
                View view = getView();
                Supplier<Animator> animator = () -> {
                    aod.setView(view);
                    if (aod.getOptions().getAnimStyle() == AnimatorStyle.CIRCULAR_REVEAL) {
                        aod.setCircularPosition(onRequestCircularPosition());
                    }
                    return FragmentTransitionAnimFactory.create(aod, Animator.class);
                };
                if (view == null) {
                    return FragmentTransitionAnimFactory.create(Animator.class);
                } else {
                    if (enter) {
                        // make sure the view attach to window
                        view.post(() -> animator.get().start());
                        return FragmentTransitionAnimFactory.create(Animator.class);
                    }
                    return animator.get();
                }
            }
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

    protected int getAnimDuration() {
        return ANIM_DURATION;
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
