#ifndef TRACE__H
#define TRACE__H

#define TRACE_LEVEL 2

#include <stdio.h>
#include <sys/stat.h>
#include <sys/time.h>

#include <android/log.h>
#include <errno.h>

#define _FAIL_T(__x__, ...)    "[%s] "   __x__ "\n", __FUNCTION__, ##__VA_ARGS__
#define _WARN_T(__x__, ...)    "[%s] "   __x__ "\n", __FUNCTION__, ##__VA_ARGS__
#define _SUCC_T(__x__, ...)    "[%s] "   __x__ "\n", __FUNCTION__, ##__VA_ARGS__
#define _RSLT_T(__x__, ...)    "[%s] "   __x__ "\n", __FUNCTION__, ##__VA_ARGS__
#define    _N_T(__x__, ...)    "[%s] "   __x__ "\n", __FUNCTION__, ##__VA_ARGS__

#if TRACE_LEVEL > 1

#define TRACE_F(__x__, ...)     __android_log_print(ANDROID_LOG_ERROR, "NATIVE", _FAIL_T(__x__, ##__VA_ARGS__))
#define TRACE_W(__x__, ...)     __android_log_print(ANDROID_LOG_INFO, "NATIVE", _WARN_T(__x__, ##__VA_ARGS__))
#define TRACE_S(__x__, ...)     __android_log_print(ANDROID_LOG_INFO, "NATIVE", _SUCC_T(__x__, ##__VA_ARGS__))
#define TRACE_R(__x__, ...)     __android_log_print(ANDROID_LOG_INFO, "NATIVE", _RSLT_T(__x__, ##__VA_ARGS__))
#define TRACE_N(__x__, ...)     __android_log_print(ANDROID_LOG_INFO, "NATIVE",    _N_T(__x__, ##__VA_ARGS__))
#define TRACE(__x__, ...)       __android_log_print(ANDROID_LOG_INFO, "NATIVE", __x__, ##__VA_ARGS__)

#elif TRACE_LEVEL == 1

#define TRACE_F(__x__, ...)     __android_log_print(ANDROID_LOG_ERROR, "NATIVE", _FAIL_T(__x__, ##__VA_ARGS__))
#define TRACE_W(__x__, ...)
#define TRACE_S(__x__, ...)
#define TRACE_R(__x__, ...)
#define TRACE_N(__x__, ...)
#define TRACE(__x__, ...)

#else

#define TRACE_F(__x__, ...)
#define TRACE_W(__x__, ...)
#define TRACE_S(__x__, ...)
#define TRACE_R(__x__, ...)
#define TRACE_N(__x__, ...)
#define TRACE(__x__, ...)

#endif //DISABLE_TRACE

#pragma GCC poison cout
#pragma GCC poison printf

#endif // TRACE__H
