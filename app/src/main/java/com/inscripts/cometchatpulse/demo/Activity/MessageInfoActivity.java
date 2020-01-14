package com.inscripts.cometchatpulse.demo.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.MessageReceipt;
import com.inscripts.cometchatpulse.demo.Adapter.MessageReceiptsAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.MessageInfoActivityContract;
import com.inscripts.cometchatpulse.demo.Presenters.MessageInfoActivityPresenter;
import com.inscripts.cometchatpulse.demo.R;

import java.util.List;

public class MessageInfoActivity extends AppCompatActivity implements MessageInfoActivityContract.MessageInfoActivityView {

    private TextView tvMessage;

    private ImageView ivImageView;

    private View rlAudio;

    private ImageButton imageButton;

    private View rlText;

    private View rlImage;

    private String type;

    private String Category;

    private String text;

    private String url;

    private RecyclerView rvMessageReceipts;

    private ProgressBar progressBar;

    private LinearLayoutManager linearLayoutManager;

    private MessageReceiptsAdapter messageReceiptsAdapter;

    private MessageInfoActivityContract.MessageInfoActivityPresenter messageInfoActivityPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_messageinfo);
        messageInfoActivityPresenter=new MessageInfoActivityPresenter();
        messageInfoActivityPresenter.attach(this);
        initView();
    }

    private void initView() {
        tvMessage=findViewById(R.id.textViewMessage);
        rvMessageReceipts=findViewById(R.id.rvReceipts);
        linearLayoutManager=new LinearLayoutManager(this);
        rvMessageReceipts.setLayoutManager(linearLayoutManager);
        ivImageView=findViewById(R.id.imageMessage);
        progressBar=findViewById(R.id.fileName);
        progressBar.setVisibility(View.GONE);
        imageButton=findViewById(R.id.btnPlayVideo);
        rlAudio=findViewById(R.id.audioMessage);
        rlImage=findViewById(R.id.ivimageMessage);
        rlText=findViewById(R.id.textMessage);

        messageInfoActivityPresenter.getIntent(this,getIntent());
    }

    @Override
    public void setType(String url, String type) {

        switch (type) {
            case CometChatConstants.MESSAGE_TYPE_TEXT:
                tvMessage.setText(url);
                rlText.setVisibility(View.VISIBLE);
                rlImage.setVisibility(View.GONE);
                rlAudio.setVisibility(View.GONE);
                break;
            case CometChatConstants.MESSAGE_TYPE_AUDIO:
                rlAudio.setVisibility(View.VISIBLE);
                rlText.setVisibility(View.GONE);
                rlImage.setVisibility(View.GONE);
                break;
            case CometChatConstants.MESSAGE_TYPE_VIDEO:
                rlAudio.setVisibility(View.GONE);
                rlText.setVisibility(View.GONE);
                rlImage.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.VISIBLE);
                Glide.with(this).load(url).into(ivImageView);
                break;
            case CometChatConstants.MESSAGE_TYPE_IMAGE:
                rlAudio.setVisibility(View.GONE);
                rlText.setVisibility(View.GONE);
                rlImage.setVisibility(View.VISIBLE);
                imageButton.setVisibility(View.GONE);
                Glide.with(this).load(url).into(ivImageView);
                break;
        }
    }

    @Override
    public void setSenderUID(String senderUID) {

    }

    @Override
    public void receiverUID(String receiverUID) {

    }

    @Override
    public void setReceiptsAdapter(List<MessageReceipt> messageReceipts) {
        runOnUiThread(() -> {
            if (messageReceipts!=null) {
                messageReceiptsAdapter = new MessageReceiptsAdapter(MessageInfoActivity.this, messageReceipts);
                rvMessageReceipts.setAdapter(messageReceiptsAdapter);
            }
        });
    }
}
