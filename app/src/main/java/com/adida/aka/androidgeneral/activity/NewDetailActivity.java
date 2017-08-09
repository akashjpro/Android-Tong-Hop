package com.adida.aka.androidgeneral.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.adida.aka.androidgeneral.R;

public class NewDetailActivity extends AppCompatActivity {
    private WebView mWebViewLink;
    private ProgressBar mProgressBar;
    boolean isFinished = true;
    boolean isRedirect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
        mWebViewLink = (WebView) findViewById(R.id.webview_link);
        mProgressBar = (ProgressBar) findViewById(R.id.prb_load);

        Intent intent = getIntent();
        final String link = intent.getStringExtra("link");

        mWebViewLink.loadUrl(link);
        WebSettings webSettings = mWebViewLink.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebViewLink.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view, WebResourceRequest request) {
                if (!isFinished) {
                    isRedirect = true;
                }

                isFinished = false;
                return true;
            }

            @Override
            public void onPageStarted(
                    WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!isRedirect){
                    isFinished = true;
                }

                if(isFinished && !isRedirect){
                    mProgressBar.setVisibility(View.GONE);
                } else{
                    isRedirect = false;
                }
            }
        });

    }
}
