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
        cardView.setCardBackgroundColor(CometChatTheme.getInstance().getPalette().getAccent50(getContext()));
        return view;
    }

    private SchedulerBubbleStyle getSchedulerBubbleStyle() {
        CometChatTheme theme = CometChatTheme.getInstance(getContext());
        SchedulerBubbleStyle schedulerBubbleStyle = new SchedulerBubbleStyle();
        schedulerBubbleStyle.setAvatarStyle(new AvatarStyle().setOuterCornerRadius(100).setInnerBackgroundColor(theme.getPalette().getAccent600(getContext())).setTextColor(theme.getPalette().getAccent900(getContext())).setTextAppearance(theme.getTypography().getName()));
        schedulerBubbleStyle.setCalenderStyle(new CalenderStyle().setTitleTextAppearance(theme.getTypography().getName()).setTitleTextColor(theme.getPalette().getAccent(getContext())));
        schedulerBubbleStyle.setTimeSlotSelectorStyle(new TimeSlotSelectorStyle()
                .setCalenderImageTint(theme.getPalette().getAccent(getContext()))
                .setEmptyTimeSlotIconColor(theme.getPalette().getAccent500(getContext()))
                .setChosenDateTextAppearance(theme.getTypography().getSubtitle1())
                .setChosenDateTextColor(theme.getPalette().getAccent(getContext()))
                .setSeparatorColor(theme.getPalette().getAccent100(getContext()))
                .setTitleColor(theme.getPalette().getAccent(getContext()))
                .setTitleTextAppearance(theme.getTypography().getName())
                .setEmptyTimeSlotTextColor(theme.getPalette().getAccent500(getContext()))
                .setEmptyTimeSlotTextAppearance(theme.getTypography().getText1())
        );
        schedulerBubbleStyle.setSlotStyle(new TimeSlotItemStyle().setCornerRadius(20).setBackground(theme.getPalette().getBackground(getContext())).setTimeColor(theme.getPalette().getAccent(getContext())));
        schedulerBubbleStyle.setSelectedSlotStyle(new TimeSlotItemStyle().setCornerRadius(20).setBackground(theme.getPalette().getPrimary(getContext())).setTimeColor(Color.WHITE));
        schedulerBubbleStyle.setScheduleStyle(new ScheduleStyle()
                .setProgressBarTintColor(Color.WHITE)
                .setButtonBackgroundColor(theme.getPalette().getPrimary(getContext()))
                .setButtonTextColor(Color.WHITE)
                .setCalendarIconTint(theme.getPalette().getAccent(getContext()))
                .setClockIconTint(theme.getPalette().getAccent(getContext()))
                .setTimeZoneIconTint(theme.getPalette().getAccent(getContext()))
                .setDurationTextColor(theme.getPalette().getAccent(getContext()))
                .setTimeTextColor(theme.getPalette().getAccent(getContext()))
                .setTimeZoneTextColor(theme.getPalette().getAccent(getContext()))
                .setErrorTextColor(theme.getPalette().getError(getContext()))
                .setButtonTextAppearance(theme.getTypography().getSubtitle1())
                .setErrorTextAppearance(theme.getTypography().getCaption1())
                .setDurationTextAppearance(theme.getTypography().getSubtitle1())
                .setTimeTextAppearance(theme.getTypography().getSubtitle1())
                .setTimeZoneTextAppearance(theme.getTypography().getSubtitle1())
        );
        schedulerBubbleStyle.setTitleAppearance(theme.getTypography().getHeading());
        schedulerBubbleStyle.setNameAppearance(theme.getTypography().getName());
        schedulerBubbleStyle.setNameColor(theme.getPalette().getAccent(getContext()));
        schedulerBubbleStyle.setTitleColor(theme.getPalette().getAccent(getContext()));
        schedulerBubbleStyle.setBackIconTint(theme.getPalette().getPrimary(getContext()));
        schedulerBubbleStyle.setSubtitleTextColor(theme.getPalette().getAccent600(getContext()));
        schedulerBubbleStyle.setClockIconTint(theme.getPalette().getAccent600(getContext()));
        schedulerBubbleStyle.setSubtitleTextAppearance(theme.getTypography().getSubtitle1());
        schedulerBubbleStyle.setSeparatorColor(theme.getPalette().getAccent100(getContext()));
        schedulerBubbleStyle.setInitialSlotsItemStyle(new TimeSlotItemStyle().setBackground(theme.getPalette().getBackground(getContext())).setTimeColor(theme.getPalette().getPrimary(getContext())).setTimeTextAppearance(theme.getTypography().getSubtitle2()).setBorderColor(theme.getPalette().getPrimary(getContext())).setBorderWidth(2).setCornerRadius(25));
        schedulerBubbleStyle.setMoreTextColor(theme.getPalette().getPrimary(getContext()));
        schedulerBubbleStyle.setDurationTimeTextColor(theme.getPalette().getAccent500(getContext()));
        schedulerBubbleStyle.setGlobeIconTint(theme.getPalette().getAccent(getContext()));
        schedulerBubbleStyle.setTimeZoneTextColor(theme.getPalette().getAccent(getContext()));
        schedulerBubbleStyle.setMoreTextAppearance(theme.getTypography().getSubtitle2());
        schedulerBubbleStyle.setDurationTimeTextAppearance(theme.getTypography().getCaption1());
        schedulerBubbleStyle.setTimeZoneTextAppearance(theme.getTypography().getSubtitle2());
        schedulerBubbleStyle.setQuickViewStyle(new QuickViewStyle().setCornerRadius(16).setBackground(theme.getPalette().getBackground(getContext())).setLeadingBarTint(theme.getPalette().getPrimary(getContext())).setTitleColor(theme.getPalette().getAccent(getContext())).setTitleAppearance(theme.getTypography().getText1()).setSubtitleColor(theme.getPalette().getAccent500(getContext())).setSubtitleAppearance(theme.getTypography().getSubtitle1()));
        schedulerBubbleStyle.setCornerRadius(10);
        schedulerBubbleStyle.setQuickSlotAvailableAppearance(theme.getTypography().getText1());
        schedulerBubbleStyle.setQuickSlotAvailableTextColor(theme.getPalette().getAccent500(getContext()));
        schedulerBubbleStyle.setDisableColor(theme.getPalette().getAccent500(getContext()));

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