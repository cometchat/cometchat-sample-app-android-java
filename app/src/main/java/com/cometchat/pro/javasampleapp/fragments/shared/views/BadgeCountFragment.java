package com.cometchat.pro.javasampleapp.fragments.shared.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.views.CometChatBadge.CometChatBadge;
import com.cometchat.pro.javasampleapp.R;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class BadgeCountFragment extends Fragment {

    private int count = 1;
    private TextInputLayout badgeCountLayout;
    private TextInputEditText badgeCountEdt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_badge_count, container, false);
        CometChatBadge badgeCount = view.findViewById(R.id.badgeCount);
        badgeCountLayout = view.findViewById(R.id.badgeCountLayout);
        badgeCountEdt = view.findViewById(R.id.badgeCountEdt);

        badgeCountEdt.setText(String.valueOf(1));
        badgeCountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && charSequence.length() < 7) {
                    count = Integer.parseInt(charSequence.toString());
                    badgeCount.setCount(count);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        view.findViewById(R.id.bdRed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setBackground(getResources().getColor(R.color.red));
            }
        });
        view.findViewById(R.id.bdYellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setBackground(getResources().getColor(R.color.yellow));
            }
        });
        view.findViewById(R.id.bdPurple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setBackground(getResources().getColor(R.color.purple));
            }
        });
        view.findViewById(R.id.bdGreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setBackground(getResources().getColor(R.color.green));
            }
        });
        view.findViewById(R.id.bdBlue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setBackground(getResources().getColor(R.color.blue));
            }
        });
        view.findViewById(R.id.bdViolet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setBackground(getResources().getColor(R.color.violet));
            }
        });

        return view;
    }


}
