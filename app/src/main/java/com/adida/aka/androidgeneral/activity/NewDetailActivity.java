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
    private WebView webViewLink;
    private ProgressBar mProgressBar;
    boolean loadingFinished = true;
    boolean redirect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
        webViewLink = (WebView) findViewById(R.id.webview_link);
        mProgressBar = (ProgressBar) findViewById(R.id.prb_load);

        Intent intent = getIntent();
        final String link = intent.getStringExtra("link");

        webViewLink.loadUrl(link);
        WebSettings webSettings = webViewLink.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webViewLink.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(
                    WebView view, WebResourceRequest request) {
                if (!loadingFinished) {
                    redirect = true;
                }

                loadingFinished = false;
                return true;
            }

            @Override
            public void onPageStarted(
                    WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingFinished = false;
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(!redirect){
                    loadingFinished = true;
                }

                if(loadingFinished && !redirect){
                    mProgressBar.setVisibility(View.GONE);
                } else{
                    redirect = false;
                }
            }
        });

    }
}
