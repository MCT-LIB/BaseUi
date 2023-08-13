package com.mct.base.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Note:</b><i>The BottomSheetDialog can't use DialogOption.setWindowBackground</i><br/>
 * <b>Theme Transparent for bottom sheet</b>
 * <pre>
 * &lt;style name="YourTheme" parent="Theme.MaterialComponents.DayNight.NoActionBar"&gt;
 *      &lt;item name="bottomSheetDialogTheme"&gt;@style/BottomSheetDialogTheme&lt;/item&gt;
 * &lt;/style&gt;
 *
 * &lt;style name="BottomSheetDialogTheme" parent="Theme.MaterialComponents.BottomSheetDialog"&gt;
 *      &lt;item name="bottomSheetStyle"&gt;@style/BottomSheetStyle&lt;/item&gt;
 * &lt;/style&gt;
 *
 * &lt;style name="BottomSheetStyle" parent="Widget.Design.BottomSheet.Modal"&gt;
 *      &lt;!-- Custom background --&gt;
 *      &lt;item name="android:background"&gt;@android:color/transparent&lt;/item&gt;
 * &lt;/style&gt;
 * </pre>
 */
public abstract class BaseOverlayDialog implements LifecycleEventObserver {

    private final Context mContext;
    private final InputMethodManager mInputManager;

    private View mView;
    private AppCompatDialog mDialog;

    private boolean mIsShowing;

    private Lifecycle mLifecycle;
    private List<OnShowListener> mOnShowListeners;
    private List<OnDismissListener> mOnDismissListeners;

    public BaseOverlayDialog(@NonNull Context context) {
        this(context, null);
    }

