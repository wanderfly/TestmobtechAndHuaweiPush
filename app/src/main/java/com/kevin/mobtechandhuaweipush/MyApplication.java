package com.kevin.mobtechandhuaweipush;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCustomMessage;
import com.mob.pushsdk.MobPushNotifyMessage;
import com.mob.pushsdk.MobPushReceiver;

/**
 * @author Kevin  2021/2/19
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        MobPush.addPushReceiver(mobPushReceiver);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        MobPush.removePushReceiver(mobPushReceiver);
    }

    MobPushReceiver mobPushReceiver = new MobPushReceiver() {
        @Override
        public void onCustomMessageReceive(Context context, MobPushCustomMessage mobPushCustomMessage) {
            Log.e(TAG, "onCustomMessageReceive:" + mobPushCustomMessage.toString());
        }

        @Override
        public void onNotifyMessageReceive(Context context, MobPushNotifyMessage mobPushNotifyMessage) {
            Log.e(TAG, "onNotifyMessageReceive:" + mobPushNotifyMessage.toString());
        }

        @Override
        public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage mobPushNotifyMessage) {
            Log.e(TAG, "onNotifyMessageOpenedReceive: " + mobPushNotifyMessage.toString());
        }

        @Override
        public void onTagsCallback(Context context, String[] strings, int i, int i1) {
            Log.e(TAG, "onTagsCallback: ----------start-----------");
            for (String s : strings) {
                Log.e(TAG, "onTagsCallback: " + s);
            }
            Log.e(TAG, "onTagsCallback: ----------end-----------");
        }

        @Override
        public void onAliasCallback(Context context, String s, int i, int i1) {
            Log.e(TAG, "onAliasCallback: "+s);
        }
    };

}
