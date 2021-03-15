package com.kevin.mobtechandhuaweipush.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;


/**
 * @author Kevin  2020/9/22
 * 共用WebView设置
 */
public class FxWebViewBase extends WebView {
    private static final String TAG = "FxWebViewBase";
    private final Context context;

    public FxWebViewBase(@NonNull Context context) {
        this(context, null);
    }

    public FxWebViewBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        //this(context, attrs, 0);//Todo 注意这里不能将 attrs的属性设置为0(如果为0，webView编辑文本点击时，无法弹出软键盘)
        this(context, attrs, Resources.getSystem().getIdentifier("webViewStyle", "attr", "android"));
    }

    public FxWebViewBase(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FxWebViewBase(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initSetting();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initSetting() {
        Log.e(TAG, "initSetting: -----------------------------");
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);                    //设置WebView是否允许执行JavaScript脚本，默认false，不允许。
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//让JavaScript自动打开窗口，默认false。适用于JavaScript方法window.open()。

        //webSettings.setBlockNetworkImage(false);
        //webSettings.setDomStorageEnabled(true);
        //webSettings.setAppCachePath();
        //webSettings.setAppCacheEnabled();


        //=============================缓存机制的相关设置===========================================
        webSettings.setAllowFileAccess(true);//是否允许访问文件，默认允许。注意，这里只是允许或禁止对文件系统的访问，Assets 和 resources 文件使用file:///android_asset和file:///android_res仍是可访问的。
        webSettings.setAppCacheEnabled(true);//应用缓存API是否可用，默认值false, 结合setAppCachePath(String)使用。
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);//设置app缓存容量
        webSettings.setAppCachePath(getDefaultWebCacheFile(context).getPath());//设置缓存路径 Todo 注:测试发现，设置自定义的默认路径没有生效，还是用的系统默认的 /data/user/0/com.fuxing.ai.water/cache/WebView目录下
        webSettings.setDatabaseEnabled(true);//数据库存储API是否可用，默认值false。
//		webSettings.setDatabasePath(mContext.getApplicationContext().getDir("databases", 0).getPath());
        webSettings.setDomStorageEnabled(true);// 使用localStorage则必须打开, 支持文件存储
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setGeolocationDatabasePath(context.getApplicationContext().getDir("geolocation", 0).getPath());

        //==============================webview页面自适应屏幕的相关设置===========================================
        // webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕 设置布局，会引起WebView的重新布局（relayout）,默认值NARROW_COLUMNS
        // webSettings.setDefaultTextEncodingName(ENCODENAME);//设置网页默认编码
        // webSettings.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        // webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSettings.setLoadWithOverviewMode(true);//是否允许WebView度超出以概览的方式载入页面，默认false。即缩小内容以适应屏幕宽度
        // webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕 设置布局，会引起WebView的重新布局（relayout）,默认值NARROW_COLUMNS
        webSettings.setSupportZoom(false);        //是否应该支持使用其屏幕缩放控件和手势缩放,默认值true Todo 在Android 6.0设备上打开缩放会报错
        webSettings.setBuiltInZoomControls(false);//设置触摸可缩放，默认值为false。
        webSettings.setUseWideViewPort(true);     //设置此属性，可任意比例缩放。
        webSettings.setSupportMultipleWindows(false);//设置WebView是否支持多窗口。如果设置为true，主程序要实现onCreateWindow(WebView, boolean, boolean, Message)，默认false。
        //webSettings.setTextZoom(100);

        setCanCopyText(false);

    }

    /**
     * 设置是否可以通过长按事件复制webView中的文本
     * (不调用该方法默认可以复制)
     *
     * @param isCan true 能复制
     */
    public void setCanCopyText(final boolean isCan) {
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return !isCan;
            }
        });
    }

    /**
     * 获取默认的WebView缓存文件
     */
    private static File getDefaultWebCacheFile(Context context) {
        return context.getApplicationContext().getDir("appCache", 0);
    }
}
