package com.cometchat.javasampleapp.fragments.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.details.CometChatDetails;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.models.CometChatDetailsOption;
import com.cometchat.chatuikit.shared.models.CometChatDetailsTemplate;
import com.cometchat.chatuikit.shared.utils.DetailsUtils;
import com.cometchat.chat.core.CometChat;
import com.cometchat.javasampleapp.R;

import java.util.List;

public class UsersDetailsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list_data_item, container, false);
        CometChatDetails cometChatDetails = view.findViewById(R.id.users_details);
        cometChatDetails.setUser(CometChatUIKit.getLoggedInUser());
        return view;
    }
}