package com.mct.base.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.mct.base.ui.core.IBaseActivity;
import com.mct.base.ui.core.IBaseFragment;
import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.core.IKeyboardManager;

public abstract class BaseActivity extends AppCompatActivity {

    private Handler mHandler;
    private BaseActivityWrapper mBaseActivity;
    private KeyboardManagerWrapper mKeyboardManager;
    private IExtraTransaction mIExtraTransaction;

    ///////////////////////////////////////////////////////////////////////////
    // Abstract
    ///////////////////////////////////////////////////////////////////////////

    @IdRes
    protected abstract int getContainerId();

    protected abstract boolean showToastOnBackPressed();

    ///////////////////////////////////////////////////////////////////////////
    // LifeCircle
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(getMainLooper());
        mBaseActivity = new BaseActivityWrapper();
        mKeyboardManager = new KeyboardManagerWrapper();
        mIExtraTransaction = new ExtraTransaction(getContainerId(), getSupportFragmentManager(), mKeyboardManager);
    }

    private boolean mIsBackPressed;

    @Override
    public void onBackPressed() {
        Fragment fragment = extraTransaction().getCurrentFragment();
        if (fragment instanceof IBaseFragment && ((IBaseFragment) fragment).onBackPressed()) {
            return;
        }
        if (extraTransaction().getBackStackCount() == 0) {
            if (!mIsBackPressed && showToastOnBackPressed()) {
                mIsBackPressed = true;
                postDelayed(() -> mIsBackPressed = false, 3000);
                return;
            }
        } else {
            mIsBackPressed = false;
            extraTransaction().popFragment();
            return;
        }
        mIsBackPressed = false;
        super.onBackPressed();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Impl
    ///////////////////////////////////////////////////////////////////////////


    public IBaseActivity getBaseActivity() {
        return mBaseActivity;
    }

    protected IKeyboardManager getKeyboardManager() {
        return mKeyboardManager;
    }

    protected IExtraTransaction extraTransaction() {
        return mIExtraTransaction;
    }

    protected void post(Runnable runnable) {
        mHandler.post(runnable);
    }

    protected void postDelayed(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }

    protected void removeCallbacks(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SoftInput
    ///////////////////////////////////////////////////////////////////////////

    private Runnable hideSoftInputRunnable;

    protected void clearFocus() {
        View view = getWindow().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
        }
        getAndFocusFakeView();
    }

    protected void showSoftInput(@NonNull View view) {
        if (!view.isFocused()) {
            view.requestFocus();
        }
        if (!isSoftInputVisible()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }
            imm.showSoftInput(view, 0);
        }
    }

    protected void hideSoftInput(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void hideSoftInput() {
        if (hideSoftInputRunnable == null) {
            hideSoftInputRunnable = () -> {
                if (isSoftInputVisible()) {
                    View view = getWindow().getCurrentFocus();
                    if (view == null) {
                        view = getAndFocusFakeView();
                    }
                    hideSoftInput(view);
                }
            };
        }
        removeCallbacks(hideSoftInputRunnable);
        postDelayed(hideSoftInputRunnable, 200);
    }

    protected boolean isSoftInputVisible() {
        return getSoftInputHeight() != -1;
    }

    /**
     * @return -1 if keyboard is hidden<br>
     * keyboardHeight if key board is visible
     */
    private int getSoftInputHeight() {
        final View contentView = findViewById(android.R.id.content);
        Rect r = new Rect();
        contentView.getWindowVisibleDisplayFrame(r);
        final int screenHeight = contentView.getRootView().getHeight();
        final int softInputHeight = screenHeight - r.bottom;
        return softInputHeight > screenHeight * 0.15 ? softInputHeight : -1;
    }

    @NonNull
    private View getAndFocusFakeView() {
        String tag = "keyboardTagView";
        View decorView = getWindow().getDecorView();
        View focusView = decorView.findViewWithTag(tag);
        View fakeView;
        if (focusView == null) {
            fakeView = new AppCompatEditText(getWindow().getContext()) {
                @Override
                public boolean onCheckIsTextEditor() {
                    return false;// disable auto show key board when resume
                }
            };
            fakeView.setTag(tag);
            ((ViewGroup) decorView).addView(fakeView, 0, 0);
        } else {
            fakeView = focusView;
        }
        fakeView.requestFocus();
        return fakeView;
    }

    class BaseActivityWrapper implements IBaseActivity {

        @Override
        public IKeyboardManager keyboardManager() {
            return BaseActivity.this.getKeyboardManager();
        }

        @Override
        public IExtraTransaction extraTransaction() {
            return BaseActivity.this.extraTransaction();
        }

        @Override
        public void post(Runnable runnable) {
            BaseActivity.this.post(runnable);
        }

        @Override
        public void postDelayed(Runnable runnable, long delay) {
            BaseActivity.this.postDelayed(runnable, delay);
        }

        @Override
        public void removeCallbacks(Runnable runnable) {
            BaseActivity.this.removeCallbacks(runnable);
        }

    }

    class KeyboardManagerWrapper implements IKeyboardManager {
        @Override
        public void clearFocus() {
            BaseActivity.this.clearFocus();
        }

        @Override
        public void showSoftInput(@NonNull View view) {
            BaseActivity.this.showSoftInput(view);
        }

        @Override
        public void hideSoftInput(@NonNull View view) {
            BaseActivity.this.hideSoftInput(view);
        }

        @Override
        public void hideSoftInput() {
            BaseActivity.this.hideSoftInput();
        }

        @Override
        public boolean isSoftInputVisible() {
            return BaseActivity.this.isSoftInputVisible();
        }
    }
}
