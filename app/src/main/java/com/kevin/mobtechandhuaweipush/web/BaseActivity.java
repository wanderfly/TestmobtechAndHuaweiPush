package com.kevin.mobtechandhuaweipush.web;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @author Kevin  2020/11/27
 */
public class BaseActivity extends AppCompatActivity {
    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Todo  设置状态栏在遇到高亮背景时，显示灰色(因为这里Activity的主题默认设置为全屏 android:theme="@style/MyAppTheme")
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 设置字体大小Activity界面字体不随系统大小变化而变化
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

}
