package com.inscripts.cometchatpulse.demo.ViewHolders;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.inscripts.cometchatpulse.demo.R;

import static android.content.Context.WINDOW_SERVICE;

public class RightAudioViewHolder extends RecyclerView.ViewHolder{
    public TextView messageTimeStamp,audioLength;
    public View audioContainer;
    public ImageView playAudio,messageStatus;
    public Guideline rightGuideLine;
    public ProgressBar fileLoadingProgressBar;
    public SeekBar audioSeekBar;
    public RightAudioViewHolder(Context context, View rightAudioMessageView) {
        super(rightAudioMessageView);
        messageStatus = rightAudioMessageView.findViewById(R.id.messageStatus);
        audioContainer = rightAudioMessageView.findViewById(R.id.fileContainer);
        playAudio = rightAudioMessageView.findViewById(R.id.playButton);
        messageTimeStamp = rightAudioMessageView.findViewById(R.id.timeStamp);
        rightGuideLine = rightAudioMessageView.findViewById(R.id.rightGuideline);
        fileLoadingProgressBar = rightAudioMessageView.findViewById(R.id.fileName);
        audioLength = rightAudioMessageView.findViewById(R.id.audioLength);
        audioSeekBar = rightAudioMessageView.findViewById(R.id.audioSeekBar);
        Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        if(orientation == 1 || orientation == 3){
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) rightGuideLine.getLayoutParams();
            params.guidePercent = 0.5f;
            rightGuideLine.setLayoutParams(params);
        }
    }
}
