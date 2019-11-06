package com.inscripts.cometchatpulse.demo.Pojo;

import android.graphics.drawable.Drawable;

public class Option {


    private String name;
    private Drawable drawable;
    private String id;

    public Option(String name, Drawable drawable, String id) {
        this.name = name;
        this.drawable = drawable;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

}
