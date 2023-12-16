package com.cometchat.javasampleapp.fragments.calls;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.calls.model.Participant;
import com.cometchat.chatuikit.calls.callparticipants.CometChatCallLogParticipants;
import com.cometchat.chatuikit.calls.callrecordings.CometChatCallLogRecordings;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.javasampleapp.R;

import java.util.ArrayList;
import java.util.List;

public class CallLogParticipantsFragment extends Fragment {
    private CometChatCallLogParticipants cometChatCallLogParticipants;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_log_participants, container, false);
        cometChatCallLogParticipants = view.findViewById(R.id.call_logs_participants);
        createCallParticipants();
        return view;
    }

    private void createCallParticipants() {
        List<Participant> participants = new ArrayList<>();

        Participant participant = new Participant();
        participant.setUid(CometChatUIKit.getLoggedInUser().getUid());
        participant.setName(CometChatUIKit.getLoggedInUser().getName());
        participant.setAvatar(CometChatUIKit.getLoggedInUser().getAvatar());
        participant.setJoinedAt(1327349241);
        participant.setHasJoined(true);
        participant.setTotalDurationInMinutes(1327349241);

        Participant participant1 = new Participant();
        participant1.setUid("UID233");
        participant1.setName("Kevin");
        participant1.setAvatar("https://data-us.cometchat.io/assets/images/avatars/spiderman.png");
        participant1.setJoinedAt(1327349241);
        participant1.setHasJoined(true);
        participant1.setTotalDurationInMinutes(1327349241);

        participants.add(participant);
        participants.add(participant1);
        cometChatCallLogParticipants.setParticipantList(participants);
    }


}