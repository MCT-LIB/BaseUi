package com.mct.base.ui.core;

public interface IBaseActivity {

    IKeyboardManager keyboardManager();

    IExtraTransaction extraTransaction();

    void post(Runnable runnable);

    void postDelayed(Runnable runnable, long delay);

    void removeCallbacks(Runnable runnable);

    void requestBackPress();

}
