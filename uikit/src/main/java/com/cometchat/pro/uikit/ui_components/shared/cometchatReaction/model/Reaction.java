package com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Reaction implements Parcelable {
    String name;
    int code;

    public Reaction(String name, int code) {
        this.name = name;
        this.code = code;
    }

    protected Reaction(Parcel in) {
        name = in.readString();
        code = in.readInt();
    }

    public static final Creator<Reaction> CREATOR = new Creator<Reaction>() {
        @Override
        public Reaction createFromParcel(Parcel in) {
            return new Reaction(in);
        }

        @Override
        public Reaction[] newArray(int size) {
            return new Reaction[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(code);
    }
}