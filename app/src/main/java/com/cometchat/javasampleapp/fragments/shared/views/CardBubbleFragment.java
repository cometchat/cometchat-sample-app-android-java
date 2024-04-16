package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.interactiveactions.APIAction;
import com.cometchat.chatuikit.shared.models.interactiveactions.URLNavigationAction;
import com.cometchat.chatuikit.shared.models.interactiveelements.BaseInteractiveElement;
import com.cometchat.chatuikit.shared.models.interactiveelements.ButtonElement;
import com.cometchat.chatuikit.shared.models.interactivemessage.CardMessage;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.views.CometChatCardBubble.CardBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatCardBubble.CometChatCardBubble;
import com.cometchat.chatuikit.shared.views.CometChatImageBubble.ImageBubbleStyle;
import com.cometchat.javasampleapp.AppConstants;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CardBubbleFragment extends Fragment {

    private LinearLayout parentLayout;
    private CometChatCardBubble cardBubble;
    private ScrollView scrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card_bubble, container, false);
        parentLayout = view.findViewById(R.id.parent_layout);
        cardBubble = view.findViewById(R.id.card_bubble);
        scrollView = view.findViewById(R.id.scroll_view);
        scrollView.setVerticalScrollBarEnabled(false);
        CometChatTheme theme = CometChatTheme.getInstance();

        //create style object for card bubble
        CardBubbleStyle cardBubbleStyle = new CardBubbleStyle()
                .setTextAppearance(theme.getTypography().getText1())
                .setTextColor(theme.getPalette().getAccent(getContext()))
                .setContentBackgroundColor(theme.getPalette().getBackground(getContext()))
                .setCornerRadius(16)
                .setProgressBarTintColor(theme.getPalette().getPrimary(getContext()))
                .setButtonSeparatorColor(theme.getPalette().getAccent100(getContext()))
                .setButtonBackgroundColor(theme.getPalette().getBackground(getContext()))
                .setButtonTextColor(theme.getPalette().getPrimary(getContext()))
                .setButtonDisableTextColor(theme.getPalette().getAccent500(getContext()))
                .setButtonTextAppearance(theme.getTypography().getSubtitle1())
                .setBackground(theme.getPalette().getBackground(getContext()))
                .setBackground(theme.getPalette().getGradientBackground())
                .setImageBubbleStyle(new ImageBubbleStyle()
                        .setCornerRadius(16)
                );

        cardBubble.setStyle(cardBubbleStyle);

        //create list of interactive elements
        List<BaseInteractiveElement> elementEntities = new ArrayList<>();

        URLNavigationAction urlNavigationAction = new URLNavigationAction("https://www.cometchat.com/");

        ButtonElement buttonElement1 = new ButtonElement("element1", "Navigate", urlNavigationAction);
        buttonElement1.setDisableAfterInteracted(true);
        elementEntities.add(buttonElement1);

        CardMessage cardMessage = new CardMessage(AppUtils.getDefaultUser() != null ? AppUtils.getDefaultUser().getUid() : AppUtils.getDefaultGroup() != null ? AppUtils.getDefaultGroup().getGuid() : null, AppUtils.getDefaultUser() != null ? UIKitConstants.ReceiverType.USER : UIKitConstants.ReceiverType.GROUP, null, elementEntities);
        cardMessage.setText("\uD83C\uDF1F Introducing our New Personalized Card Messages! \uD83C\uDF1F\n" + "\n" + "Want to make your gifts more special? Now it's easy with our personalized card messages! \uD83D\uDCAC✍️\n" + "\n" + "Our new feature lets you add a custom message on a beautifully designed card, making your gift-giving extra personal and memorable. Whether it's for a birthday \uD83C\uDF82, anniversary \uD83D\uDC8D, or just because \uD83C\uDF81, our card messages will express your feelings perfectly.\n" + "\n" + "To start creating your own card message:\n 1️⃣ Choose the gift \n2️⃣ Write your heartfelt message \n3️⃣ We'll print it on a high-quality card and include it with your gift\n" + "\n" + "\uD83D\uDCAB Add a touch of your own sentiments with our personalized card messages. Make every gift unforgettable. Start creating your card message today!\n" + "\n" + "Visit our website [Website Link] or download our app [App Link].\n" + "\n" + "Express more than just words with our Personalized Card Messages. Because it's not just a gift, it's your feelings. ❤️");
        cardMessage.setImageUrl("https://images.unsplash.com/photo-1608755728617-aefab37d2edd?q=80&w=3270&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
        cardMessage.setAllowSenderInteraction(true); // sender can interact with the card message

        cardMessage.setSender(CometChatUIKit.getLoggedInUser());
        cardMessage.setSentAt(System.currentTimeMillis() / 1000);
        cardMessage.setReceiver(AppUtils.getDefaultUser() != null ? AppUtils.getDefaultUser() : AppUtils.getDefaultGroup() != null ? AppUtils.getDefaultGroup() : null);

        cardBubble.setCardMessage(cardMessage);

        return view;
    }
}