package com.mct.base.ui;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.mct.base.ui.core.IBaseActivity;
import com.mct.base.ui.core.IBaseFragment;
import com.mct.base.ui.core.IExtraTransaction;
import com.mct.base.ui.core.IWindowInsetsApplier;
import com.mct.base.ui.core.IKeyboardManager;

public abstract class BaseActivity extends AppCompatActivity implements IWindowInsetsApplier {

    private Handler mHandler;
    private BaseActivityWrapper mBaseActivity;
    private KeyboardManagerWrapper mKeyboardManager;
    private IExtraTransaction mIExtraTransaction;

    ///////////////////////////////////////////////////////////////////////////
    // LifeCircle
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = extraTransaction().getCurrentFragment();
                if (fragment instanceof IBaseFragment && ((IBaseFragment) fragment).handleOnBackPressed()) {
                    return;
                }
                if (extraTransaction().getBackStackCount() != 0) {
                    extraTransaction().popFragment();
                    return;
                }
                if (onHandleBackPressed()) {
                    return;
                }
                finish();
            }
        });
        mHandler = new Handler(getMainLooper());
    }

    ///////////////////////////////////////////////////////////////////////////
    // Can be override
    ///////////////////////////////////////////////////////////////////////////

    @IdRes
    protected int getContainerId() {
        return Window.ID_ANDROID_CONTENT;
    }

    protected boolean onHandleBackPressed() {
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Impl area
    ///////////////////////////////////////////////////////////////////////////

    public IBaseActivity getBaseActivity() {
        if (mBaseActivity == null) {
            mBaseActivity = new BaseActivityWrapper();
        }
        return mBaseActivity;
    }

    protected IKeyboardManager keyboardManager() {
        if (mKeyboardManager == null) {
            mKeyboardManager = new KeyboardManagerWrapper();
        }
        return mKeyboardManager;
    }

    protected IExtraTransaction extraTransaction() {
        if (mIExtraTransaction == null) {
            mIExtraTransaction = new ExtraTransaction(getContainerId(), getSupportFragmentManager(), keyboardManager());
        }
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

    protected void hideSoftInput() {
        hideSoftInput(0);
    }

    protected void hideSoftInput(long delay) {
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
        postDelayed(hideSoftInputRunnable, delay);
    }

    protected void hideSoftInput(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
            return BaseActivity.this.keyboardManager();
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

        @Override
        public void requestBackPress() {
            BaseActivity.this.getOnBackPressedDispatcher().onBackPressed();
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
        public void hideSoftInput() {
            BaseActivity.this.hideSoftInput();
        }

        @Override
        public void hideSoftInput(long delay) {
            BaseActivity.this.hideSoftInput(delay);
        }

        @Override
        public void hideSoftInput(@NonNull View view) {
            BaseActivity.this.hideSoftInput(view);
        }

        @Override
        public boolean isSoftInputVisible() {
            return BaseActivity.this.isSoftInputVisible();
        }
    }
}
