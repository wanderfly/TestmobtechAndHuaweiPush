package com.kevin.mobtechandhuaweipush.web;

import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;


/**
 * @author Kevin  2020/11/16
 */
public class FxWebChromeClient extends WebChromeClient {
    private static final boolean DEBUG = true;
    private static final String TAG = "FxWebChromeClient";
    private final ProgressBar mProgressBar;

    public FxWebChromeClient(@Nullable ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (DEBUG) Log.e(TAG, "onProgressChanged: newProgress:" + newProgress);
        if (mProgressBar!=null){
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.INVISIBLE);//加载完网页进度条消失
            } else {
                mProgressBar.setVisibility(View.VISIBLE);  //开始加载网页时显示进度条
                mProgressBar.setProgress(newProgress);     //设置进度值
            }
        }
        super.onProgressChanged(view, newProgress);
    }
}
