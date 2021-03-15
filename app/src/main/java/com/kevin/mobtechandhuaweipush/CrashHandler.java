package com.kevin.mobtechandhuaweipush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author Kevin  2020/9/11
 * <p>
 * 捕获系统全局异常,这个类是在程序Appication初始化的时候使用，
 * </p>
 */
public final class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    @SuppressLint("StaticFieldLeak")
    private static volatile CrashHandler mCrashHandler = null;
    private Context context;

    private UncaughtExceptionHandler mDefaultHandler;
    private final Map<String, String> mMap = new HashMap<>();// 用来存储设备信息和异常信息
    public static final String LOG_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MobLog/";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());// 用于格式化日期作为日志文件名的


    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (mCrashHandler == null) {
            synchronized (CrashHandler.class) {
                if (mCrashHandler == null) {
                    mCrashHandler = new CrashHandler();
                }
            }
        }
        return mCrashHandler;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        this.context = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler(); // 获取系统默认的UncaughtException处理
        Thread.setDefaultUncaughtExceptionHandler(this);               // 设置该CrashHandler为程序的默认处理
    }

    public void release() {
        if (mCrashHandler != null)
            mCrashHandler = null;
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {

        try {
            boolean isHandle = handleException(throwable);
            if (!isHandle)
                Log.e(TAG, "uncaughtException: 抛出的异常为null，无法保存到本地文件中");
            mDefaultHandler.uncaughtException(thread, throwable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义错误处收集错误信息 发错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     * @see #saveCrashInfo2File(Throwable, boolean)
     */
    private boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        saveCrashInfo2File(throwable, true);
        throwable.printStackTrace();
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mMap.put("versionName", versionName);
                mMap.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mMap.put(field.getName(), Objects.requireNonNull(field.get(null)).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }


    /**
     * 获取错误报告文件
     */
    private File[] getErrorReportFiles() {
        File filesDir = new File(LOG_DIR);
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".log");
            }
        };
        if (filesDir.exists() && filesDir.isDirectory()) {
            return filesDir.listFiles(filter);
        } else {
            return null;
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param isCollectDeviceInfo 是否收集设备信息
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveCrashInfo2File(Throwable ex, boolean isCollectDeviceInfo) {

        //Todo 收集设备信息-----start------
        StringBuffer sb = null;
        if (isCollectDeviceInfo) {
            collectDeviceInfo(context);
            sb = new StringBuffer();
            for (Map.Entry<String, String> entry : mMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key);
                sb.append("=");
                sb.append(value);
                sb.append("\n");
            }
        }
        //Todo 收集设备信息-----end------
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result;
        if (isCollectDeviceInfo) {
            sb.append(writer.toString());
            result = sb.toString();
        } else {
            result = writer.toString();
        }

        try {
            String time = formatter.format(new Date());
            String fileName = time + ".txt";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = new File(LOG_DIR);
                if (!dir.exists() || !dir.isDirectory()) {
                    boolean isSuccess = dir.mkdirs();
                    Log.e(TAG, "saveCrashInfo2File: 生成异常捕获文件 <<" + (isSuccess ? "成功" : "失败") + ">>");
                }
                FileOutputStream fos = new FileOutputStream(LOG_DIR + fileName);
                fos.write(result.getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }
}
