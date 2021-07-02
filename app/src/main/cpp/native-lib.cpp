#include <jni.h>
#include <string>
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/stitching.hpp"
//#include <opencv2/stitching.hpp>
//#include "stitching.hpp"
//#include <opencv2/stitching/stitcher.hpp>
#include <iostream>
//#include "com_example_panorama_NativePanorama.h"

// CPP program to Stitch
// input images (panorama) using OpenCV
#include <iostream>
#include <fstream>

// Include header files from OpenCV directory
// required to stitch images.
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/stitching.hpp"
#include <jni.h>

#include "opencv2/opencv.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui.hpp"
#include "opencv2/stitching.hpp"

//#include <opencv2/stitching/stitcher.hpp>

using namespace std;
using namespace cv;


// Define mode for stitching as panoroma
// (One out of many functions of Stitcher)
Stitcher::Mode mode = Stitcher::PANORAMA;

// Array for pictures
//vector <Mat> imgs;

extern "C" jintArray
Java_io_tapart_opencvdemon_MainActivity_stitchImages_12(JNIEnv *env, jclass clazz,
                                                         jobjectArray argv) {
    jstring jstr;

    jsize len = env->GetArrayLength(argv);
    std::vector<cv::Mat> imgs;

    // Get all the images that need to be
    // stitched as arguments from command line
    for (int i = 1; i < len; ++i)
    {
        // Read the ith argument or image
        // and push into the image array
        jstr = (jstring) env->GetObjectArrayElement(argv, i);
        const char *path = (char *) env->GetStringUTFChars(jstr, 0);
        cv::Mat img = cv::imread(path);
        imgs.push_back(img);
    }
    cv::Mat pano;
    cv::Ptr<Stitcher> stitcher = cv::Stitcher::create(mode);
    cv::Stitcher::Status status = stitcher->stitch(imgs, pano);

    cv::imwrite("result.jpg", pano);

}

//extern "C" JNIEXPORT jstring JNICALL
//Java_io_tapart_cppapp_MainActivity_stringFromJNI(
//        JNIEnv* env,
//        jobject /* this */) {
//    std::string hello = "Hello from C++ Good";
//    return env->NewStringUTF(hello.c_str());
//}



#define BORDER_GRAY_LEVEL 0

#include <android/log.h>
#include <android/bitmap.h>

#define LOG_TAG    "DDLog-jni"
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG, __VA_ARGS__)
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG, __VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG, __VA_ARGS__)
#define LOGF(...)  __android_log_print(ANDROID_LOG_FATAL,LOG_TAG, __VA_ARGS__)
using namespace cv;
using namespace std;
char filepath1[100] = "/storage/emulated/0/panorama_stitched.jpg";


cv::Mat finalMat;




extern "C" jintArray
Java_io_tapart_opencvdemon_ImagesStitchUtil_stitchImages(JNIEnv *env, jclass clazz,
                                                         jobjectArray paths) {
    // TODO: implement stitchImages()

    jstring jstr;

    jsize len = env->GetArrayLength(paths);
    std::vector<cv::Mat> mats;
    for (int i = 0; i < len; i++) {
        jstr = (jstring) env->GetObjectArrayElement(paths, i);
        const char *path = (char *) env->GetStringUTFChars(jstr, 0);
        LOGI("path %s", path);
        cv::Mat mat = cv::imread(path);
//        cvtColor(mat, mat, CV_RGBA2RGB);
        mats.push_back(mat);
    }

    LOGI ("Start stitching...");
    // Define mode for stitching as panoroma
// (One out of many functions of Stitcher)

    cv::Stitcher stitcher;
//    stitcher = cv::Stitcher::createDefault(false);
//    stitcher = cv::Stitcher::createDefault(false);


    //stitcher.setRegistrationResol(0.6);
    // stitcher.setWaveCorrection(false);
    /*=match_conf defaults to 0.65, I choose 0.8, if there is too much feature, there will be no feature points, and 0.8 will fail*/
    detail::BestOf2NearestMatcher *matcher = new detail::BestOf2NearestMatcher(false, 0.5f);
    stitcher.setFeaturesMatcher(matcher);
    stitcher.setBundleAdjuster(new detail::BundleAdjusterRay());
    stitcher.setSeamFinder(new detail::NoSeamFinder);
    stitcher.setExposureCompensator(new detail::NoExposureCompensator());//exposure compensation
    stitcher.setBlender(new detail::FeatherBlender());

    Stitcher::Status state = stitcher.stitch(mats, finalMat);

    //This time finalMat is bgr type

    LOGI ("splicing result: %d", state);
//        finalMat = clipping(finalMat);
    jintArray jint_arr = env->NewIntArray(3);
    jint *elems = env->GetIntArrayElements(jint_arr, NULL);
    elems[0] = state;//status code
    elems[1] = finalMat.cols;//wide
    elems[2] = finalMat.rows;//high

    if (state == cv::Stitcher::OK){
        LOGI ("splicing success: OK");
    }else{
        LOGI ("splicing failure: fail code %d", state);
    }
    //Synchronize
    env->ReleaseIntArrayElements(jint_arr, elems, 0);
//    bool isSave  = cv::imwrite(filepath1, finalMat);
    // LOGI ("whether it is stored successfully: %d", isSave);
    return jint_arr;

}