    public BaseOverlayDialog(@NonNull Context context, Lifecycle lifecycle) {
        this.mContext = context;
        this.mLifecycle = lifecycle;
        this.mInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public
    ///////////////////////////////////////////////////////////////////////////

    public void show() {
        createDialogIfNecessary();
        if (!mIsShowing) {
            mIsShowing = true;
            mDialog.show();
            onShowed();
            addObserverLifecycle();
        }
    }

    public void dismiss() {
        if (mIsShowing) {
            mIsShowing = false;
            mDialog.dismiss();
            onDismissed();
            removeObserveLifecycle();
        }
    }

    public BaseOverlayDialog setLifecycle(Lifecycle lifecycle) {
        if (mIsShowing) {
            removeObserveLifecycle();
            mLifecycle = lifecycle;
            addObserverLifecycle();
        } else {
            mLifecycle = lifecycle;
        }
        return this;
    }

    public BaseOverlayDialog addOnShowListener(OnShowListener listener) {
        if (listener == null) {
            return this;
        }
        if (mOnShowListeners == null) {
            mOnShowListeners = new ArrayList<>();
        }
        if (!mOnShowListeners.contains(listener)) {
            mOnShowListeners.add(listener);
        }
        return this;
    }

    public BaseOverlayDialog removeOnShowListener(OnShowListener listener) {
        if (mOnShowListeners != null) {
            mOnShowListeners.remove(listener);
        }
        return this;
    }

    public BaseOverlayDialog addOnDismissListener(OnDismissListener listener) {
        if (listener == null) {
            return this;
        }
        if (mOnDismissListeners == null) {
            mOnDismissListeners = new ArrayList<>();
        }
        if (!mOnDismissListeners.contains(listener)) {
            mOnDismissListeners.add(listener);
        }
        return this;
    }

    public BaseOverlayDialog removeOnDismissListener(OnDismissListener listener) {
        if (mOnDismissListeners != null) {
            mOnDismissListeners.remove(listener);
        }
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // SoftInput
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Show the soft keyboard.
     */
    public void showSoftInput(@NonNull View view) {
        view.requestFocus();
        mInputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Hide the soft keyboard.
     */
    public void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(mDialog.getWindow().getDecorView().getWindowToken(), 0);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        // @formatter:off
        switch (event) {
            case ON_START:      onStart();  break;
            case ON_RESUME:     onResume(); break;
            case ON_PAUSE:      onPause();  break;
            case ON_STOP:       onStop();   break;
            case ON_DESTROY:    dismiss();  break;
        }
        // @formatter:on
    }

    protected void onStart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    private void addObserverLifecycle() {
        if (mLifecycle != null) {
            mLifecycle.addObserver(this);
        }
    }

    private void removeObserveLifecycle() {
        if (mLifecycle != null) {
            mLifecycle.removeObserver(this);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Abstract
    ///////////////////////////////////////////////////////////////////////////

    /**
     * An abstract method to create a View for the dialog.
     *
     * @param inflater helper inflater
     * @return a View to be displayed by the dialog.
     */
    protected abstract View onCreateView(@NonNull LayoutInflater inflater);

    /**
     * An abstract method to create an AlertDialog.Builder for the dialog.
     *
     * @return an AppCompatDialog
     */
    protected abstract AppCompatDialog onCreateDialog(Context context);

    /**
     * An abstract method called after the dialog is created.
     * This method provides the subclass with an opportunity to perform any
     * necessary operations on the dialog and View that was created.
     *
     * @param dialog the dialog was created
     * @param view   the view was created
     */
    protected abstract void onDialogCreated(@NonNull AppCompatDialog dialog, View view);

    /**
     * Creates the dialog option
     *
     * @return The option can be modify the dialog window
     */
    @Nullable
    protected abstract DialogOption onCreateDialogOption();

    ///////////////////////////////////////////////////////////////////////////
    // Protected
    ///////////////////////////////////////////////////////////////////////////

    protected final Context getContext() {
        return mContext;
    }

    public View getView() {
        return mView;
    }

    protected final AppCompatDialog getDialog() {
        return mDialog;
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        return mView.findViewById(id);
    }

    @CallSuper
    protected void onShowed() {
        if (mOnShowListeners == null) {
            return;
        }
        for (OnShowListener listener : mOnShowListeners) {
            listener.onShow(this);
        }
    }

    @CallSuper
    protected void onDismissed() {
        if (mOnDismissListeners == null) {
            return;
        }
        for (OnDismissListener listener : mOnDismissListeners) {
            listener.onDismiss(this);
        }
    }

    protected void onInitWindow(Window window) {
    }

    /**
     * Handle backPress if need
     *
     * @return true if have handle backPress.
     */
    protected boolean onBackPressed() {
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private
    ///////////////////////////////////////////////////////////////////////////

    private void createDialogIfNecessary() {
        if (mDialog == null) {
            DialogInterface.OnDismissListener onDismissListener = d -> {
                dismiss();
                hideSoftInput();
            };
            DialogInterface.OnKeyListener onKeyListener = (d, i, e) -> {
                if (e.getKeyCode() == KeyEvent.KEYCODE_BACK && e.getAction() == KeyEvent.ACTION_UP) {
                    if (!onBackPressed()) {
                        dismiss();
                    }
                    return true;
                }
                return false;
            };
            mView = onCreateView(LayoutInflater.from(mContext));
            mDialog = onCreateDialog(mContext);
            mDialog.setOnDismissListener(onDismissListener);
            mDialog.setOnKeyListener(onKeyListener);
            if (mView != null) {
                if (mDialog instanceof AlertDialog) {
                    ((AlertDialog) mDialog).setView(mView);
                } else {
                    mDialog.setContentView(mView);
                }
            }
            initWindow(mDialog.getWindow(), onCreateDialogOption());
            onDialogCreated(mDialog, mView);
        }
    }

    private void initWindow(Window window, DialogOption opt) {
        if (opt == null) {
            opt = new DialogOption.Builder().build();
        }
        // overlay
        if (opt.type != DialogOption.UNSET) {
            window.setType(opt.type);
        }
        // animations
        if (opt.windowAnimation != DialogOption.UNSET) {
            window.getAttributes().windowAnimations = opt.windowAnimation;
        }
        // background
        if (opt.windowBackground != null) {
            window.setBackgroundDrawable(opt.windowBackground);
        }
        // soft input
        window.setSoftInputMode(opt.softInputMode);
        // auto hide soft input
        window.getDecorView().setOnTouchListener((view, e) -> {
            hideSoftInput();
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                view.performClick();
            }
            return false;
        });
        onInitWindow(window);
    }

    public static class DialogOption {

        public static final int UNSET = 0;

        int type;
        int softInputMode;
        int windowAnimation;
        Drawable windowBackground;

        private DialogOption(@NonNull Builder builder) {
            this.type = builder.type;
            this.softInputMode = builder.softInputMode;
            this.windowAnimation = builder.windowAnimation;
            this.windowBackground = builder.windowBackground;
        }

        public static class Builder {
            private int type;
            private int softInputMode;
            private int windowAnimation;
            private Drawable windowBackground;

            public Builder() {
                type = UNSET;
                windowAnimation = UNSET;
                windowBackground = null;
                softInputMode = LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
            }

            public Builder setType(int type) {
                this.type = type;
                return this;
            }

            public Builder setSoftInputMode(int softInputMode) {
                this.softInputMode = softInputMode;
                return this;
            }

            public Builder setWindowAnimation(int windowAnimation) {
                this.windowAnimation = windowAnimation;
                return this;
            }

            public Builder setWindowBackground(Drawable windowBackground) {
                this.windowBackground = windowBackground;
                return this;
            }

            public DialogOption build() {
                return new DialogOption(this);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Listener
    ///////////////////////////////////////////////////////////////////////////

    public interface OnShowListener {
        void onShow(BaseOverlayDialog dialog);
    }

    public interface OnDismissListener {
        void onDismiss(BaseOverlayDialog dialog);
    }
}
