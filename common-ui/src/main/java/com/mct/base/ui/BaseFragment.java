package com.mct.base.ui;

import static com.mct.base.ui.transition.annotation.AnimDirection.LEFT;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mct.base.ui.core.IBaseActivity;
import com.mct.base.ui.core.IBaseFragment;
import com.mct.base.ui.core.IBaseView;
import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.core.IKeyboardManager;
import com.mct.base.ui.transition.AnimationFactory;
import com.mct.base.ui.transition.animator.CircularAnimator;
import com.mct.base.ui.transition.animator.SlideAnimator;
import com.mct.base.ui.transition.annotation.AnimDirection;
import com.mct.base.ui.transition.annotation.Transit;

public abstract class BaseFragment extends Fragment implements IBaseFragment, IBaseView {

    private static final int ANIM_DURATION = 300;
    private IBaseActivity mIBaseActivity;
    private IExtraTransaction mIExtraTransaction;
    @Transit
    private int mTransit = FragmentTransaction.TRANSIT_NONE;

    @Override
    public Animation onCreateAnimation(@Transit int transit, boolean enter, int nextAnim) {
        if (transit == Transit.TRANSIT_CUSTOM_ANIMATION) {
            mTransit = Transit.TRANSIT_CUSTOM_ANIMATION;
        }
        return mTransit == Transit.TRANSIT_CUSTOM_ANIMATION
                ? AnimationFactory.createAnimation(nextAnim, enter, ANIM_DURATION)
                : super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public Animator onCreateAnimator(@Transit int transit, boolean enter, int nextAnim) {
        if (transit == Transit.TRANSIT_CUSTOM_ANIMATOR) {
            mTransit = Transit.TRANSIT_CUSTOM_ANIMATOR;
        }
        Log.e("ddd", "onCreateAnimator: " + mTransit + " " +enter);
        return mTransit == Transit.TRANSIT_CUSTOM_ANIMATOR
                ? new SlideAnimator(requireView(), LEFT, enter, ANIM_DURATION)
                : super.onCreateAnimator(transit, enter, nextAnim);
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
