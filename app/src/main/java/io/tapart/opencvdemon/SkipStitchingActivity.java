package io.tapart.opencvdemon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class SkipStitchingActivity extends AppCompatActivity {


    private Button captureBtn, saveBtn,saveBtn_me, button_take4_1_c, button_take4_2_c, button_take4_3_c, button_take4_4_c; // used to interact with capture and save Button in UI
    private SurfaceView mSurfaceView1_c,mSurfaceView2_c, mSurfaceView3_c, mSurfaceView4_c,  mSurfaceViewOnTop; // used to display the camera frame in UI
    private SurfaceView mSurfaceView1_x,mSurfaceView2_x, mSurfaceView3_x, mSurfaceView4_x; // used to display the camera frame in UI
    private ImageView mImageView_take4_1_x;
    private Camera mCam;
    private boolean isPreview; // Is the camera frame displaying?
    private boolean safeToTakePicture = true; // Is it safe to capture a picture?

    private List<Mat> listImage = new ArrayList<>();
    private Button btnCheck;

    Mat imageMat;
    Mat mat;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java4");
    }

    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    imageMat=new Mat();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip_stitching);

        btnCheck = findViewById(R.id.button_check);
        ImageView imageViewUpdate = findViewById(R.id.imageView_update);
        Bitmap pano1_3c = BitmapFactory.decodeResource(getResources(),R.drawable.pano1_3);
        imageViewUpdate.setImageBitmap(pano1_3c);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, "FloatingActionButton is clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                mat = new Mat();
                Bitmap pano1_1 = BitmapFactory.decodeResource(getResources(),R.drawable.pano1_1);
                Bitmap pano1_2 = BitmapFactory.decodeResource(getResources(),R.drawable.pano1_2);
                Bitmap pano1_3 = BitmapFactory.decodeResource(getResources(),R.drawable.pano1_3);

                Utils.bitmapToMat(pano1_1, mat);
                listImage.add(mat);
                Utils.bitmapToMat(pano1_2, mat);
                listImage.add(mat);
                Utils.bitmapToMat(pano1_3, mat);
                listImage.add(mat);
                makeText(getApplicationContext(), "Done stitching", Toast.LENGTH_LONG);

                Thread thread = new Thread(imageProcessingRunnable);
                thread.start();

            }
        });

    }

    View.OnClickListener saveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Thread thread = new Thread(imageProcessingRunnable);
            thread.start();
        }
    };

    ProgressDialog ringProgressDialog;

    private final Runnable imageProcessingRunnable = new Runnable() {
        @Override
        public void run() {
            showProcessingDialog();

            try {
                // Create a long array to store all image address
                int elems=  listImage.size();
                long[] tempobjadr = new long[elems];
                for (int i=0;i<elems;i++){
                    tempobjadr[i]=  listImage.get(i).getNativeObjAddr();
                }
                // Create a Mat to store the final panorama image
                Mat result = new Mat();

                // Call the Open CV C++ Code to perform stitching process
                NativePanorama.processPanorama(tempobjadr, result.getNativeObjAddr());

                Log.d("Vision", "Height " + result.rows() + " Width: " + result.cols());
                // Save the image to internal storage
                File sdcard = Environment.getExternalStorageDirectory();
                final String fileName = sdcard.getAbsolutePath() + "/opencv_" +  System.currentTimeMillis() + ".png";
                Imgcodecs.imwrite(fileName, result);



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        makeText(getApplicationContext(), "File saved at: " + fileName, LENGTH_LONG).show();

                        ImageView imageViewUpdate = findViewById(R.id.imageView_update);
                        Bitmap pano1_3c = BitmapFactory.decodeResource(getResources(),R.drawable.pano1_3);
                        imageViewUpdate.setImageBitmap(pano1_3c);

                    }
                });

                listImage.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

            closeProcessingDialog();
        }
    };

    private void showProcessingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ringProgressDialog = ProgressDialog.show(SkipStitchingActivity.this, "",	"Stitching in Progress", true);
                ringProgressDialog.setCancelable(false);
            }
        });
    }

    private void closeProcessingDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mCam.startPreview();
                ringProgressDialog.dismiss();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback(){
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                // Tell the camera to display the frame on this surfaceview
                mCam.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // Get the default parameters for camera
            Camera.Parameters myParameters = mCam.getParameters();
            // Select the best preview size
            Camera.Size myBestSize = getBestPreviewSize( myParameters );

            if(myBestSize != null){
                // Set the preview Size
                myParameters.setPreviewSize(myBestSize.width, myBestSize.height);
                // Set the parameters to the camera
                mCam.setParameters(myParameters);
                // Rotate the display frame 90 degree to view in portrait mode
                mCam.setDisplayOrientation(90);
                // Start the preview
                mCam.startPreview();
                isPreview = true;

                makeText(getApplicationContext(),
                        "Best Size:\n" +
                                String.valueOf(myBestSize.width) + " : " + String.valueOf(myBestSize.height),
                        LENGTH_SHORT).show();
            }
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    private Camera.Size getBestPreviewSize(Camera.Parameters parameters){
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for(int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(i);
            }
        }
        return bestSize;
    }


}