package com.upwork.cameraproject.app;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Pair;

import com.upwork.cameraproject.util.FileUtils;

import java.io.File;

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
    private String appDirPath;

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
        context = getApplicationContext();
        initAppDir();
        initAsyncTask();
        initFFMpeg();
    }

    private void initAppDir() {
        File appDirFile;
        if (null == (appDirFile = getExternalFilesDir(null))) {
            appDirFile = getFilesDir();
        }

        if (appDirFile == null) {
            throw new RuntimeException("Failed to init app directory");
        }

        appDirPath = appDirFile.getPath();
    }
    private void initFFMpeg() {
        if (!FileUtils.copyAssetFile("ffmpeg", appDirPath + File.separator + "ffmpeg", getAssets())) {
            throw new RuntimeException("Failed to copy ffmpeg binary");
        }
        try {
            Class.forName("com.upwork.cameraproject.ffmpeg.FFMpegHelper");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
