package com.inscripts.cometchatpulse.demo.CustomView;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

public interface StickyHeaderAdapter<T extends RecyclerView.ViewHolder> {

    long getHeaderId(int var1);

    T onCreateHeaderViewHolder(ViewGroup var1);

    void onBindHeaderViewHolder(T var1, int var2, long var3);
}