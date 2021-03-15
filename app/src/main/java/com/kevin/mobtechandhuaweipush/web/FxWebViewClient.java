package com.kevin.mobtechandhuaweipush.web;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * @author Kevin  2020/11/16
 */
public class FxWebViewClient extends WebViewClient {
    private static final boolean DEBUG = true;
    private static final String TAG = "FxWebViewClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (DEBUG) Log.e(TAG, "shouldOverrideUrlLoading: view:" + view);
        Uri uri = request.getUrl();
        String uriValue = uri.toString();
        if (DEBUG) Log.e(TAG, "shouldOverrideUrlLoading: uri " + uri);
        //自定义功能
        if (uriValue.startsWith("nan://....")) {
            //自定义功能，比如打电话等
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (DEBUG) Log.e(TAG, "onPageStarted: ");
    }

    /**
     * 页面加载完毕调用(因为JS有些方法需要在页面加载完毕的时候才能调用，不然没有效果)
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        if (DEBUG) Log.e(TAG, "onPageFinished: ");
        super.onPageFinished(view, url);
    }


    @Override
    public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        if (DEBUG) Log.e(TAG, "shouldOverrideKeyEvent: ");

        return super.shouldOverrideKeyEvent(view, event);//返回true android端处理，返回false web端处理
    }
}
