package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.models.InteractionGoal;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.chatuikit.shared.models.interactiveactions.APIAction;
import com.cometchat.chatuikit.shared.models.interactiveactions.URLNavigationAction;
import com.cometchat.chatuikit.shared.models.interactiveelements.ButtonElement;
import com.cometchat.chatuikit.shared.models.interactiveelements.CheckboxElement;
import com.cometchat.chatuikit.shared.models.interactiveelements.ElementEntity;
import com.cometchat.chatuikit.shared.models.interactiveelements.OptionElement;
import com.cometchat.chatuikit.shared.models.interactiveelements.PlaceHolder;
import com.cometchat.chatuikit.shared.models.interactiveelements.RadioButtonElement;
import com.cometchat.chatuikit.shared.models.interactiveelements.SingleSelectElement;
import com.cometchat.chatuikit.shared.models.interactiveelements.SpinnerElement;
import com.cometchat.chatuikit.shared.models.interactiveelements.TextInputElement;
import com.cometchat.chatuikit.shared.models.interactivemessage.FormMessage;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.views.CometChatFormbubble.CometChatFormBubble;
import com.cometchat.chatuikit.shared.views.CometChatFormbubble.FormBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatSingleSelect.SingleSelectStyle;
import com.cometchat.javasampleapp.AppConstants;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormBubbleFragment extends Fragment {
    private LinearLayout parentLayout;
    private CometChatFormBubble formBubble;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_bubble, container, false);

        parentLayout = view.findViewById(R.id.parent_layout);
        formBubble = view.findViewById(R.id.form_bubble);


        CometChatTheme theme = CometChatTheme.getInstance();
        //create style object for Form bubble
        FormBubbleStyle formBubbleStyle = new FormBubbleStyle()
                .setTitleAppearance(theme.getTypography().getHeading())
                .setTitleColor(theme.getPalette().getAccent(getContext()))

                .setLabelAppearance(theme.getTypography().getSubtitle1())
                .setLabelColor(theme.getPalette().getAccent(getContext()))

                .setInputTextAppearance(theme.getTypography().getSubtitle1())
                .setInputTextColor(theme.getPalette().getAccent(getContext()))
                .setInputHintColor(theme.getPalette().getAccent500(getContext()))
                .setErrorColor(theme.getPalette().getError(getContext()))
                .setInputStrokeColor(theme.getPalette().getAccent600(getContext()))
                .setActiveInputStrokeColor(theme.getPalette().getAccent(getContext()))

                .setDefaultCheckboxButtonTint(theme.getPalette().getAccent500(getContext()))
                .setSelectedCheckboxButtonTint(theme.getPalette().getPrimary(getContext()))
                .setErrorCheckboxButtonTint(theme.getPalette().getError(getContext()))
                .setCheckboxTextColor(theme.getPalette().getAccent(getContext()))
                .setCheckboxTextAppearance(theme.getTypography().getSubtitle1())

                .setButtonBackgroundColor(theme.getPalette().getPrimary(getContext()))
                .setButtonTextColor(theme.getPalette().getAccent900(getContext()))
                .setButtonTextAppearance(theme.getTypography().getSubtitle1())
                .setProgressBarTintColor(theme.getPalette().getAccent900(getContext()))
                .setRadioButtonTint(theme.getPalette().getAccent500(getContext()))
                .setRadioButtonTextColor(theme.getPalette().getAccent(getContext()))
                .setRadioButtonTextAppearance(theme.getTypography().getSubtitle1())
                .setSelectedRadioButtonTint(theme.getPalette().getPrimary(getContext()))

                .setSpinnerTextColor(theme.getPalette().getAccent(getContext()))
                .setSpinnerTextAppearance(theme.getTypography().getSubtitle1())
                .setSpinnerBackgroundColor(theme.getPalette().getAccent500(getContext()))

                .setBackground(theme.getPalette().getBackground(getContext()))
                .setBackground(theme.getPalette().getGradientBackground())

                .setSingleSelectStyle(new SingleSelectStyle()
                        .setOptionTextAppearance(theme.getTypography().getSubtitle1())
                        .setOptionTextColor(theme.getPalette().getAccent500(getContext()))
                        .setSelectedOptionTextAppearance(theme.getTypography().getSubtitle1())
                        .setSelectedOptionTextColor(theme.getPalette().getAccent(getContext()))
                        .setButtonStrokeColor(theme.getPalette().getAccent600(getContext()))
                        .setTitleColor(theme.getPalette().getAccent(getContext()))
                        .setTitleAppearance(theme.getTypography().getSubtitle1())
                );
        formBubble.setStyle(formBubbleStyle);

        //create elements for form bubble
        List<ElementEntity> elementEntities = new ArrayList<>();
        TextInputElement textInputElement1 = new TextInputElement("element1", "Name");
        textInputElement1.setPlaceHolder(new PlaceHolder("write your name here"));

        TextInputElement textInputElement2 = new TextInputElement("element2", "Last Name");
        TextInputElement textInputElement3 = new TextInputElement("element3", "Address");
        textInputElement3.setMaxLines(5);
        textInputElement1.setDefaultValue("vivek");
        elementEntities.add(textInputElement1);
        elementEntities.add(textInputElement2);
        elementEntities.add(textInputElement3);

        List<OptionElement> optionElementList = new ArrayList<>();
        optionElementList.add(new OptionElement("option1", "INDIA"));
        optionElementList.add(new OptionElement("option2", "AUSTRALIA"));
        optionElementList.add(new OptionElement("option3", "RUSSIA"));
        optionElementList.add(new OptionElement("option4", "AMERICA"));
        SpinnerElement spinnerElement = new SpinnerElement("element4", "Country", optionElementList, "option1");
        elementEntities.add(spinnerElement);


        List<OptionElement> checkBox = new ArrayList<>();
        checkBox.add(new OptionElement("option1", "Garbage"));
        checkBox.add(new OptionElement("option2", "Electricity Bill"));
        checkBox.add(new OptionElement("option3", "Lift"));
        CheckboxElement checkboxElement = new CheckboxElement("element5", "Services", checkBox, new ArrayList<>(Arrays.asList("option1", "option2")));
        elementEntities.add(checkboxElement);

        List<OptionElement> optionElementList2 = new ArrayList<>();
        optionElementList2.add(new OptionElement("option1", "A"));
        optionElementList2.add(new OptionElement("option2", "B"));

        SingleSelectElement singleSelectElement = new SingleSelectElement("element6", "Wing", optionElementList2, "option1");
        elementEntities.add(singleSelectElement);

        RadioButtonElement radioButtonElement = new RadioButtonElement("element7", "Country", optionElementList, "option1");
        elementEntities.add(radioButtonElement);

        URLNavigationAction urlNavigationAction = new URLNavigationAction("https://www.cometchat.com/");
        ButtonElement submitButton = new ButtonElement("element9", "About us", urlNavigationAction);
        submitButton.setDisableAfterInteracted(true);
        elementEntities.add(submitButton);

        FormMessage formMessage = new FormMessage(AppUtils.getDefaultUser() != null ? AppUtils.getDefaultUser().getUid() : AppUtils.getDefaultGroup() != null ? AppUtils.getDefaultGroup().getGuid() : null, AppUtils.getDefaultUser() != null ? UIKitConstants.ReceiverType.USER : UIKitConstants.ReceiverType.GROUP, elementEntities, submitButton);
        formMessage.setTitle("Society Survey");
        formMessage.setAllowSenderInteraction(true);
        formMessage.setSender(CometChatUIKit.getLoggedInUser());
        formMessage.setSentAt(System.currentTimeMillis() / 1000);
        formMessage.setReceiver(AppUtils.getDefaultUser() != null ? AppUtils.getDefaultUser() : AppUtils.getDefaultGroup() != null ? AppUtils.getDefaultGroup() : null);
        formMessage.setInteractionGoal(new InteractionGoal(CometChatConstants.INTERACTION_TYPE_All_OF, Arrays.asList("element8", "element9")));

        formBubble.setFormMessage(formMessage);
        return view;
    }
}