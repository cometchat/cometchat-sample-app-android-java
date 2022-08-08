package com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.cometchat.pro.models.AppEntity;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import com.cometchat.pro.uikit.R;
import com.google.android.material.card.MaterialCardView;

/**
 * Purpose - This class is a subclass of AppCompatImageView, It is a component which is been used by developer
 * to display the avatar of their user or icon of the group. This class contains various methods which
 * are used to change shape of image as circle or rectangle, border with of image, border color of image.
 * and set default drawable if there is no image.
 *
 * Created on - 20th December 2019
 *
 * Modified on  - 20th January 2020
 *
 */
public class CometChatAvatar extends MaterialCardView {

    private static final String TAG = CometChatAvatar.class.getSimpleName();

    /*
     * Place holder drawable (with background color and initials)
     * */
    Drawable drawable;

    /*
     * Contains initials of the member
     * */
    String text;

    /*
     * User whose avatar should be displayed
     * */
    //User user;
    String avatarUrl;


    /*
     * Bounds of the canvas in float
     * Used to set bounds of member initial and background
     * */
    RectF rectF;


    private Context context;

    private int color;

    private int borderColor;

    private int backgroundColor;

    private float borderWidth;

    private float radius;

    private MaterialCardView cardView;
    private ImageView imageView;
    private TextView textView;

    public CometChatAvatar(Context context) {
        super(context);
        this.context = context;
    }

    public CometChatAvatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getAttributes(attrs);
    }

    public CometChatAvatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttributes(attrs);
    }

    private void getAttributes(AttributeSet attrs) {
        View view =View.inflate(context, R.layout.cometchat_avatar,null);
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Avatar,
                0, 0);
        /*
         * Get the shape and set shape field accordingly
         * */
        drawable = a.getDrawable(R.styleable.Avatar_image);
        radius = a.getDimension(R.styleable.Avatar_corner_radius,16);
        backgroundColor = a.getColor(R.styleable.Avatar_background_color,
                getResources().getColor(R.color.colorPrimary));
        avatarUrl = a.getString(R.styleable.Avatar_avatar);
//            borderRadius = a.getInteger(R.styleable.Avatar_cornerRadius,8);
        borderColor = a.getColor(R.styleable.Avatar_border_color,getResources().getColor(R.color.colorPrimary));
        //          backgroundColor = a.getColor(R.styleable.Avatar_backgroundColor,getResources().getColor(R.color.colorPrimary));
        borderWidth = a.getDimension(R.styleable.Avatar_border_width,1f);


        addView(view);


        cardView = view.findViewById(R.id.cardView);
        setRadius(radius);
        cardView.setCardBackgroundColor(backgroundColor);
        imageView = view.findViewById(R.id.image);
        textView = view.findViewById(R.id.text);
        if (drawable!=null)
            imageView.setImageDrawable(drawable);
    }

    private void setAvatar(@NonNull User user) {

        if (user!=null) {
            if (user.getAvatar() != null) {
                avatarUrl = user.getAvatar();
                if (isValidContextForGlide(context)) {
                    setValues();
                }
            } else {
                if (user.getName()!=null&&!user.getName().isEmpty()) {
                    if (user.getName().length() > 2) {
                        text = user.getName().substring(0, 2);
                    } else {
                        text = user.getName();
                    }
                }else {
                    text="??";
                }
                imageView.setVisibility(View.GONE);
                textView.setText(text);
            }
        }

    }

    /**
     * This method is used to check if the group parameter passed is null or not. If it is not null then
     * it will show icon of group, else it will show default drawable or first two letter of group name.
     *
     * @param group is an object of Group.class.
     * @see Group
     */
    private void setAvatar(@NonNull Group group) {

        if (group!=null) {

            if (group.getIcon() != null) {
                avatarUrl = group.getIcon();
                if (isValidContextForGlide(context))
                    setValues();
            } else {
                if (group.getName()!=null) {
                    if (group.getName().length() > 2)
                        text = group.getName().substring(0, 2);
                    else {
                        text = group.getName();
                    }
                }
                imageView.setVisibility(View.GONE);
                textView.setText(text);
            }
        }
    }

    public void setAvatar(AppEntity appEntity) {
        if (appEntity instanceof User) {
            setAvatar((User)appEntity);
        } else if (appEntity instanceof Group) {
            setAvatar((Group)appEntity);
        }
    }

    /**
     * This method is used to set image by using url passed in parameter..
     *
     * @param avatarUrl is an object of String.class which is used to set avatar.
     *
     */
    public void setAvatar(@NonNull String avatarUrl) {

        this.avatarUrl = avatarUrl;
        if (isValidContextForGlide(context))
            setValues();

    }

    /**
     * @param drawable  placeholder image
     * @param avatarUrl url of the image
     */
    public void setAvatar(Drawable drawable, @NonNull String avatarUrl) {
        this.drawable = drawable;
        this.avatarUrl = avatarUrl;
        if (isValidContextForGlide(context)) {
            setValues();
        }
    }

    public String getAvatarUrl() {
        return  this.avatarUrl;
    }
    /**
     * This method is used to set first two character as image. It is used when user, group or url
     * is null.
     *
     * @param name is a object of String.class. Its first 2 character are used in image with no avatar or icon.
     */
    public void setInitials(@NonNull String name) {
        if (name!=null) {
            if (name.length() >= 2) {
                text = name.substring(0, 2);
            } else {
                text = name;
            }
        }
        imageView.setVisibility(View.GONE);
        textView.setText(text);
        textView.setVisibility(View.VISIBLE);
    }

    public void setAvatar(String url,String name) {
        setAvatar(url);
        if (url==null)
            setInitials(name);
    }
    public float getBorderWidth() {
        return borderWidth;
    }
    public Drawable getDrawable()
    {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        imageView.setImageDrawable(drawable);
        imageView.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }
    /*
     * Set user specific fields in here
     * */
    private void setValues() {
        try {
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                if (context != null) {
                    Glide.with(context)
                            .load(avatarUrl)
                            .placeholder(drawable)
                            .into(imageView);
                }
                imageView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        invalidate();
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }


    /**
     * This method is used to set border color of avatar.
     * @param color
     */
    public void setBorderColor(@ColorInt int color) {
        this.borderColor = color;
        cardView.setStrokeColor(color);
    }

    /**
     * This method is used to set border width of avatar
     * @param borderWidth
     */
    public void setBorderWidth(int borderWidth) {

        this.borderWidth = borderWidth;
        cardView.setStrokeWidth(borderWidth);
    }

    public void setCornerRadius(int radius) {
        this.radius = radius;
        cardView.setRadius(radius);
        setRadius(radius);
    }

    public void setBackgroundColor(@ColorInt int color) {
        this.backgroundColor = color;
        imageView.setBackgroundColor(backgroundColor);
    }
}
