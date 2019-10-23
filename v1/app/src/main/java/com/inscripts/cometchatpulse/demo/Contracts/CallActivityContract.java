package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Context;

import com.inscripts.cometchatpulse.demo.Base.BasePresenter;

public interface CallActivityContract {

    interface CallActivityView{

    }

    interface CallActivityPresenter extends BasePresenter<CallActivityView>{

        void removeCallListener(String listener);

        void addCallListener(Context context,String listener);

    }
}
