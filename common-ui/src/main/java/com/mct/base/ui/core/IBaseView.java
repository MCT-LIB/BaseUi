package com.mct.base.ui.core;

public interface IBaseView {

    void showLoading();

    void hideLoading();

    void showError(Throwable t);

}
