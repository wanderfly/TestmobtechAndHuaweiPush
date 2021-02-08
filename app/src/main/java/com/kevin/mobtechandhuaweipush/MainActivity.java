package com.kevin.mobtechandhuaweipush;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.aaid.entity.AAIDResult;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAAID();
            }
        }).start();
    }

    public void getAAID() {
        Task<AAIDResult> idResult = HmsInstanceId.getInstance(this).getAAID();
        idResult.addOnSuccessListener(new OnSuccessListener<AAIDResult>() {
            @Override
            public void onSuccess(AAIDResult aaidResult) {
                // 获取AAID方法成功
                String aaid = aaidResult.getId();
                Log.e(TAG, "getAAID success:" + aaid );
                getToken(aaid,"HCM");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // 获取AAID失败
                Log.e(TAG, "getAAID failure:" + e);
            }
        });
    }

    public void getToken(String aaid,String scope) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String token=HmsInstanceId.getInstance(MainActivity.this).getToken(aaid, scope);
                    Log.e(TAG, "getToken: "+token);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "getToken: "+e.toString());
                }
            }
        }).start();
    }
}