package com.mct.base.ui.abs;

public interface IBaseView {

    void showLoading();

    void hideLoading();

    void onError(Throwable t);

}
