package com.mct.base.ui.abs;

public interface IBaseFragment {

    IKeyboardManager keyboardManager();

    IExtraTransaction extraTransaction();

    IExtraTransaction childExtraTransaction();

    void post(Runnable runnable);

    void postDelay(Runnable runnable, long delay);

    void removeCallbacks(Runnable runnable);

    boolean onBackPressed();

}
