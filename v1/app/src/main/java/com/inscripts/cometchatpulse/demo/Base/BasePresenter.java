package com.inscripts.cometchatpulse.demo.Base;

public interface BasePresenter<V> {

    void attach(V baseView);

    void detach();


}
