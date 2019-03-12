package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.inscripts.cometchatpulse.demo.Activity.CallActivity;
import com.inscripts.cometchatpulse.demo.Activity.IncomingCallActivity;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.IncomingCallActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Helper.BlurTransformation;
import com.inscripts.cometchatpulse.demo.R;

public class IncomingCallActivityPresenter extends Presenter<IncomingCallActivityContract.IncomingCallActivityView>
        implements IncomingCallActivityContract.IncomingCallActivityPresenter {


    private Context context;

    @Override
    public void handleIntent(Intent intent, Context context) {
        this.context = context;

        if (intent.hasExtra(StringContract.IntentStrings.ID)) {
            getBaseView().setContactUserId(intent.getStringExtra(StringContract.IntentStrings.ID));
        }

        if (intent.hasExtra(StringContract.IntentStrings.SESSION_ID)) {
            getBaseView().setSessionId(intent.getStringExtra(StringContract.IntentStrings.SESSION_ID));
        }
        if (intent.hasExtra(StringContract.IntentStrings.AVATAR)) {
            getBaseView().setUserImage(intent.getStringExtra(StringContract.IntentStrings.AVATAR));
        }

        if (intent.hasExtra(StringContract.IntentStrings.NAME)) {
            getBaseView().setUserName(intent.getStringExtra(StringContract.IntentStrings.NAME));
        }

        try {
            boolean isVideo = intent.getAction().equals(CometChatConstants.CALL_TYPE_VIDEO);

            boolean isIncoming = intent.getType().equals(StringContract.IntentStrings.INCOMING);

            getBaseView().setCallType(isVideo, isIncoming);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setImage(String stringExtra, ImageView ivUserPic, ImageView ivUserBackground) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.chat_pattern);

        Glide.with(context).load(bitmap).apply(RequestOptions.bitmapTransform(new BlurTransformation(context)))
                .into(ivUserBackground);

        setContactImage(ivUserPic, stringExtra);


    }



    @Override
    public void rejectCall(Context context, String sessionId,String callStatus) {

             CometChat.rejectCall(sessionId,callStatus, new CometChat.CallbackListener<Call>() {
                 @Override
                 public void onSuccess(Call call) {

                     ((IncomingCallActivity)context).finish();
                 }

                 @Override
                 public void onError(CometChatException e) {

                 }
             });
    }

    @Override
    public void answerCall(final Camera camera, final RelativeLayout mainView, final Context context, String sessionId) {

        CometChat.acceptCall(sessionId, new CometChat.CallbackListener<Call>() {
            @Override
            public void onSuccess(Call call) {

                Intent callIntent = new Intent(context, CallActivity.class);
                callIntent.putExtra(StringContract.IntentStrings.SESSION_ID, call.getSessionId());
                context.startActivity(callIntent);
                ((IncomingCallActivity) context).finish();
            }

            @Override
            public void onError(CometChatException e) {

            }

        });
//
    }

    @Override
    public void removerMessageListener(Context context, String listenerId) {
        CometChat.removeMessageListener(listenerId);
    }

    @Override
    public void addMessageListener(final RelativeLayout view, final Context context, String listenerId) {

    }

    @Override
    public void addCallEventListener(String listener) {

        CometChat.addCallListener(listener, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {

            }

            @Override
            public void onOutgoingCallAccepted(Call call) {

                Intent callIntent = new Intent(context, CallActivity.class);
                callIntent.putExtra(StringContract.IntentStrings.SESSION_ID, call.getSessionId());
                context.startActivity(callIntent);
                ((IncomingCallActivity) context).finish();
            }

            @Override
            public void onOutgoingCallRejected(Call call) {
                ((IncomingCallActivity) context).finish();
            }

            @Override
            public void onIncomingCallCancelled(Call call) {
                ((IncomingCallActivity) context).finish();
            }

        });
    }

    @Override
    public void removeCallListener(String listenerId) {
        CometChat.removeCallListener(listenerId);
    }

    private void setContactImage(ImageView ivUserPic, String stringExtra) {
         if (stringExtra!=null) {
             Glide.with(context).load(stringExtra).into(ivUserPic);
         }else {

         }
    }
}
