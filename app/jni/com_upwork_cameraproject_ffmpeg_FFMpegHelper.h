#ifndef COM_UPWORK_CAMERAPROJECT_FFMPEG_FFMPEGHELPER__H
#define COM_UPWORK_CAMERAPROJECT_FFMPEG_FFMPEGHELPER__H

#include <jni.h>

#define FFMPEG_HELPER_FUNCTION(a) JNICALL Java_com_upwork_cameraproject_ffmpeg_FFMpegHelper_##a

JNIEXPORT void FFMPEG_HELPER_FUNCTION(nativeInit)(JNIEnv *env, jobject self);

#endif //COM_UPWORK_CAMERAPROJECT_FFMPEG_FFMPEGHELPER__H