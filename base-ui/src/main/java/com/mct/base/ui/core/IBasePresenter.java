package com.mct.base.ui.core;

public interface IBasePresenter<V extends IBaseView> {

    void attachView(V view);

    void detachView();

    boolean isViewAttached();

    V getView();

}
