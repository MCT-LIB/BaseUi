package com.mct.base.ui.core;

import com.mct.base.ui.BaseFragment;

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
    IExtraTransaction parentExtraTransaction(Class<? extends BaseFragment> parent);

    void post(Runnable runnable);

    void postDelayed(Runnable runnable, long delay);

    void removeCallbacks(Runnable runnable);

    boolean onBackPressed();

}
