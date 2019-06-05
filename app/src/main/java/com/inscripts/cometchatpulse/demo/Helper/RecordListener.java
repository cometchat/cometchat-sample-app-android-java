package com.inscripts.cometchatpulse.demo.Helper;

public interface RecordListener {
    void onStart();
    void onCancel();
    void onFinish(long time);
    void onLessTime();
}