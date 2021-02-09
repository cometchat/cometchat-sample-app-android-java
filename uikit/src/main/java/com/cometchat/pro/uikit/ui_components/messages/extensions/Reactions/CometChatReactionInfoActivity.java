package com.cometchat.pro.uikit.ui_components.messages.extensions.Reactions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cometchat.pro.uikit.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import com.cometchat.pro.uikit.ui_components.messages.extensions.Extensions;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;

public class CometChatReactionInfoActivity extends AppCompatActivity {

    private LinearLayout reactionInfoLayout;

    private JSONObject jsonObject;

    private ImageView closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_reaction_info);
        reactionInfoLayout = findViewById(R.id.reaction_info_layout);
        closeBtn = findViewById(R.id.close_btn);
        closeBtn.setOnClickListener(view -> {
            onBackPressed();
        });
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.REACTION_INFO)) {
            try {
                jsonObject = new JSONObject(getIntent().getStringExtra(UIKitConstants.IntentStrings.REACTION_INFO));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        HashMap<String, List<String>> reactionInfo = Extensions.getReactionsInfo(jsonObject);
        for (String str : reactionInfo.keySet()) {
            View view = LayoutInflater.from(this).inflate(R.layout.reaction_info_row,null);
            TextView react = view.findViewById(R.id.react_tv);
            TextView users = view.findViewById(R.id.users_tv);
            react.setText(str);
            List<String> usernames = reactionInfo.get(str);
            for (String uname : usernames) {
                if (users.getText().toString().trim().isEmpty())
                    users.setText(uname);
                else
                    users.setText(users.getText().toString()+","+uname);
            }
            reactionInfoLayout.addView(view);
        }
    }
}
