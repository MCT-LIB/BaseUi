package com.mct.base.ui.core;

public interface IBaseActivity {

    IKeyboardManager keyboardManager();

    IExtraTransaction extraTransaction();

    void post(Runnable runnable);

    void postDelay(Runnable runnable, long delay);

    void removeCallbacks(Runnable runnable);

}