//extern "C"
//JNIEXPORT void JNICALL
extern "C" void
Java_io_tapart_opencvdemon_ImagesStitchUtil_getMat(JNIEnv *env, jclass clazz,
                                                   jlong mat) {
// TODO: implement getMat()

    LOGI ("Getting mat...");
    Mat *res = (Mat *) mat;
    res->create(finalMat.rows, finalMat.cols, finalMat.type());
    memcpy(res->data, finalMat.data, finalMat.rows * finalMat.step);
    LOGI ("Get Success");

}


// Convert mat to bitmap
void MatToBitmap(JNIEnv *env, Mat &mat, jobject &bitmap, jboolean needPremultiplyAlpha) {
//    AndroidBitmapInfo info;
//    void *pixels = 0;
//    Mat &src = mat;
//
//
//    try {
//
//        LOGD("nMatToBitmap");
//        CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
//        LOGD("nMatToBitmap1");
//
//        CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
//                  info.format == ANDROID_BITMAP_FORMAT_RGB_565);
//        LOGD("nMatToBitmap2 :%d  : %d  :%d", src.dims, src.rows, src.cols);
//
//        CV_Assert(src.dims == 2 && info.height == (uint32_t) src.rows &&
//                  info.width == (uint32_t) src.cols);
//        LOGD("nMatToBitmap3");
//        CV_Assert(src.type() == CV_8UC1 || src.type() == CV_8UC3 || src.type() == CV_8UC4);
//        LOGD("nMatToBitmap4");
//        CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
//        LOGD("nMatToBitmap5");
//        CV_Assert(pixels);
//        LOGD("nMatToBitmap6");
//
//
//        if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
//            Mat tmp(info.height, info.width, CV_8UC4, pixels);
////            Mat tmp(info.height, info.width, CV_8UC3, pixels);
//            if (src.type() == CV_8UC1) {
//                LOGD("nMatToBitmap: CV_8UC1 -> RGBA_8888");
//                cvtColor(src, tmp, COLOR_GRAY2RGBA);
//            } else if (src.type() == CV_8UC3) {
//                LOGD("nMatToBitmap: CV_8UC3 -> RGBA_8888");
////                cvtColor(src, tmp, COLOR_RGB2RGBA);
////                cvtColor(src, tmp, COLOR_RGB2RGBA);
//                cvtColor(src, tmp, COLOR_BGR2RGBA);
////                src.copyTo(tmp);
//            } else if (src.type() == CV_8UC4) {
//                LOGD("nMatToBitmap: CV_8UC4 -> RGBA_8888");
//                if (needPremultiplyAlpha)
//                    cvtColor(src, tmp, COLOR_RGBA2mRGBA);
//                else
//                    src.copyTo(tmp);
//            }
//        } else {
//            // info.format == ANDROID_BITMAP_FORMAT_RGB_565
//            Mat tmp(info.height, info.width, CV_8UC2, pixels);
//            if (src.type() == CV_8UC1) {
//                LOGD("nMatToBitmap: CV_8UC1 -> RGB_565");
//                cvtColor(src, tmp, COLOR_GRAY2BGR565);
//            } else if (src.type() == CV_8UC3) {
//                LOGD("nMatToBitmap: CV_8UC3 -> RGB_565");
////                src.copyTo(tmp);
//                cvtColor(src, tmp, COLOR_RGB2BGR565);
//            } else if (src.type() == CV_8UC4) {
//                LOGD("nMatToBitmap: CV_8UC4 -> RGB_565");
//                cvtColor(src, tmp, COLOR_RGBA2BGR565);
//            }
//        }
//        AndroidBitmap_unlockPixels(env, bitmap);
//        return;
//    } catch (const cv::Exception &e) {
//        AndroidBitmap_unlockPixels(env, bitmap);
//        LOGE("nMatToBitmap catched cv::Exception: %s", e.what());
//        jclass je = env->FindClass("org/opencv/core/CvException");
//        if (!je) je = env->FindClass("java/lang/Exception");
//        env->ThrowNew(je, e.what());
//        return;
//    } catch (...) {
//        AndroidBitmap_unlockPixels(env, bitmap);
//        LOGE("nMatToBitmap catched unknown exception (...)");
//        jclass je = env->FindClass("java/lang/Exception");
//        env->ThrowNew(je, "Unknown exception in JNI code {nMatToBitmap}");
//        return;
//    }
}

