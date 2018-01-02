package vn.bacsiviet.bacsivietvn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.view.View.SCROLLBARS_INSIDE_OVERLAY;

public class DetailWeb extends AppCompatActivity {

    private static final String TAG = "DetailWeb";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        webView = findViewById(R.id.web_view);
        Intent intent = getIntent();
        if (intent != null) {
            String urlPath = intent.getStringExtra("QUESTION");
            if (urlPath != null) {
//                Log.d(TAG, "onCreate: " + urlPath);
                webView.setWebViewClient(new MyWebViewClient());
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl(urlPath);

            } else
                webView.loadData(getResources().getString(R.string.error_url), "text/html", null);
        }


    }

    private class MyWebViewClient extends WebViewClient {
        private static final String TAG = "MyWebViewClient" ;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldOverrideUrlLoading: " + request);
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "onPageStarted: " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished: "+url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.d(TAG, "onLoadResource: "+url);
        }
    }
}
