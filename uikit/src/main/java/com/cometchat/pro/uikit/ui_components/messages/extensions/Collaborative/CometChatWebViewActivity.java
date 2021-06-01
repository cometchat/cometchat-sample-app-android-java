package com.cometchat.pro.uikit.ui_components.messages.extensions.Collaborative;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;

/**
 * CometChatCollaborativeActivity is a Activity class which is used to load extension such as
 * whiteboard and writeboard in webView.
 *
 * Created On - 26 November 2020
 *
 */
public class CometChatWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url = null;
    private ImageView mBackIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_webview);
        webView = findViewById(R.id.webview);
        mBackIcon = findViewById(R.id.back_icon);
        mBackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.URL)) {
            url = getIntent().getStringExtra(UIKitConstants.IntentStrings.URL);
        }
        if (url!=null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAppCacheEnabled(true);
            webView.setWebViewClient(new WebViewClient()
            {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url)
                {
                    //view.loadUrl(url);
                    return false;
                }
            });
            webView.loadUrl(url);
        }
    }
}
