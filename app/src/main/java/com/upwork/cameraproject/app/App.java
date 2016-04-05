package com.upwork.cameraproject.app;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Pair;

import com.upwork.cameraproject.util.FileUtils;
import com.upwork.cameraproject.util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class App extends Application {
//    static {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectAll()
//                .penaltyLog()
//                .build());
//    }

    private static Context context;
    private String appDirPath;
    private String ffmpegBinaryPath;

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
        testFFMpeg();
    }

    private void initAppDir() {
        File appDirFile = getFilesDir();

        if (appDirFile == null) {
            throw new RuntimeException("Failed to init app directory");
        }

        appDirPath = appDirFile.getPath();
    }

    private void initFFMpeg() {
        ffmpegBinaryPath = appDirPath + File.separator + "ffmpeg";
        FileUtils.deleteRecursive(ffmpegBinaryPath);
        if (!FileUtils.copyAssetFile("ffmpeg", ffmpegBinaryPath, getAssets())) {
            throw new RuntimeException("Failed to copy ffmpeg binary");
        }
        try {
            Logger.e(FileUtils.runBinary("chmod 744 " + ffmpegBinaryPath));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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

    private void testFFMpeg() {
        try {
            Logger.e(FileUtils.runBinary(ffmpegBinaryPath));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
