package com.inscripts.cometchatpulse.demo.ViewHolders;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.Utils.Logger;


import static android.content.Context.WINDOW_SERVICE;

public class RightImageVideoViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = RightImageVideoViewHolder.class.getSimpleName();
    public TextView messageTimeStamp,imageTitle;
    public ImageView imageMessage;
    public CircleImageView messageStatus;
    public View imageContainer;
    public ImageButton btnPlayVideo;
    public Guideline rightGuideLine;
    public ProgressBar fileLoadingProgressBar;
    public RightImageVideoViewHolder(Context context, View rightImageMessageView) {
        super(rightImageMessageView);
        Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        Logger.error(TAG, "RightImageVideoViewHolder: orientation: "+orientation);
        rightGuideLine = rightImageMessageView.findViewById(R.id.rightGuideline);
        if(orientation == 1 || orientation == 3){
            Logger.error(TAG, "RightImageVideoViewHolder: Landscape Mode");
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) rightGuideLine.getLayoutParams();
            params.guidePercent = 0.5f;
            rightGuideLine.setLayoutParams(params);
        }
        messageTimeStamp = rightImageMessageView.findViewById(R.id.timeStamp);
        imageTitle = rightImageMessageView.findViewById(R.id.imageTitle);
        imageMessage = rightImageMessageView.findViewById(R.id.imageMessage);
        imageContainer = rightImageMessageView.findViewById(R.id.imageContainer);
        btnPlayVideo = rightImageMessageView.findViewById(R.id.btnPlayVideo);
        messageStatus = rightImageMessageView.findViewById(R.id.messageStatus);
        fileLoadingProgressBar = rightImageMessageView.findViewById(R.id.fileName);
    }
}

