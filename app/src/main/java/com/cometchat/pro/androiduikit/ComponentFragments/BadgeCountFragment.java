package com.cometchat.pro.androiduikit.ComponentFragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.uikit.BadgeCount;
import com.cometchat.pro.uikit.StatusIndicator;
import com.google.android.material.textfield.TextInputEditText;

public class BadgeCountFragment extends Fragment {

    private int count=1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_badge_count, container, false);
        BadgeCount badgeCount = view.findViewById(R.id.badgeCount);
        TextInputEditText badgecountedt = view.findViewById(R.id.badgeCount_edt);
        TextInputEditText countSize = view.findViewById(R.id.countSize);
        countSize.setText(String.valueOf(12));
        badgecountedt.setText(String.valueOf(1));
        badgecountedt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0 && charSequence.length()<7) {
                    count = Integer.parseInt(charSequence.toString());
                    badgeCount.setCount(Integer.parseInt(charSequence.toString()));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        countSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0&&Integer.parseInt(String.valueOf(charSequence))<32)
                    badgeCount.setCountSize(Float.parseFloat(charSequence.toString()));
                else if (charSequence.length()==0){
                    badgeCount.setCountSize(12f);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        view.findViewById(R.id.bd_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountBackground(getResources().getColor(R.color.red));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.bd_yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountBackground(getResources().getColor(R.color.yellow));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.bd_purple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountBackground(getResources().getColor(R.color.purple));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.bd_green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountBackground(getResources().getColor(R.color.green));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.bd_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountBackground(getResources().getColor(R.color.blue));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.bd_violet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountBackground(getResources().getColor(R.color.violet));
                refreshbadgeCount(badgeCount);
            }
        });

        /**/
        view.findViewById(R.id.count_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountColor(getResources().getColor(R.color.red));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.count_yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountColor(getResources().getColor(R.color.yellow));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.count_purple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountColor(getResources().getColor(R.color.purple));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.count_green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountColor(getResources().getColor(R.color.green));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.count_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountColor(getResources().getColor(R.color.blue));
                refreshbadgeCount(badgeCount);
            }
        });
        view.findViewById(R.id.count_violet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                badgeCount.setCountColor(getResources().getColor(R.color.violet));
                refreshbadgeCount(badgeCount);
            }
        });
        return view;
    }

    private void refreshbadgeCount(BadgeCount badgeCount) {
        badgeCount.setCount(count);
    }
}
