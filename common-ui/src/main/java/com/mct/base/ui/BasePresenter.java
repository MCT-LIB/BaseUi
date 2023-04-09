package com.mct.base.ui;

import com.mct.base.ui.core.IBasePresenter;
import com.mct.base.ui.core.IBaseView;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    private WeakReference<V> mView;

    @Override
    public void attachView(V view) {
        mView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        mView.clear();
        mView = null;
    }

    @Override
    public boolean isViewAttached() {
        return mView != null && mView.get() != null;
    }

    @Override
    public V getView() {
        return mView.get();
    }

    public void showLoading() {
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

    public void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    public void showError(Throwable throwable) {
        if (isViewAttached()) {
            getView().showError(throwable);
        }
    }

}
