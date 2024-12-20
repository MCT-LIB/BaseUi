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
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.lifecycle.LifecycleEventObserver;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.mct.base.ui.utils.InsetDialogOnTouchListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public abstract class BaseOverlayDialog {

    private final Context mContext;
    private final InputMethodManager mInputManager;

    private View mView;
    private AppCompatDialog mDialog;
    private DialogOption mDialogOption;

    private boolean mIsHiding;
    private boolean mIsDismissed;

    private List<OnShowListener> mOnShowListeners;
    private List<OnDismissListener> mOnDismissListeners;

    public BaseOverlayDialog(@NonNull Context context) {
        this.mContext = context;
        this.mInputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public
    ///////////////////////////////////////////////////////////////////////////

    public final void show() {
        createDialogIfNecessary();
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public final void hide() {
        if (mDialog != null) {
            mDialog.hide();
        }
    }

    public final void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public final boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
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
        if (mDialog == null) {
            return;
        }
        mInputManager.hideSoftInputFromWindow(mDialog.getWindow().getDecorView().getWindowToken(), 0);
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
        if (null != mDialog) {
            return mDialog.findViewById(id);
        }
        if (null != mView) {
            return mView.findViewById(id);
        }
        throw new IllegalStateException("Dialog was not created yet.");
    }

    /**
     * Handle backPress if need
     *
     * @return true if have handle backPress.
     */
    protected boolean onHandleBackPressed() {
        return false;
    }

    /* --- Lifecycle --- */

    @CallSuper
    protected void onDialogCreated(@NonNull AppCompatDialog dialog, DialogOption dialogOption, View view) {
    }

    @CallSuper
    protected void onStart() {
        if (mOnShowListeners != null) {
            for (OnShowListener listener : mOnShowListeners) {
                listener.onShow(this);
            }
        }
    }

    @CallSuper
    protected void onStop() {
        if (mOnDismissListeners != null) {
            for (OnDismissListener listener : mOnDismissListeners) {
                listener.onDismiss(this);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Private
    ///////////////////////////////////////////////////////////////////////////

    private void createDialogIfNecessary() {
        if (mDialog == null) {
            mView = onCreateView(LayoutInflater.from(mContext));
            mDialog = onCreateDialog(mContext);
            mDialog.getLifecycle().addObserver((LifecycleEventObserver) (lifecycleOwner, event) -> {
                switch (event) {
                    // @formatter:off
                    case ON_CREATE: onDialogCreated(mDialog, mDialogOption, mView); break;
                    case ON_START:  onStart();                                      break;
                    case ON_STOP:   onStop();                                       break;
                    // @formatter:on
                }
            });
            mDialogOption = Optional.ofNullable(onCreateDialogOption()).orElse(new DialogOption.Builder().build());
            initWindow(mDialog.getWindow(), mDialogOption);
            initDialog(mDialog, mView, mDialogOption);
        }
    }

    private void initDialog(@NonNull AppCompatDialog dialog, @Nullable View view, @NonNull DialogOption opt) {
        // dismiss listener
        dialog.setOnDismissListener(d -> hideSoftInput());
        // back pressed
        dialog.setOnKeyListener((d, i, e) -> {
            if (e.getAction() == KeyEvent.ACTION_UP) {
                if (i == KeyEvent.KEYCODE_BACK || i == KeyEvent.KEYCODE_ESCAPE) {
                    if (onHandleBackPressed()) {
                        return true;
                    }
                    dismiss();
                    return true;
                }
            }
            return false;
        });
        // background
        Window window = dialog.getWindow();
        if (dialog instanceof BottomSheetDialog) {
            window.getDecorView().post(() -> {
                ViewParent parent = view.getParent();
                setBackground(parent instanceof View ? (View) parent : view, opt, false);
            });
        } else {
            setBackground(window.findViewById(Window.ID_ANDROID_CONTENT), opt, true);
        }
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
    }

    private static void setBackground(View view, DialogOption option, boolean roundedBottomCorners) {
        Optional.ofNullable(view).ifPresent(v -> {
            Context context = v.getContext();
            v.setClipToOutline(true);
            v.setBackground(option.shapeAppearanceModel != null
                    ? createPopupDrawable(context, option.backgroundColor, option.shapeAppearanceModel)
                    : createPopupDrawable(context, option.backgroundColor, option.cornerRadius, roundedBottomCorners));
        });
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

    ///////////////////////////////////////////////////////////////////////////
    // DialogOption
    ///////////////////////////////////////////////////////////////////////////

    public static class DialogOption {

        public static final int UNSET = 0;

        public int type;
        public int softInputMode;
        public int windowAnimation;
        public int cornerRadius;
        public int backgroundColor;
        public Rect backgroundInsets;
        public ShapeAppearanceModel shapeAppearanceModel;

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
