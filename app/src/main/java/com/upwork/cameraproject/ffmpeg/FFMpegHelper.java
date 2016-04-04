package com.upwork.cameraproject.ffmpeg;

public class FFMpegHelper {
    static {
        System.loadLibrary("avutil-55");
        System.loadLibrary("swscale-4");
        System.loadLibrary("avcodec-57");
        System.loadLibrary("avformat-57");
        System.loadLibrary("camera");
        nativeInit();
    }

    private static native void nativeInit();

    public static void test() {

    }
}
