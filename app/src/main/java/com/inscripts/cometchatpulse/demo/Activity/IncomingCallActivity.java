package com.inscripts.cometchatpulse.demo.Activity;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Contracts.IncomingCallActivityContract;
import com.inscripts.cometchatpulse.demo.CustomView.CallScreenButton;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.Helper.CCPermissionHelper;
import com.inscripts.cometchatpulse.demo.Helper.CameraPreview;
import com.inscripts.cometchatpulse.demo.Helper.CometChatAudioHelper;
import com.inscripts.cometchatpulse.demo.Helper.OutgoingAudioHelper;
import com.inscripts.cometchatpulse.demo.Presenters.IncomingCallActivityPresenter;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.AnimUtil;
import com.inscripts.cometchatpulse.demo.Utils.ColorUtils;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;

public class IncomingCallActivity extends AppCompatActivity implements
        IncomingCallActivityContract.IncomingCallActivityView, View.OnClickListener,
        CallScreenButton.AnswerDeclineListener {

    private static final int CAMERA_CODE = 1;

    private ImageView ivUserBackground;

    private CircleImageView ivUserPic;

    private TextView tvUserName;

    private FloatingActionButton fabHangUp;

    private CallScreenButton callScreenButton;

    private CallScreenButton.AnswerDeclineListener listener;

    private IncomingCallActivityContract.IncomingCallActivityPresenter incomingCallActivityPresenter;

    private FrameLayout cameraFrame;

    private CameraPreview cameraPreview;

    private Camera camera;

    private CometChatAudioHelper cometChatAudioHelper;

    String[] CAMERA_PERMISSION = {CCPermissionHelper.REQUEST_PERMISSION_CAMERA,
            CCPermissionHelper.REQUEST_PERMISSION_RECORD_AUDIO};

    private Uri notification;

    private boolean isVideoCall;

    private boolean isIncoming;

    private TextView tvDots;

    private TextView tvCallText;

    private String sessionId;

    private String contactUserId;

    private RelativeLayout mainView;

    private static final String TAG = "IncomingCallActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_call);

        new FontUtils(this);
        incomingCallActivityPresenter = new IncomingCallActivityPresenter();
        incomingCallActivityPresenter.attach(this);
        cometChatAudioHelper = new CometChatAudioHelper(this);
        cometChatAudioHelper.initAudio();
        initComponentView();

    }

    @Override
    public void setUserImage(String stringExtra) {

          if (stringExtra!=null) {
              incomingCallActivityPresenter.setImage(stringExtra, ivUserPic, ivUserBackground);
          }
          else {
              Drawable drawable = getResources().getDrawable(R.drawable.default_avatar);
              try {

                  ivUserPic.setCircleBackgroundColor(ColorUtils.getMaterialColor(this));
                  ivUserPic.setImageBitmap(MediaUtils.getPlaceholderImage(this, drawable));

              } catch (Exception e) {

                  ivUserPic.setCircleBackgroundColor(getResources().getColor(R.color.secondaryDarkColor));
                  ivUserPic.setImageDrawable(drawable);
              }
          }
    }

    @Override
    public void setUserName(String userName) {
        tvUserName.setText(userName);
    }

    @Override
    public void setCallType(boolean isVideoCall, boolean isIncoming) {

        this.isVideoCall = isVideoCall;
        this.isIncoming = isIncoming;
        AnimUtil.blinkAnimation(tvDots);
        callScreenButton.setAnswerDeclineListener(listener, isVideoCall);

        if (isVideoCall) {
            if (CCPermissionHelper.hasPermissions(this, CAMERA_PERMISSION)) {
                camera = MediaUtils.openFrontCam();
                cameraPreview = new CameraPreview(this, camera);
                cameraFrame.addView(cameraPreview);
            } else {
                CCPermissionHelper.requestPermissions(this, CAMERA_PERMISSION, CAMERA_CODE);
            }

        }


        if (isIncoming) {
            cometChatAudioHelper.startIncomingAudio(notification, true);
            callScreenButton.setVisibility(View.VISIBLE);
            callScreenButton.startAnimation();
            fabHangUp.setVisibility(View.INVISIBLE);
            tvCallText.setText(getString(R.string.incoming_call));

        } else {
            tvCallText.setText(getString(R.string.calling));
            cometChatAudioHelper.startOutgoingAudio(OutgoingAudioHelper.Type.IN_COMMUNICATION);
            callScreenButton.setVisibility(View.GONE);
            callScreenButton.stopAnimation();
            fabHangUp.setVisibility(View.VISIBLE);
            if (isVideoCall) {
                fabHangUp.setImageDrawable(getResources().getDrawable(R.drawable.ic_videocam_white_24dp));

            } else {
                fabHangUp.setImageDrawable(getResources().getDrawable(R.drawable.ic_call_end_white_24dp));
            }
        }

    }

    @Override
    public void setContactUserId(String contactUserId) {
        this.contactUserId = contactUserId;
    }

    @Override
    public void setSessionId(String sessionId) {
        this.sessionId=sessionId;
    }

    @Override
    public void handleCall(Call call) {

        cometChatAudioHelper.stop(true);
        callScreenButton.setVisibility(View.GONE);
        callScreenButton.stopAnimation();
        Toast.makeText(this, "Decline", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initComponentView() {
        ivUserBackground = findViewById(R.id.ivUserImageBackground);
        ivUserPic = findViewById(R.id.ivUserImage);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserName.setTypeface(FontUtils.openSansRegular);
        fabHangUp = findViewById(R.id.hangup);
        mainView=findViewById(R.id.main_view);
        tvCallText=findViewById(R.id.tv_callText);
        cameraFrame = findViewById(R.id.camera_preview);
        tvDots=findViewById(R.id.tv_dots);
        callScreenButton = findViewById(R.id.callScreenButton);
        fabHangUp.setOnClickListener(this);
        fabHangUp.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red_600)));
        listener = (CallScreenButton.AnswerDeclineListener) this;
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        incomingCallActivityPresenter.handleIntent(getIntent(), this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        cometChatAudioHelper.stop(true);

        callScreenButton.setVisibility(View.GONE);
        callScreenButton.stopAnimation();
        AnimUtil.stopBlinkAnimation(tvDots);
        incomingCallActivityPresenter.removerMessageListener(IncomingCallActivity.this,getString(R.string.message_listener));

        incomingCallActivityPresenter.detach();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
        incomingCallActivityPresenter.removeCallListener(TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isVideoCall) {
            camera = MediaUtils.openFrontCam();
            if (camera != null) {
                cameraPreview = new CameraPreview(this, camera);
                cameraFrame.addView(cameraPreview);
                ivUserBackground.setVisibility(View.GONE);
            }
        }
        incomingCallActivityPresenter.addCallEventListener(TAG);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.hangup:

                cometChatAudioHelper.stop(false);
                AnimUtil.stopBlinkAnimation(tvDots);
                incomingCallActivityPresenter.rejectCall(IncomingCallActivity.this,sessionId,CometChatConstants.CALL_STATUS_CANCELLED);
                Toast.makeText(this, "Decline", Toast.LENGTH_SHORT).show();


                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        incomingCallActivityPresenter.addMessageListener(mainView,IncomingCallActivity.this,
                getString(R.string.message_listener));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    camera = MediaUtils.openFrontCam();
                    cameraPreview = new CameraPreview(this, camera);
                    cameraFrame.addView(cameraPreview);
                    ivUserBackground.setVisibility(View.GONE);

                } else {
                    ivUserBackground.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onAnswered() {

        cometChatAudioHelper.stop(false);
        incomingCallActivityPresenter.answerCall(camera,mainView,IncomingCallActivity.this,sessionId);
        callScreenButton.setVisibility(View.GONE);
        callScreenButton.stopAnimation();
        fabHangUp.setVisibility(View.VISIBLE);
        AnimUtil.stopBlinkAnimation(tvDots);

        Toast.makeText(this, "Answer", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclined() {
        incomingCallActivityPresenter.rejectCall(IncomingCallActivity.this,sessionId,CometChatConstants.CALL_STATUS_REJECTED);
        cometChatAudioHelper.stop(true);
        AnimUtil.stopBlinkAnimation(tvDots);
        callScreenButton.setVisibility(View.GONE);
        callScreenButton.stopAnimation();
        Toast.makeText(this, "Decline", Toast.LENGTH_SHORT).show();
        finish();
    }
}
