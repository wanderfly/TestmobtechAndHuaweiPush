package com.kevin.mobtechandhuaweipush.web;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.kevin.mobtechandhuaweipush.MainActivity;
import com.kevin.mobtechandhuaweipush.R;


/**
 * @author Kevin  2020/9/14
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private static final boolean DEBUG = true;
    private static final String TAG = "WebViewActivity";
    private LinearLayout mLlWebViewContainer;
    private FxWebViewBase mWebView;
    public static String url;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Log.e(TAG, "onCreate: ==================");
        initUrlByIntentType();
        initWebView();
    }

    private void initWebView() {
        mLlWebViewContainer = findViewById(R.id.ll_web_view);
        mWebView = findViewById(R.id.web_view_base);
        ProgressBar progressBar = findViewById(R.id.progress_web_view_base);
        mWebView.setWebViewClient(new FxWebViewClient());
        mWebView.setWebChromeClient(new FxWebChromeClient(progressBar));
        //mWebView.addJavascriptInterface(new JsInterface(this, mWebView), JsInterfaceBase.JS_INTERFACE_NAME);
        mWebView.loadUrl(url);
        if (DEBUG) Log.e(TAG, "initWebView: " + url);
    }


    private void initUrlByIntentType() {
        url = getIntent().getStringExtra(MainActivity.WEB_INTENT_URL_KEY);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mLlWebViewContainer.removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
