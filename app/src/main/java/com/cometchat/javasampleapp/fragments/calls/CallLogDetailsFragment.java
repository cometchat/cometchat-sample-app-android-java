package com.cometchat.javasampleapp.fragments.calls;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.calls.constants.CometChatCallsConstants;
import com.cometchat.calls.model.CallLog;
import com.cometchat.calls.model.CallUser;
import com.cometchat.calls.model.Participant;
import com.cometchat.calls.model.Recording;
import com.cometchat.chatuikit.calls.calldetails.CometChatCallLogDetails;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.javasampleapp.R;

import java.util.ArrayList;
import java.util.List;

public class CallLogDetailsFragment extends Fragment {
    private CometChatCallLogDetails cometChatCallLogDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_details, container, false);
        cometChatCallLogDetails = view.findViewById(R.id.call_logs_details);
        createCallLog();
        return view;
    }

    public void createCallLog() {
        if (CometChatUIKit.getLoggedInUser() != null) {
            CallUser initiator = new CallUser();
            initiator.setAvatar(CometChatUIKit.getLoggedInUser().getAvatar());
            initiator.setUid(CometChatUIKit.getLoggedInUser().getUid());
            initiator.setName(CometChatUIKit.getLoggedInUser().getName());

            CallUser receiver = new CallUser();
            receiver.setUid("UID233");
            receiver.setName("Kevin");
            receiver.setAvatar("https://data-us.cometchat.io/assets/images/avatars/spiderman.png");


            List<Participant> participants = new ArrayList<>();

            Participant participant = new Participant();
            participant.setUid(CometChatUIKit.getLoggedInUser().getUid());
            participant.setName(CometChatUIKit.getLoggedInUser().getName());
            participant.setAvatar(CometChatUIKit.getLoggedInUser().getAvatar());
            participant.setJoinedAt(1327349241);
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

            List<Recording> recordings = new ArrayList<>();

            Recording recording = new Recording();
            recording.setRecordingURL("https://recordings-us.cometchat.io/236497dcc2cd529b/2023-12-15/v1.us.236497dcc2cd529b.170264141733632a2e3171d8a5dcb1f82b743fbc2730422263_2023-12-15-11-57-16.mp4");
            recording.setDuration(1327349241);
            recording.setRid("RID2023");

            Recording recording1 = new Recording();
            recording1.setRecordingURL("https://recordings-us.cometchat.io/236497dcc2cd529b/2023-12-15/v1.us.236497dcc2cd529b.170264141733632a2e3171d8a5dcb1f82b743fbc2730422263_2023-12-15-11-57-16.mp4");
            recording1.setDuration(1327349241);
            recording1.setRid("RID2023-1");
            recordings.add(recording);
            recordings.add(recording1);

            CallLog callLog = new CallLog();
            callLog.setInitiatedAt(1327349241);
            callLog.setInitiator(initiator);
            callLog.setReceiver(receiver);
            callLog.setStatus(CometChatCallsConstants.CALL_STATUS_BUSY);
            callLog.setType(CometChatCallsConstants.CALL_TYPE_AUDIO);
            callLog.setParticipants(participants);
            callLog.setRecordings(recordings);
            callLog.setReceiverType(CometChatCallsConstants.RECEIVER_TYPE_USER);
            cometChatCallLogDetails.setCall(callLog);
        }
    }
}