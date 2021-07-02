package io.tapart.opencvdemon;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;

import java.io.File;

public class ImagesStitchUtil {
    public final static int OK = 0;
    public final static int ERR_NEED_MORE_IMGS = 1;
    public final static int ERR_HOMOGRAPHY_EST_FAIL = 2;
    public final static int ERR_CAMERA_PARAMS_ADJUST_FAIL = 3;

    static {
        System.loadLibrary("Stitcher");
    }


    public static void StitchImages(String paths[], @NonNull onStitchResultListener listener) {
        for (String path : paths) {
            if (!new File(path).exists()) {
                listener.onError("Unable to read file or file does not exist:" + path);
                return;
            }
        }
        int wh[] = stitchImages(paths);
        switch (wh[0]) {
            case OK: {
                Bitmap bitmap = Bitmap.createBitmap(wh[1], wh[2], Bitmap.Config.ARGB_8888);
                int result = getBitmap(bitmap);
                if (result == OK && bitmap != null) {
                    listener.onSuccess(bitmap);
                } else {
                    listener.onError("Image synthesis failed");
                }
            }
            break;
            case ERR_NEED_MORE_IMGS: {
                listener.onError("require more images");
                return;
            }
            case ERR_HOMOGRAPHY_EST_FAIL: {
                listener.onError("Image does not correspond");
                return;
            }
            case ERR_CAMERA_PARAMS_ADJUST_FAIL: {
                listener.onError("Image parameter processing failed");
                return;
            }
        }
    }


    private native static int[] stitchImages(String path[]);

    private native static void getMat(long mat);

    private native static int getBitmap(Bitmap bitmap);


    public interface onStitchResultListener {

        void onSuccess(Bitmap bitmap);

        void onError(String errorMsg);
    }


}