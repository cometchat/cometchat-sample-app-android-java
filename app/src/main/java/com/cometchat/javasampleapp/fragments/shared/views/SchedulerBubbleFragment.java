package com.cometchat.javasampleapp.fragments.shared.views;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.interactiveactions.APIAction;
import com.cometchat.chatuikit.shared.models.interactiveelements.ButtonElement;
import com.cometchat.chatuikit.shared.models.interactivemessage.SchedulerMessage;
import com.cometchat.chatuikit.shared.models.interactivemessage.TimeRange;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.AvatarStyle;
import com.cometchat.chatuikit.shared.views.CometChatQuickView.QuickViewStyle;
import com.cometchat.chatuikit.shared.views.CometChatSchedulerBubble.CometChatSchedulerBubble;
import com.cometchat.chatuikit.shared.views.CometChatSchedulerBubble.ScheduleStyle;
import com.cometchat.chatuikit.shared.views.CometChatSchedulerBubble.SchedulerBubbleStyle;
import com.cometchat.chatuikit.shared.views.calender.CalenderStyle;
import com.cometchat.chatuikit.shared.views.timeslotitem.TimeSlotItemStyle;
import com.cometchat.chatuikit.shared.views.timeslotselector.TimeSlotSelectorStyle;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class SchedulerBubbleFragment extends Fragment {

    private CometChatSchedulerBubble schedulerBubble;
    private MaterialCardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scheduler_bubble, container, false);
        schedulerBubble = view.findViewById(R.id.scheduler_bubble);
        cardView = view.findViewById(R.id.scheduler_bubble_card);
        schedulerBubble.setStyle(getSchedulerBubbleStyle());
        schedulerBubble.setSchedulerMessage(getSchedulerMessage());
        cardView.setCardBackgroundColor(CometChatTheme.getInstance(getContext()).getPalette().getAccent50());
        return view;
    }

    private SchedulerBubbleStyle getSchedulerBubbleStyle() {
        CometChatTheme theme = CometChatTheme.getInstance(getContext());
        SchedulerBubbleStyle schedulerBubbleStyle = new SchedulerBubbleStyle();
        schedulerBubbleStyle.setAvatarStyle(new AvatarStyle().setOuterCornerRadius(100).setInnerBackgroundColor(theme.getPalette().getAccent600()).setTextColor(theme.getPalette().getAccent900()).setTextAppearance(theme.getTypography().getName()));
        schedulerBubbleStyle.setCalenderStyle(new CalenderStyle().setTitleTextAppearance(theme.getTypography().getName()).setTitleTextColor(theme.getPalette().getAccent()));
        schedulerBubbleStyle.setTimeSlotSelectorStyle(new TimeSlotSelectorStyle()
                .setCalenderImageTint(theme.getPalette().getAccent())
                .setEmptyTimeSlotIconColor(theme.getPalette().getAccent500())
                .setChosenDateTextAppearance(theme.getTypography().getSubtitle1())
                .setChosenDateTextColor(theme.getPalette().getAccent())
                .setSeparatorColor(theme.getPalette().getAccent100())
                .setTitleColor(theme.getPalette().getAccent())
                .setTitleTextAppearance(theme.getTypography().getName())
                .setEmptyTimeSlotTextColor(theme.getPalette().getAccent500())
                .setEmptyTimeSlotTextAppearance(theme.getTypography().getText1())
        );
        schedulerBubbleStyle.setSlotStyle(new TimeSlotItemStyle().setCornerRadius(20).setBackground(theme.getPalette().getBackground()).setTimeColor(theme.getPalette().getAccent()));
        schedulerBubbleStyle.setSelectedSlotStyle(new TimeSlotItemStyle().setCornerRadius(20).setBackground(theme.getPalette().getPrimary()).setTimeColor(Color.WHITE));
        schedulerBubbleStyle.setScheduleStyle(new ScheduleStyle()
                .setProgressBarTintColor(Color.WHITE)
                .setButtonBackgroundColor(theme.getPalette().getPrimary())
                .setButtonTextColor(Color.WHITE)
                .setCalendarIconTint(theme.getPalette().getAccent())
                .setClockIconTint(theme.getPalette().getAccent())
                .setTimeZoneIconTint(theme.getPalette().getAccent())
                .setDurationTextColor(theme.getPalette().getAccent())
                .setTimeTextColor(theme.getPalette().getAccent())
                .setTimeZoneTextColor(theme.getPalette().getAccent())
                .setErrorTextColor(theme.getPalette().getError())
                .setButtonTextAppearance(theme.getTypography().getSubtitle1())
                .setErrorTextAppearance(theme.getTypography().getCaption1())
                .setDurationTextAppearance(theme.getTypography().getSubtitle1())
                .setTimeTextAppearance(theme.getTypography().getSubtitle1())
                .setTimeZoneTextAppearance(theme.getTypography().getSubtitle1())
        );
        schedulerBubbleStyle.setTitleAppearance(theme.getTypography().getHeading());
        schedulerBubbleStyle.setNameAppearance(theme.getTypography().getName());
        schedulerBubbleStyle.setNameColor(theme.getPalette().getAccent());
        schedulerBubbleStyle.setTitleColor(theme.getPalette().getAccent());
        schedulerBubbleStyle.setBackIconTint(theme.getPalette().getPrimary());
        schedulerBubbleStyle.setSubtitleTextColor(theme.getPalette().getAccent600());
        schedulerBubbleStyle.setClockIconTint(theme.getPalette().getAccent600());
        schedulerBubbleStyle.setSubtitleTextAppearance(theme.getTypography().getSubtitle1());
        schedulerBubbleStyle.setSeparatorColor(theme.getPalette().getAccent100());
        schedulerBubbleStyle.setInitialSlotsItemStyle(new TimeSlotItemStyle().setBackground(theme.getPalette().getBackground()).setTimeColor(theme.getPalette().getPrimary()).setTimeTextAppearance(theme.getTypography().getSubtitle2()).setBorderColor(theme.getPalette().getPrimary()).setBorderWidth(2).setCornerRadius(25));
        schedulerBubbleStyle.setMoreTextColor(theme.getPalette().getPrimary());
        schedulerBubbleStyle.setDurationTimeTextColor(theme.getPalette().getAccent500());
        schedulerBubbleStyle.setGlobeIconTint(theme.getPalette().getAccent());
        schedulerBubbleStyle.setTimeZoneTextColor(theme.getPalette().getAccent());
        schedulerBubbleStyle.setMoreTextAppearance(theme.getTypography().getSubtitle2());
        schedulerBubbleStyle.setDurationTimeTextAppearance(theme.getTypography().getCaption1());
        schedulerBubbleStyle.setTimeZoneTextAppearance(theme.getTypography().getSubtitle2());
        schedulerBubbleStyle.setQuickViewStyle(new QuickViewStyle().setCornerRadius(16).setBackground(theme.getPalette().getBackground()).setLeadingBarTint(theme.getPalette().getPrimary()).setTitleColor(theme.getPalette().getAccent()).setTitleAppearance(theme.getTypography().getText1()).setSubtitleColor(theme.getPalette().getAccent500()).setSubtitleAppearance(theme.getTypography().getSubtitle1()));
        schedulerBubbleStyle.setCornerRadius(10);
        schedulerBubbleStyle.setQuickSlotAvailableAppearance(theme.getTypography().getText1());
        schedulerBubbleStyle.setQuickSlotAvailableTextColor(theme.getPalette().getAccent500());
        schedulerBubbleStyle.setDisableColor(theme.getPalette().getAccent500());

        return schedulerBubbleStyle;
    }

    private SchedulerMessage getSchedulerMessage() {

        SchedulerMessage schedulerMessage = new SchedulerMessage();
        schedulerMessage.setDuration(60);
        schedulerMessage.setAllowSenderInteraction(true); // true to set the sender as the scheduler
        schedulerMessage.setTitle("Meet Dr. Jackob");
        schedulerMessage.setBufferTime(15);
        schedulerMessage.setAvatarUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdRz0HEBl1wvncmX6rU8wFrRDxt2cvn2Dq9w&usqp=CAU");
        schedulerMessage.setGoalCompletionText("Meeting Scheduled Successfully!!");

        TimeZone timeZone = TimeZone.getDefault();
        schedulerMessage.setTimezoneCode(timeZone.getID());
        schedulerMessage.setDateRangeStart("2024-01-01");
        schedulerMessage.setDateRangeEnd("2024-12-31");
        schedulerMessage.setReceiverUid("superhero1");
        schedulerMessage.setReceiverType(CometChatConstants.RECEIVER_TYPE_USER);
        schedulerMessage.setSender(CometChatUIKit.getLoggedInUser());
        schedulerMessage.setReceiver(AppUtils.getDefaultUser());
        HashMap<String, List<TimeRange>> availability = new HashMap<>();
        availability.put("monday", Arrays.asList(new TimeRange("0000", "1359")));
        availability.put("tuesday", Arrays.asList(new TimeRange("0000", "1559")));
        availability.put("wednesday", Arrays.asList(new TimeRange("0000", "0659")));
        availability.put("thursday", Arrays.asList(new TimeRange("0000", "0959")));
        availability.put("friday", Arrays.asList(new TimeRange("0000", "1059")));
        schedulerMessage.setAvailability(availability);
        APIAction clickAction = new APIAction("https://www.example.com", "POST", "data");

        ButtonElement scheduleElement = new ButtonElement("21", "Submit", clickAction);
        schedulerMessage.setScheduleElement(scheduleElement);
        return schedulerMessage;
    }
}