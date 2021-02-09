package com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.emoji.widget.EmojiTextView;

import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.model.Reaction;

import java.util.List;

public class EmojiAdapter extends ArrayAdapter<Reaction> {

    private boolean mUseSystemDefault = Boolean.FALSE;

    // CONSTRUCTOR
    public EmojiAdapter(Context context, Reaction[] data) {
        super(context, R.layout.rsc_emoji_item, data);
    }

    public EmojiAdapter(Context context, List<Reaction> data) {
        super(context, R.layout.rsc_emoji_item, data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = View.inflate(getContext(), R.layout.rsc_emoji_item, null);
            ViewHolder viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        if (null != getItem(position)) {
            Reaction emoji = this.getItem(position);
            ViewHolder holder = (ViewHolder) view.getTag();
            holder.icon.setText(new String(Character.toChars(emoji.getCode())));
        }

        return view;
    }

    static class ViewHolder {
        EmojiTextView icon;

        public ViewHolder(View view) {
            this.icon = view.findViewById(R.id.emoji_icon);
        }
    }
}