package com.mct.base.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mct.base.ui.abs.IBaseActivity;
import com.mct.base.ui.abs.IBaseFragment;
import com.mct.base.ui.abs.IBaseView;
import com.mct.base.ui.abs.IExtraTransaction;
import com.mct.base.ui.abs.IKeyboardManager;

public abstract class BaseFragment extends Fragment implements IBaseFragment, IBaseView {

    private IBaseActivity mIBaseActivity;
    private IExtraTransaction mIExtraTransaction;

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
}
