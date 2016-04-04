package com.upwork.cameraproject.app;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;

import com.upwork.cameraproject.ffmpeg.FFMpegHelper;

public class App extends Application {
    static {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
    }

    private static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = this;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAsyncTask();
        context = getApplicationContext();
        FFMpegHelper.test();
    }

    private void initAsyncTask() {
        // workaround for http://code.google.com/p/android/issues/detail?id=20915
        try {
            Class.forName("android.os.AsyncTask");
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static Pair<Integer, Integer> getDisplayMetrics() {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        return new Pair<>(metrics.widthPixels, metrics.heightPixels);
    }
}
