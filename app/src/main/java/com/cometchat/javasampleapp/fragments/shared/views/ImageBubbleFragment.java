package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.CometChatImageBubble;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.ImageBubbleStyle;
import com.cometchat.javasampleapp.R;

public class ImageBubbleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_bubble, container, false);

        CometChatImageBubble imageBubble = view.findViewById(R.id.image_bubble);
        imageBubble.setImageUrl("https://data-us.cometchat.io/2379614bd4db65dd/media/1682517838_2050398854_08d684e835e3c003f70f2478f937ed57.jpeg", R.drawable.ic_launcher_background, false);
        imageBubble.setStyle(new ImageBubbleStyle().setCornerRadius(18).setTextColor(Palette.getInstance().getAccent(getContext())).setBackground(Palette.getInstance().getBackground(getContext())));
        imageBubble.setCaption("This is a simple representation of CometChat Image Bubble");
        return view;
    }
}