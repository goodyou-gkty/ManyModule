package com.gkt.browse.newdiary;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsArticle extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);

        Intent intent = getIntent();

        webView = (WebView) findViewById(R.id.articleWebView);


        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

       // webView.loadData(intent.getStringExtra("content"),"text/html","UTF-8");

        String url="https://www.wsj.com/articles/u-s-changes-visa-process-for-high-skilled-workers-11548879868";
        webView.loadUrl(url);

    }
}
