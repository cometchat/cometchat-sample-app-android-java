package com.cometchat.pro.uikit.ui_components.messages.message_actions;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_components.messages.extensions.Extensions;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.model.Reaction;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.cometchat.pro.uikit.ui_components.messages.thread_message_list.CometChatThreadMessageListActivity;

/**
 * MessageActions is a BottomSheetDialogFragment which is being used in MessageList & ThreadList.
 * It is used to show message actions such as edit,delete,copy,share,etc for a particular message.
 *
 */
public class CometChatMessageActions extends BottomSheetDialogFragment {

    private TextView threadMessage;
    private TextView editMessage;
    private TextView replyMessage;
    private TextView forwardMessage;
    private TextView deleteMessage;
    private TextView copyMessage;
    private TextView messageInfo;
    private TextView shareMessage;
    private TextView translateMessage;
    private TextView retryMessage;
    private TextView replyMessagePrivately;

    private TextView messagePrivately;

    private String userAvatar;

    private LinearLayout reactionsList;
    private ImageView showReactionDialog;

    private boolean isPrivateReplyVisible;
    private boolean isShareVisible;
    private boolean isThreadVisible;
    private boolean isCopyVisible;
    private boolean isEditVisible;
    private boolean isDeleteVisible;
    private boolean isForwardVisible;
    private boolean isReplyVisible;
    private boolean isReplyPrivatelyVisible;
    private boolean isMessageInfoVisible;
    private boolean isReactionsVisible;
    private boolean isTranslateVisible;
    private boolean isRetryVisible;

    public MessageActionListener messageActionListener;

    private String type;

