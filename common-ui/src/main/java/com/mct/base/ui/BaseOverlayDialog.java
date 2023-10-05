package com.mct.base.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.InsetDialogOnTouchListener;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
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

    protected final View getView() {
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
            mView = onCreateView(LayoutInflater.from(mContext));
            mDialog = onCreateDialog(mContext);
            DialogOption option = onCreateDialogOption();
            if (option == null) {
                option = new DialogOption.Builder().build();
            }
            initDialog(mDialog, mView, option);
            initWindow(mDialog.getWindow(), option);
            onDialogCreated(mDialog, mView);
        }
    }

    private void initDialog(@NonNull AppCompatDialog dialog, @Nullable View view, @NonNull DialogOption opt) {
        // dismiss listener
        dialog.setOnDismissListener(d -> {
            dismiss();
            hideSoftInput();
        });
        // back pressed
        dialog.setOnKeyListener((d, i, e) -> {
            if (i == KeyEvent.KEYCODE_BACK || i == KeyEvent.KEYCODE_ESCAPE) {
                if (!onBackPressed()) {
                    dismiss();
                }
                return true;
            }
            return false;
        });
        // background
        Window window = dialog.getWindow();
        window.getDecorView().post(() -> {
            int contentViewIdRes;
            boolean roundedBottomCorners;
            if (dialog instanceof BottomSheetDialog) {
                contentViewIdRes = com.google.android.material.R.id.design_bottom_sheet;
                roundedBottomCorners = false;
            } else {
                contentViewIdRes = Window.ID_ANDROID_CONTENT;
                roundedBottomCorners = true;
            }
            View contentView = window.findViewById(contentViewIdRes);
            if (contentView != null) {
                contentView.setClipToOutline(true);
                contentView.setBackground(opt.shapeAppearanceModel != null
                        ? createPopupDrawable(mContext, opt.backgroundColor, opt.shapeAppearanceModel)
                        : createPopupDrawable(mContext, opt.backgroundColor, opt.cornerRadius, roundedBottomCorners));
            }
        });
        // content
        if (view != null) {
            if (dialog instanceof AlertDialog) {
                ((AlertDialog) dialog).setView(view);
            } else {
                dialog.setContentView(view);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void initWindow(@NonNull Window window, @NonNull DialogOption opt) {
        // overlay
        if (opt.type != DialogOption.UNSET) {
            window.setType(opt.type);
        }
        // animations
        if (opt.windowAnimation != DialogOption.UNSET) {
            window.getAttributes().windowAnimations = opt.windowAnimation;
        }
        // background
        Rect insets = opt.backgroundInsets;
        if (insets != null) {
            window.getDecorView().setOnTouchListener(new InsetDialogOnTouchListener(getDialog(), insets));
            window.setBackgroundDrawable(new InsetDrawable(new ColorDrawable(Color.TRANSPARENT),
                    insets.left, insets.top, insets.right, insets.bottom)
            );
        } else {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        // soft input
        window.setSoftInputMode(opt.softInputMode);
        // on init window
        onInitWindow(window);
    }

    @NonNull
    private static Drawable createPopupDrawable(Context context, int color, int radius, boolean roundedBottomCorners) {
        float bottomCornerRadius = roundedBottomCorners ? radius : 0;
        return createPopupDrawable(context, color, ShapeAppearanceModel.builder()
                .setTopLeftCornerSize(radius)
                .setTopRightCornerSize(radius)
                .setBottomLeftCornerSize(bottomCornerRadius)
                .setBottomRightCornerSize(bottomCornerRadius)
                .build());
    }

    @NonNull
    private static Drawable createPopupDrawable(Context context, int color, ShapeAppearanceModel shapeAppearanceModel) {
        MaterialShapeDrawable popupDrawable = MaterialShapeDrawable.createWithElevationOverlay(context);
        popupDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        popupDrawable.setFillColor(ColorStateList.valueOf(color));
        return popupDrawable;
    }

    public static class DialogOption {

        public static final int UNSET = 0;

        int type;
        int softInputMode;
        int windowAnimation;
        int cornerRadius;
        int backgroundColor;
        Rect backgroundInsets;
        ShapeAppearanceModel shapeAppearanceModel;

        private DialogOption(@NonNull Builder builder) {
            this.type = builder.type;
            this.softInputMode = builder.softInputMode;
            this.windowAnimation = builder.windowAnimation;
            this.cornerRadius = builder.cornerRadius;
            this.backgroundColor = builder.backgroundColor;
            this.backgroundInsets = builder.backgroundInsets;
            this.shapeAppearanceModel = builder.shapeAppearanceModel;
        }

        public static class Builder {
            private int type;
            private int softInputMode;
            private int windowAnimation;
            private int cornerRadius;
            private int backgroundColor;
            private Rect backgroundInsets;
            private ShapeAppearanceModel shapeAppearanceModel;

            public Builder() {
                type = UNSET;
                windowAnimation = UNSET;
                cornerRadius = UNSET;
                backgroundColor = Color.TRANSPARENT;
                backgroundInsets = null;
                shapeAppearanceModel = null;
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

            public Builder setCornerRadius(int cornerRadius) {
                this.cornerRadius = cornerRadius;
                return this;
            }

            public Builder setBackgroundColor(int backgroundColor) {
                this.backgroundColor = backgroundColor;
                return this;
            }

            public Builder setBackgroundInsets(Rect backgroundInsets) {
                this.backgroundInsets = backgroundInsets;
                return this;
            }

            public Builder setBackgroundInsets(int insets) {
                this.backgroundInsets = new Rect(insets, insets, insets, insets);
                return this;
            }

            public Builder setBackgroundInsets(int left, int top, int right, int bottom) {
                this.backgroundInsets = new Rect(left, top, right, bottom);
                return this;
            }

            public Builder setShapeAppearance(ShapeAppearanceModel shapeAppearanceModel) {
                this.shapeAppearanceModel = shapeAppearanceModel;
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
