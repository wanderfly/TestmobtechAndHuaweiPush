package com.kevin.mobtechandhuaweipush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.aaid.entity.AAIDResult;
import com.kevin.mobtechandhuaweipush.web.WebViewActivity;
import com.mob.pushsdk.MobPushUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String WEB_INTENT_URL_KEY = "intent_url_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseMobTech();
        getDeviceInfo();
        if (isHonor() || isHuaWei()) {
            new Thread(this::getAAID).start();
        }
    }


    private void getDeviceInfo() {
        Log.e(TAG, "getDeviceInfo: BRAND:" + Build.BRAND + " BOARD:" + Build.BOARD + " MANUFACTURER:" + Build.MANUFACTURER + " PRODUCT:" + Build.PRODUCT);
    }

    private boolean isHonor() {
        return Objects.equals("HONOR", Build.BRAND);
    }

    private boolean isHuaWei() {
        return Objects.equals("HUAWEI", Build.BRAND);
    }

    private boolean isXiaoMi() {
        return Objects.equals("Xiaomi", Build.BRAND);
    }

    private void parseMobTech() {
        JSONArray jsonArray = MobPushUtils.parseSchemePluginPushIntent(getIntent());
        Log.e(TAG, "parseMobTech: jsonArray:" + jsonArray.toString());
        int size = jsonArray.length();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Log.e(TAG, "parseMobTech: obj.toString()" + obj.toString());
                    String url = obj.getString("url").replace("\\/", "/");
                    Log.e(TAG, "parseMobTech: newUrl:" + url);
                    toWebActivity(url, WebViewActivity.class);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "parseMobTech: " + e.toString());
                }
            }
        }
    }

    private void toWebActivity(String url, Class<?> webActivity) {
        Intent intent = new Intent(this, webActivity);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(WEB_INTENT_URL_KEY, url);
        startActivity(intent);
        //overridePendingTransition(0,0);//Todo 去除动画
    }


    public void getAAID() {
        Task<AAIDResult> idResult = HmsInstanceId.getInstance(this).getAAID();
        idResult.addOnSuccessListener(new OnSuccessListener<AAIDResult>() {
            @Override
            public void onSuccess(AAIDResult aaidResult) {
                // 获取AAID方法成功
                String aaid = aaidResult.getId();
                Log.e(TAG, "getAAID success:" + aaid);
                getToken(aaid, "HCM");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 获取AAID失败
                Log.e(TAG, "getAAID failure:" + e);
            }
        });
    }

    public void getToken(String aaid, String scope) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HmsInstanceId hmsInstanceId = HmsInstanceId.getInstance(MainActivity.this);
                    String token = hmsInstanceId.getToken(aaid, scope);
                    Log.e(TAG, "getToken: hmsInstanceId:" + hmsInstanceId + " token:" + token);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "getToken: 出错" + e.toString());
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String[] permissions = PermissionUtil.generatePermissionArray(this);
        if (permissions.length > 0) {
            PermissionUtil.requestBase(this, 110, permissions);
        }
    }
}