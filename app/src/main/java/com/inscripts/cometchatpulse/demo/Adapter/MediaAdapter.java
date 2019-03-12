package com.inscripts.cometchatpulse.demo.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cometchat.pro.models.MediaMessage;
import com.inscripts.cometchatpulse.demo.R;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaHolder> {

    private List<MediaMessage> messageList;

    private Context context;

    public MediaAdapter(List<MediaMessage> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MediaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mediaView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.media_item, viewGroup, false);

        return new MediaHolder(mediaView);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaHolder mediaHolder, int i) {

        try {

            Glide.with(context).load(messageList.get(i).getUrl()).into(mediaHolder.imageView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MediaHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MediaHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
