package com.cometchat.pro.androiduikit.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class AvatarBorderMatcher extends TypeSafeMatcher<View> {

    private final int borderWidth;
    private String resourceName;
    private int resourceId;
    Context context;
    static final int EMPTY = -1;
    static final int ANY = -2;

    AvatarBorderMatcher(Context context, int borderWidth, int resourceId) {
        super(View.class);
        this.borderWidth = borderWidth;
        this.context = context;
    }

    @Override
    protected boolean matchesSafely(View target) {
        if (!(target instanceof ImageView)) {
            return false;
        }
        ImageView imageView = (ImageView) target;
        if (borderWidth == EMPTY) {
            return imageView.getDrawable() == null;
        }
        if (borderWidth == ANY) {
            return imageView.getDrawable() != null;
        }
        Resources resources = target.getContext().getResources();
        Drawable expectedDrawable = resources.getDrawable(resourceId);
        resourceName = resources.getResourceEntryName(resourceId);

        if (expectedDrawable == null) {
            return false;
        }
        CometChatAvatar avatarA = new CometChatAvatar(context);
        avatarA.setImageDrawable(imageView.getDrawable());
        CometChatAvatar avatarB = new CometChatAvatar(context);
        avatarB.setImageDrawable(expectedDrawable);
        return avatarA.getBorderWidth()==avatarB.getBorderWidth()?true:false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with drawable from resource id: ");
        description.appendValue(resourceId);
        if (resourceName != null) {
            description.appendText("[");
            description.appendText(resourceName);
            description.appendText("]");
        }
    }
}