extern "C" jint
Java_io_tapart_opencvdemon_ImagesStitchUtil_getBitmap(JNIEnv *env, jclass clazz,
                                                      jobject bitmap) {
    // TODO: implement getBitmap()
    if (finalMat.dims != 2){
        return -1;
    }

    MatToBitmap(env,finalMat,bitmap,false);

    return 0;

}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_tapart_opencvdemon_MainActivity_stitchImages_1(JNIEnv *env, jclass clazz,
                                                        jobjectArray paths) {

    return paths;
}


extern "C"
JNIEXPORT jintArray JNICALL
Java_io_tapart_opencvdemon_MainActivity_stitchImages_112(JNIEnv *env, jclass clazz,
                                                        jobjectArray paths) {
    // TODO: implement stitchImages_1()
    jstring jstr;

    jsize len = env->GetArrayLength(paths);
    std::vector<cv::Mat> imgs;

    // Get all the images that need to be
    // stitched as arguments from command line
    for (int i = 1; i < len; ++i)
    {
        // Read the ith argument or image
        // and push into the image array
        jstr = (jstring) env->GetObjectArrayElement(paths, i);
        const char *path = (char *) env->GetStringUTFChars(jstr, 0);
        cv::Mat img = cv::imread(path);
        imgs.push_back(img);
    }
    cv::Mat pano;
    cv::Ptr<Stitcher> stitcher = cv::Stitcher::create(mode);
    cv::Stitcher::Status status = stitcher->stitch(imgs, pano);

    cv::imwrite("result.jpg", pano);

    jintArray jint_arr = env->NewIntArray(3);
    return jint_arr;
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_tapart_opencvdemon_MainActivity_stitchImages_11(JNIEnv *env, jclass clazz,
                                                        jobjectArray paths) {
    // TODO: implement stitchImages_1()
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_tapart_opencvdemon_MainActivity_stitchImages_1p(JNIEnv *env, jclass clazz,
                                                        jobjectArray paths) {
    // TODO: implement stitchImages_p()
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_io_tapart_opencvdemon_MainActivity_stitchIt(JNIEnv *env, jclass clazz, jobjectArray paths) {
    // TODO: implement stitchIt()

    jstring jstr;

    jsize len = env->GetArrayLength(paths);
    std::vector<cv::Mat> imgs;

    // Get all the images that need to be
    // stitched as arguments from command line
    for (int i = 1; i < len; ++i)
    {
        // Read the ith argument or image
        // and push into the image array
        jstr = (jstring) env->GetObjectArrayElement(paths, i);
        const char *path = (char *) env->GetStringUTFChars(jstr, 0);
        cv::Mat img = cv::imread(path);
        imgs.push_back(img);
    }
    cv::Mat pano;
//    cv::Ptr<Stitcher> stitcher = cv::Stitcher::create(mode);
//    cv::Stitcher::Status status = stitcher->stitch(imgs, pano);
//
//    cv::imwrite("result.jpg", pano);

    return paths;
}

extern "C"
JNIEXPORT void JNICALL
Java_io_tapart_opencvdemon_NativePanorama_processPanorama(JNIEnv *env, jclass clazz,
                                                          jlongArray imageAddressArray,
                                                          jlong outputAddress) {
    // TODO: implement processPanorama()
    // Get the length of the long array
    jsize a_len = env->GetArrayLength(imageAddressArray);

    // Convert the jlongArray to an array of jlong
    jlong *imgAddressArr = env->GetLongArrayElements(imageAddressArray,0);

    // Create a vector to store all the image
    vector< Mat > imgVec;
    for(int k=0;k<a_len;k++)
    {
        // Get the image
        Mat & curimage=*(Mat*)imgAddressArr[k];
        Mat newimage;

        // Convert to a 3 channel Mat to use with Stitcher module
        cvtColor(curimage, newimage, COLOR_BGRA2RGB);

        // Reduce the resolution for fast computation
//        float scale = 1000.0f / curimage.rows;
//        resize(newimage, newimage, Size(scale * curimage.rows, scale * curimage.cols));

        imgVec.push_back(newimage);
    }

    Mat & result  = *(Mat*) outputAddress;

//    Stitcher stitcher;
//    stitcher = cv::Stitcher::createDefault();
//    stitcher = Stitcher::createStitcher();
//    stitcher.stitch(imgVec, result);

// Define mode for stitching as panoroma
// (One out of many functions of Stitcher)
//    Stitcher::Mode mode = Stitcher::PANORAMA;
    Stitcher::Mode mode = Stitcher::SCANS;

// Array for pictures
//    vector<Mat> imgs;

    // Create a Stitcher class object with mode panoroma
    Ptr<Stitcher> stitcher = Stitcher::create(mode);

    // Command to stitch all the images present in the image array
    Stitcher::Status status = stitcher->stitch(imgVec, result);


    env->ReleaseLongArrayElements(imageAddressArray, imgAddressArr ,0);
}

extern "C"
JNIEXPORT void JNICALL
Java_io_tapart_opencvdemon_NativePanorama_processPanoramaC(JNIEnv *env, jclass clazz,
                                                          jlongArray imageAddressArray,
                                                          jlong outputAddress) {
    // TODO: implement processPanorama()
    // Get the length of the long array
    jsize a_len = env->GetArrayLength(imageAddressArray);

    // Convert the jlongArray to an array of jlong
    jlong *imgAddressArr = env->GetLongArrayElements(imageAddressArray,0);

    // Create a vector to store all the image
    vector< Mat > imgVec;
    for(int k=0;k<a_len;k++)
    {
        // Get the image
        Mat & curimage=*(Mat*)imgAddressArr[k];
        Mat newimage;

        // Convert to a 3 channel Mat to use with Stitcher module
        cvtColor(curimage, newimage, COLOR_BGRA2RGB);

        // Reduce the resolution for fast computation
        float scaleC = 1000.0f / curimage.cols;
        float scaleR = 1000.0f / curimage.rows;
        resize(newimage, newimage, Size(scaleR * curimage.rows, scaleC * curimage.cols));

        imgVec.push_back(newimage);
    }

    Mat & result  = *(Mat*) outputAddress;

//    Stitcher stitcher;
//    stitcher = cv::Stitcher::createDefault();
//    stitcher = Stitcher::createStitcher();
//    stitcher.stitch(imgVec, result);

// Define mode for stitching as panoroma
// (One out of many functions of Stitcher)
//    Stitcher::Mode mode = Stitcher::PANORAMA;
    Stitcher::Mode mode = Stitcher::SCANS;

// Array for pictures
//    vector<Mat> imgs;

    // Create a Stitcher class object with mode panoroma
    Ptr<Stitcher> stitcher = Stitcher::create(mode);

    // Command to stitch all the images present in the image array
    Stitcher::Status status = stitcher->stitch(imgVec, result);


    env->ReleaseLongArrayElements(imageAddressArray, imgAddressArr ,0);
}