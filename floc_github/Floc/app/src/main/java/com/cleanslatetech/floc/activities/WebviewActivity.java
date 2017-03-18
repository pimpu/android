package com.cleanslatetech.floc.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cleanslatetech.floc.R;

public class WebviewActivity extends BaseAppCompactActivity {
    WebView mWebView;
    ProgressDialog progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        super.setToolBar(getIntent().getExtras().getString("from"));

        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void init() {
        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.setWebViewClient(new AppWebViewClients());

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.loadUrl(getIntent().getExtras().getString("url"));
        progressBar = ProgressDialog.show(WebviewActivity.this, "Floc", "Loading...");
    }

    private class AppWebViewClients extends WebViewClient {

        public AppWebViewClients() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(WebviewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            AlertDialog alertDialog = new AlertDialog.Builder(WebviewActivity .this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(description);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();
        }
    }
}
