package com.upwork.cameraproject.util;

import android.util.Log;

public class Logger {
    private static final String TAG = "CAMERA.PROJECT";
    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }
}
