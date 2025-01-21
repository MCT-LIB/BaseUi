package com.mct.base.ui.core;

import android.view.View;

public interface IBaseFragment {

    IKeyboardManager keyboardManager();

    /**
     * @return transaction of activity
     */
    IExtraTransaction extraTransaction();

    /**
     * @return transaction of current fragment
     */
    IExtraTransaction childExtraTransaction();

    /**
     * @return transaction of parent fragment if have
     */
    IExtraTransaction parentExtraTransaction();

    /**
     * @return transaction of parent fragment if have (recursive)
     */
    IExtraTransaction parentExtraTransaction(Class<? extends IBaseFragment> parent);

    View getParentView();

    void pendingPreventAnimation();

    void removePendingPreventAnimation();

    void post(Runnable runnable);

    void postDelayed(Runnable runnable, long delay);

    void removeCallbacks(Runnable runnable);

    void requestBackPress();

    boolean handleOnBackPressed();

}
