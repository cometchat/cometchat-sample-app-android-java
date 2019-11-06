package com.inscripts.cometchatpulse.demo.Base;

public class Presenter<V> implements BasePresenter<V> {

    private V baseView;

    @Override
    public void attach(V baseView) {

        this.baseView=baseView;

    }

    @Override
    public void detach() {
        baseView=null;
    }

    public boolean isViewAttached() {
        return baseView != null;
    }

    public V getBaseView()
    {
        return baseView;
    }
}
