package com.cometchat.javasampleapp.fragments.shared.views;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.utils.ConversationTailView;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatBadge.BadgeStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.DateStyle;
import com.cometchat.chatuikit.shared.views.CometChatDate.Pattern;
import com.cometchat.chatuikit.shared.views.CometChatListItem.CometChatListItem;
import com.cometchat.chat.core.CometChat;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;


public class ListItemFragment extends Fragment {
    private LinearLayout parentView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_item, container, false);
        parentView=view.findViewById(R.id.parent_view);
        CometChatTheme theme = CometChatTheme.getInstance();
        CometChatListItem groupListItem = view.findViewById(R.id.group_list_item);
        groupListItem.setTitle("Superhero");
        groupListItem.setTitleColor(Palette.getInstance().getAccent(getContext()));
        groupListItem.setSubtitleView(getTextView("8 members"));
        groupListItem.setAvatar("https://data-us.cometchat.io/2379614bd4db65dd/media/1682517838_2050398854_08d684e835e3c003f70f2478f937ed57.jpeg", "Superhero");
        groupListItem.hideStatusIndicator(true);

        String name = CometChatUIKit.getLoggedInUser().getName();
        CometChatListItem userListItem = view.findViewById(R.id.user_list_Item);
        userListItem.setAvatar(CometChatUIKit.getLoggedInUser().getAvatar(), name);
        userListItem.setSubtitleView(getTextView(CometChatUIKit.getLoggedInUser().getStatus()));
        userListItem.setTitle(name);
        userListItem.setTitleColor(Palette.getInstance().getAccent(getContext()));
        userListItem.setStatusIndicatorColor(getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_online_green));

        CometChatListItem conversationListItem = view.findViewById(R.id.conversation_list_item);
        ConversationTailView tailView = new ConversationTailView(getContext());
        tailView.setBadgeCount(100);
        tailView.getBadge().setStyle(new BadgeStyle().setTextColor(theme.getPalette().getAccent(getContext())).setBackground(theme.getPalette().getPrimary(getContext())).setCornerRadius(100));
        tailView.getDate().setDate(System.currentTimeMillis() / 1000, Pattern.DAY_DATE_TIME);
        tailView.getDate().setStyle(new DateStyle().setTextAppearance(theme.getTypography().getSubtitle1()).setTextColor(theme.getPalette().getAccent600(getContext())));
        conversationListItem.setTitle(name);
        conversationListItem.setTitleColor(Palette.getInstance().getAccent(getContext()));
        conversationListItem.setAvatar(CometChatUIKit.getLoggedInUser().getAvatar(), name);
        conversationListItem.setTailView(tailView);
        conversationListItem.setSubtitleView(getTextView("Hey, How are you?"));
        conversationListItem.setStatusIndicatorColor(getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_online_green));
        setUpUI(view);
        return view;
    }

    private View getTextView(String name) {
        TextView textView = new TextView(getContext());
        textView.setText(name);
        return textView;
    }

    private void setUpUI(View view) {
        if (AppUtils.isNightMode(getContext())) {
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.conversation));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.group));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.user));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.list_item));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.list_item_desc));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background_dark)));
        } else {
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.conversation));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.group));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.user));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.list_item));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }
}