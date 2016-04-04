#include "com_upwork_cameraproject_ffmpeg_FFMpegHelper.h"
#include <libavformat/avformat.h>

#include "trace.h"

JNIEXPORT void FFMPEG_HELPER_FUNCTION(nativeInit)(JNIEnv* env, jobject self) {
    TRACE_N("Hello world");
    av_register_all();
}
