package com.cometchat.javasampleapp.fragments.calls;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.calls.model.Recording;
import com.cometchat.chatuikit.calls.callrecordings.CometChatCallLogRecordings;
import com.cometchat.javasampleapp.R;

import java.util.ArrayList;
import java.util.List;


public class CallLogRecordingFragment extends Fragment {

    private CometChatCallLogRecordings cometChatCallLogRecordings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_log_recording, container, false);
        cometChatCallLogRecordings = view.findViewById(R.id.call_logs_recordings);
        createCallRecordings();
        return view;
    }

    private void createCallRecordings() {
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
        cometChatCallLogRecordings.setRecordingList(recordings);
    }
}