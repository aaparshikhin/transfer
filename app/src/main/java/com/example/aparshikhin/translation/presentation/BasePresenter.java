package com.example.aparshikhin.translation.presentation;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;

public abstract class BasePresenter<View extends MvpView> extends MvpPresenter<View> {

    @NonNull
    private final PresenterLifecycleScopeProvider mScopeProvider;

    protected BasePresenter() {
        mScopeProvider = new PresenterLifecycleScopeProvider();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        mScopeProvider.onCreate();
    }

    @NonNull
    protected <T> AutoDisposeConverter<T> autoDisposable() {
        return AutoDispose.autoDisposable(mScopeProvider);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mScopeProvider.onDestroy();
    }
}