    private static int INITIAL_REACTION_COUNT = 6;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchArguments();
    }

    private void fetchArguments() {
        if (getArguments()!=null) {
            isPrivateReplyVisible = getArguments().getBoolean("privateReplyVisible");
            isCopyVisible = getArguments().getBoolean("copyVisible");
            isThreadVisible = getArguments().getBoolean("threadVisible");
            isEditVisible = getArguments().getBoolean("editVisible");
            isDeleteVisible = getArguments().getBoolean("deleteVisible");
            isReplyVisible = getArguments().getBoolean("replyVisible");
            isReplyPrivatelyVisible = getArguments().getBoolean("replyPrivatelyVisible");
            isForwardVisible = getArguments().getBoolean("forwardVisible");
            isShareVisible = getArguments().getBoolean("shareVisible");
            isMessageInfoVisible = getArguments().getBoolean("messageInfoVisible");
            isReactionsVisible = getArguments().getBoolean("isReactionVisible");
            isTranslateVisible = getArguments().getBoolean("translateVisible");
            isRetryVisible = getArguments().getBoolean("retryVisible");
            type = getArguments().getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cometchat_message_actions, container, false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                // androidx should use: com.google.android.material.R.id.design_bottom_sheet
                FrameLayout bottomSheet = (FrameLayout)
                        dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });

        reactionsList = view.findViewById(R.id.initial_reactions);
        List<Reaction> reactions = Extensions.getInitialReactions(INITIAL_REACTION_COUNT);
        for(Reaction reaction : reactions) {
            View vw = LayoutInflater.from(getContext()).inflate(R.layout.reaction_list_row,null);
            TextView textView = vw.findViewById(R.id.reaction);
            LinearLayout.LayoutParams params = new LinearLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;
            params.rightMargin = 16;
            params.bottomMargin = 8;
            params.topMargin = 8;
            textView.setLayoutParams(params);
            textView.setText(reaction.getName());
            reactionsList.addView(vw);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (messageActionListener!=null)
                        messageActionListener.onReactionClick(reaction);
                    dismiss();
                }
            });
        }
        ImageView addEmojiView = new ImageView(getContext());
        addEmojiView.setImageDrawable(getResources().getDrawable(R.drawable.add_emoji));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int)Utils.dpToPx(getContext(),36),(int)Utils.dpToPx(getContext(),36));
        layoutParams.topMargin = 8;
        layoutParams.leftMargin = 16;
        addEmojiView.setLayoutParams(layoutParams);
        reactionsList.addView(addEmojiView);
        addEmojiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageActionListener!=null)
                    messageActionListener.onReactionClick(new Reaction("add_emoji",0));
                dismiss();
            }
        });

        retryMessage = view.findViewById(R.id.retry_message);
        translateMessage = view.findViewById(R.id.translate_message);
        threadMessage = view.findViewById(R.id.start_thread);
        editMessage = view.findViewById(R.id.edit_message);
        replyMessage = view.findViewById(R.id.reply_message);
        forwardMessage = view.findViewById(R.id.forward_message);
        deleteMessage = view.findViewById(R.id.delete_message);
        copyMessage = view.findViewById(R.id.copy_message);
        shareMessage = view.findViewById(R.id.share_message);
        messageInfo = view.findViewById(R.id.message_info);

        messagePrivately = view.findViewById(R.id.reply_privately);
        replyMessagePrivately = view.findViewById(R.id.reply_message_privately);

        if (isPrivateReplyVisible)
            messagePrivately.setVisibility(View.VISIBLE);
        else
            messagePrivately.setVisibility(View.GONE);

        if (isReplyPrivatelyVisible)
            replyMessagePrivately.setVisibility(View.VISIBLE);
        else
            replyMessagePrivately.setVisibility(View.GONE);

        if (isPrivateReplyVisible)
            messagePrivately.setVisibility(View.VISIBLE);
        else
            messagePrivately.setVisibility(View.GONE);

        if (isRetryVisible)
            retryMessage.setVisibility(View.VISIBLE);
        else
            retryMessage.setVisibility(View.GONE);

        if (isTranslateVisible)
            translateMessage.setVisibility(View.VISIBLE);
        else
            translateMessage.setVisibility(View.GONE);

        if (isReactionsVisible)
            reactionsList.setVisibility(View.VISIBLE);
        else
            reactionsList.setVisibility(View.GONE);
        if (isThreadVisible)
            threadMessage.setVisibility(View.VISIBLE);
        else
            threadMessage.setVisibility(View.GONE);
        if (isCopyVisible)
            copyMessage.setVisibility(View.VISIBLE);
        else
            copyMessage.setVisibility(View.GONE);
        if (isEditVisible)
            editMessage.setVisibility(View.VISIBLE);
        else
            editMessage.setVisibility(View.GONE);
        if (isDeleteVisible)
            deleteMessage.setVisibility(View.VISIBLE);
        else
            deleteMessage.setVisibility(View.GONE);
        if (isReplyVisible)
            replyMessage.setVisibility(View.VISIBLE);
        else
            replyMessage.setVisibility(View.GONE);
        if (isForwardVisible)
            forwardMessage.setVisibility(View.VISIBLE);
        else
            forwardMessage.setVisibility(View.GONE);
        if (isShareVisible)
            shareMessage.setVisibility(View.VISIBLE);
        else
            shareMessage.setVisibility(View.GONE);

        if (isMessageInfoVisible)
            messageInfo.setVisibility(View.VISIBLE);
        else
            messageInfo.setVisibility(View.GONE);

        if (type!=null && type.equals(CometChatThreadMessageListActivity.class.getName())) {
            threadMessage.setVisibility(View.GONE);
        }

        replyMessagePrivately.setOnClickListener(v-> {
            if (messageActionListener!=null)
                messageActionListener.onReplyMessagePrivately();
            dismiss();
        });
        messagePrivately.setOnClickListener(v-> {
            if (messageActionListener!=null) {
                messageActionListener.onPrivateReplyToUser();
                dismiss();
            }
        });

        retryMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onRetryClick();
                dismiss();
            }
        });

        translateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messageActionListener!=null)
                    messageActionListener.onTranslateMessageClick();
                dismiss();
            }
        });
        threadMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onThreadMessageClick();
                dismiss();
            }
        });
        copyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onCopyMessageClick();
                dismiss();
            }
        });
        editMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onEditMessageClick();
                dismiss();
            }
        });
        deleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onDeleteMessageClick();
                dismiss();
            }
        });
        replyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onReplyMessageClick();
                dismiss();
            }
        });
        forwardMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onForwardMessageClick();
                dismiss();
            }
        });
        shareMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onShareMessageClick();
                dismiss();
            }
        });
        messageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageActionListener!=null)
                    messageActionListener.onMessageInfoClick();
                dismiss();
            }
        });

        return view;
    }


    public void setMessageActionListener(MessageActionListener messageActionListener) {
        this.messageActionListener = messageActionListener;

    }

    public interface MessageActionListener {
        void onThreadMessageClick();
        void onEditMessageClick();
        void onReplyMessageClick();
        void onForwardMessageClick();
        void onDeleteMessageClick();
        void onCopyMessageClick();
        void onShareMessageClick();
        void onMessageInfoClick();

        void onReactionClick(Reaction reaction);

        void onTranslateMessageClick();
        void onRetryClick();
        void onPrivateReplyToUser();

        void onReplyMessagePrivately();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity!=null)
            if (type!=null && type== CometChatMessageListActivity.class.getName())
                ((CometChatMessageListActivity)activity).handleDialogClose(dialog);
            else
                ((CometChatThreadMessageListActivity)activity).handleDialogClose(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        Activity activity = getActivity();
        if (activity!=null)
            if (type!=null && type== CometChatMessageListActivity.class.getName())
                ((CometChatMessageListActivity)activity).handleDialogClose(dialog);
            else
                ((CometChatThreadMessageListActivity)activity).handleDialogClose(dialog);
    }
}