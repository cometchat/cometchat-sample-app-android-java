package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.views.CometChatFileBubble.CometChatFileBubble;
import com.cometchat.chatuikit.shared.views.CometChatFileBubble.FileBubbleStyle;
import com.cometchat.javasampleapp.R;

public class FileBubbleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_bubble, container, false);

        CometChatFileBubble fileBubble = view.findViewById(R.id.file_bubble);
        fileBubble.setFileUrl("https://data-us.cometchat.io/2379614bd4db65dd/media/1682517934_233027292_069741a92a2f641eb428ba6d12ccb9af.pdf", "Sample", "pdf");
        fileBubble.setStyle(new FileBubbleStyle().setBackground(CometChatTheme.getInstance().getPalette().getAccent100(getContext())).setCornerRadius(18));
        fileBubble.getTitle().setPadding(20,20,20,10);
        fileBubble.getSubtitle().setPadding(20,10,2,10);
        return view;
    }
}