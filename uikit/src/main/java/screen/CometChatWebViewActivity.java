package screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cometchat.pro.uikit.R;

import constant.StringContract;

public class CometChatWebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_webview);
        webView = findViewById(R.id.webview);
        if (getIntent().hasExtra(StringContract.IntentStrings.URL)) {
            url = getIntent().getStringExtra(StringContract.IntentStrings.URL);
        }
        if (url!=null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webView.loadUrl(url);
        }
    }
}
