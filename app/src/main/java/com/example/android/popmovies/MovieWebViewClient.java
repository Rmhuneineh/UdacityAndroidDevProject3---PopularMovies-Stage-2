package com.example.android.popmovies;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by rmhuneineh on 10/03/2018.
 */

public class MovieWebViewClient extends WebViewClient {
    private ProgressBar mProgressBar;

    public MovieWebViewClient(ProgressBar progressBar) {
        mProgressBar = progressBar;
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mProgressBar.setVisibility(View.GONE);
    }

}
