package com.cometchat.pro.javasampleapp.fragments.messages;

import android.os.Bundle;

import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cometchat.pro.javasampleapp.R;
import com.cometchatworkspace.components.messages.composer.CometChatMessageComposer;
import com.cometchatworkspace.components.messages.composer.listener.Events;
import com.cometchatworkspace.components.messages.template.CometChatMessageTemplate;

public class MessageComposerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message_composer, container, false);

        //To handle click performed on Action sheet
        CometChatMessageComposer.addListener("MessageComposerFragment", new Events() {
            @Override
            public void onMoreActionClicked(ImageView moreIcon) {
                super.onMoreActionClicked(moreIcon);
            }

            @Override
            public void onPollActionClicked() {
                super.onPollActionClicked();
            }

            @Override
            public void onCameraActionClicked() {
                super.onCameraActionClicked();
            }

            @Override
            public void onGalleryActionClicked() {
                super.onGalleryActionClicked();
            }

            @Override
            public void onAudioActionClicked() {
                super.onAudioActionClicked();
            }

            @Override
            public void onFileActionClicked() {
                super.onFileActionClicked();
            }

            @Override
            public void onVoiceNoteComplete(String string) {
                super.onVoiceNoteComplete(string);
            }

            @Override
            public void onKeyboardMediaSelected(InputContentInfoCompat inputContentInfo) {
                super.onKeyboardMediaSelected(inputContentInfo);
            }

            @Override
            public void onLocationActionClicked() {
                super.onLocationActionClicked();
            }

            @Override
            public void onStickerClicked() {
                super.onStickerClicked();
            }

            @Override
            public void onWhiteboardClicked() {
                super.onWhiteboardClicked();
            }

            @Override
            public void onDocumentClicked() {
                super.onDocumentClicked();
            }

            @Override
            public void onViewClosed() {
                super.onViewClosed();
            }

            @Override
            public void onCustomUserAction(CometChatMessageTemplate template) {
                super.onCustomUserAction(template);
            }
        });

        return view;
    }
}