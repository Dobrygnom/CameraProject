LOCAL_PATH:= $(call my-dir)

#build ffmpeg
include ffmpeg/android/Android.mk

#build nativemedia
include $(CLEAR_VARS)

LOCAL_MODULE    := camera

LOCAL_SRC_FILES := FFMpegHelper.c
LOCAL_LDLIBS := -llog -ljnigraphics -lz -landroid
LOCAL_STATIC_LIBRARIES := libavformat-57 libavcodec-57 libswscale-4 libavutil-55
include $(BUILD_SHARED_LIBRARY)
