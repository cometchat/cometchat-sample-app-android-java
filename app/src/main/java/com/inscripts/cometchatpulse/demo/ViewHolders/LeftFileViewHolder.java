package com.inscripts.cometchatpulse.demo.ViewHolders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.Logger;

import static android.content.Context.WINDOW_SERVICE;

public class LeftFileViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "LeftFileViewHolder";
    public  TextView fileName;

    public TextView fileType;

    public TextView messageTimeStamp,senderName;
    public CircleImageView avatar;
    public View fileContainer;
    public Guideline leftGuideLine;


    public LeftFileViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        Display display = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        Logger.error(TAG, "LeftImageVideoViewHolder: orientation: "+orientation);
        leftGuideLine = itemView.findViewById(R.id.leftGuideline);
        if(orientation == 1 || orientation == 3){
            Logger.error(TAG, "LeftImageVideoViewHolder: Landscape Mode");
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) leftGuideLine.getLayoutParams();
            params.guidePercent = 0.5f;
            leftGuideLine.setLayoutParams(params);
        }
        fileType=itemView.findViewById(R.id.fileType);
        fileName=itemView.findViewById(R.id.fileName);
        messageTimeStamp = itemView.findViewById(R.id.timeStamp);
        avatar = itemView.findViewById(R.id.imgAvatar);
        fileContainer = itemView.findViewById(R.id.fileContainer);
        senderName = itemView.findViewById(R.id.senderName);

    }
}
