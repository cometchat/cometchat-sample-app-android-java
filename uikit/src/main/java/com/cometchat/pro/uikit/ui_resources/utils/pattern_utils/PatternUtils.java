package com.cometchat.pro.uikit.ui_resources.utils.pattern_utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Patterns;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;

public class PatternUtils {
    public static void setHyperLinkSupport(Context context, TextView txtMessage) {
        new PatternBuilder().
                addPattern(Pattern.compile("(^|[\\s.:;?\\-\\]<\\(])" +
                                "((https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,☺]+[\\w/#](\\(\\))?)" +
                                "(?=$|[\\s',\\|\\(\\).:;?\\-\\[\\]>\\)])"),
                        context.getResources().getColor(FeatureRestriction.getUrlColor()),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                if (!text.trim().contains("http")) {
                                    text = "http://"+text;
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(text.trim()));
                                context.startActivity(Intent.createChooser(intent, "Url"));
                            }
                        }).into(txtMessage);
        new PatternBuilder().
                addPattern(Patterns.PHONE, context.getResources().getColor(FeatureRestriction.getPhoneColor()),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse(text));
                                intent.setData(Uri.parse("tel:"+text));
                                context.startActivity(Intent.createChooser(intent, "Dial"));
                            }
                        }).into(txtMessage);
        new PatternBuilder().
                addPattern(Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}"),
                        context.getResources().getColor(FeatureRestriction.getEmailColor()),
                        new PatternBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" +text));
                                intent.putExtra(Intent.EXTRA_EMAIL, text);
                                context.startActivity(Intent.createChooser(intent, "Mail"));
                            }
                        }).into(txtMessage);
    }


    /**
     * Below method is used to remove the emojis from string
     * @param content is a String object
     * @return a String value without emojis.
     */
    public static String removeEmojiAndSymbol(String content) {
        String utf8tweet = "";
        try {
            byte[] utf8Bytes = content.getBytes("UTF-8");
            utf8tweet = new String(utf8Bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Pattern unicodeOutliers = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE |
                        Pattern.CASE_INSENSITIVE);
        Matcher unicodeOutlierMatcher = unicodeOutliers.matcher(utf8tweet);
        utf8tweet = unicodeOutlierMatcher.replaceAll(" ");
        return utf8tweet;
    }
}
