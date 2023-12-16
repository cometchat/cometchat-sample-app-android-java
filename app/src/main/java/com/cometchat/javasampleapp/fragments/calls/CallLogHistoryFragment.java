package com.cometchat.javasampleapp.fragments.calls;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.chatuikit.calls.callhistory.CometChatCallLogHistory;
import com.cometchat.javasampleapp.R;


public class CallLogHistoryFragment extends Fragment {
    private CometChatCallLogHistory cometChatCallLogHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_log_history, container, false);

        cometChatCallLogHistory = view.findViewById(R.id.call_logs_history);
        return view;
    }
}