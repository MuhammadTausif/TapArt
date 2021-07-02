package io.tapart.opencvdemon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import android.graphics.Camera;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.hardware.Camera;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Environment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;

public class CameraPreviewActivity extends AppCompatActivity {

    private Button btnUP, btnDN, btnTest, captureBtn, saveBtn, saveBtn_me, button_take4_1_c, button_take4_2_c, button_take4_3_c, button_take4_4_c; // used to interact with capture and save Button in UI
    private SurfaceView mSurfaceView1_c, mSurfaceView2_c, mSurfaceView3_c, mSurfaceView4_c, mSurfaceViewOnTop; // used to display the camera frame in UI
    private SurfaceView mSurfaceView1_x, mSurfaceView2_x, mSurfaceView3_x, mSurfaceView4_x; // used to display the camera frame in UI
    private ImageView mImageView_take4_1_x;
    private Camera mCam;
    private boolean isPreview; // Is the camera frame displaying?
    private boolean safeToTakePicture = true; // Is it safe to capture a picture?
    Bitmap stitchedImage = null;


    private List<Mat> listImage = new ArrayList<>();
    private List<Mat> listImageUp = new ArrayList<>();
    private List<Mat> listImageDn = new ArrayList<>();
    private List<Mat> listImage_test = new ArrayList<>();
    String fileName_g = "Test";

    Mat imageMat;
    Mat mat, mat1, mat2, mat3, mat4, mat5, mat6, resultUp, resultDn;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java4");
    }

    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    imageMat = new Mat();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

//        showDialog();
        OpenCVLoader.initDebug();
        if (!OpenCVLoader.initDebug()) {
            Log.d("MEME", ": Good is loaded............");
        }

        isPreview = false;
        mSurfaceView1_c = findViewById(R.id.surfaceView1_c);
        mSurfaceView1_x = findViewById(R.id.surfaceView1_x);
        mSurfaceView1_c.getHolder().addCallback(mSurfaceCallback);
        mSurfaceView2_c = findViewById(R.id.surfaceView2_c);
        mSurfaceView3_c = findViewById(R.id.surfaceView3_c);
        mSurfaceView4_c = findViewById(R.id.surfaceView4_c);
        stitchedImage = null;

        mSurfaceViewOnTop = findViewById(R.id.surfaceViewOnTop2_c);

        mSurfaceViewOnTop.getHolder().setFormat(PixelFormat.TRANSPARENT);

        saveBtn = (Button) findViewById(R.id.save3);
        saveBtn.setOnClickListener(saveOnClickListener);
        saveBtn.setVisibility(View.INVISIBLE);

        button_take4_1_c = findViewById(R.id.button_take4_1_c);
        button_take4_1_c.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Capturing image
                        if (mCam != null && safeToTakePicture) {
                            // set the flag to false so we don't take two picture at a same time
                            safeToTakePicture = false;
                            mCam.takePicture(null, null, jpegCallback);
                        }
                    }
                }
        );

        button_take4_2_c = findViewById(R.id.button_take4_2_c);
        button_take4_2_c.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Capturing image
                        if (mCam != null && safeToTakePicture) {

                            // set the flag to false so we don't take two picture at a same time
                            safeToTakePicture = false;
                            mCam.takePicture(null, null, jpegCallback);
                        }
                   }
                }
        );

        button_take4_3_c = findViewById(R.id.button_take4_3_c);
        button_take4_3_c.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Capturing image
                        if (mCam != null && safeToTakePicture) {

                            // set the flag to false so we don't take two picture at a same time
                            safeToTakePicture = false;
                            mCam.takePicture(null, null, jpegCallback);
                        }
                    }
                }
        );

        button_take4_4_c = findViewById(R.id.button_take4_4_c);
        button_take4_4_c.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Capturing image
                        button_take4_4_c.setVisibility(View.INVISIBLE);
                        if (mCam != null && safeToTakePicture) {

                            // set the flag to false so we don't take two picture at a same time
                            safeToTakePicture = false;
                            mCam.takePicture(null, null, jpegCallback);
                        }

                    }
                }
        );

        btnTest = findViewById(R.id.buttonTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeText(getApplicationContext(), "I am testing", LENGTH_SHORT).show();
                Context context = getApplicationContext();

                Bitmap btm_newspaper1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pano2_3up1);
                Bitmap btm_newspaper2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pano2_3up2);
                Bitmap btm_newspaper3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pano2_3dn1);
                Bitmap btm_newspaper4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.pano2_3dn2);
                Bitmap btm_newspaper5 = BitmapFactory.decodeResource(context.getResources(), R.drawable.boat5);
                Bitmap btm_newspaper6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.boat6);

//                btm_newspaper1 = RotateBitmap(btm_newspaper1, 90);
//                btm_newspaper2 = RotateBitmap(btm_newspaper2, 90);
//                btm_newspaper3 = RotateBitmap(btm_newspaper3, 90);
//                btm_newspaper4 = RotateBitmap(btm_newspaper4, 90);

                mat1 = new Mat();
                mat2 = new Mat();
                mat3 = new Mat();
                mat4 = new Mat();
                mat5 = new Mat();
                mat6 = new Mat();

                Utils.bitmapToMat(btm_newspaper1, mat1);
                listImageUp.add(mat1);
                Utils.bitmapToMat(btm_newspaper2, mat2);
                listImageUp.add(mat2);
                Utils.bitmapToMat(btm_newspaper3, mat3);
                listImageDn.add(mat3);
                Utils.bitmapToMat(btm_newspaper4, mat4);
                listImageDn.add(mat4);
//                Utils.bitmapToMat(btm_newspaper3, mat5);
//                listImage.add(mat5);
//                Utils.bitmapToMat(btm_newspaper4, mat6);
//                listImage.add(mat6);

                saveBtn.setVisibility(View.VISIBLE);
            }
        });
        btnTest.performClick();
        saveUpAndDown();
//        saveBtn.performClick();
        swithOnCamera();
    }
    // end onCreate()

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void swithOnCamera() {

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        mCam = Camera.open(0); // 0 for back camera
    }

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case 200:

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                break;

        }

    }

    public void stopCameraPreview() {
        if (mSurfaceView1_c != null) {
            try {
                isPreview = false;
                mSurfaceView1_c.getHolder().removeCallback(mSurfaceCallback);
                mCam.cancelAutoFocus();
                mCam.setOneShotPreviewCallback(null);
                mCam.stopPreview();
            } catch (Exception e) {
                Log.e("TAG_1", e.toString(), e);
            }
        }
    }

    View.OnClickListener captureOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCam != null && safeToTakePicture) {
                // set the flag to false so we don't take two picture at a same time
                safeToTakePicture = false;
                mCam.takePicture(null, null, jpegCallback);
            }
        }
    };

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {

            ViewGroup par_old = (ViewGroup) mSurfaceView1_c.getParent();
            switch (par_old.getId()) {
                case R.id.linearLayout2:
                    button_take4_1_c.setVisibility(View.INVISIBLE);
                    mSurfaceViewOnTop = findViewById(R.id.surfaceViewOnTop2_c);
                    break;
                case R.id.linearLayout3:
                    button_take4_2_c.setVisibility(View.INVISIBLE);
                    mSurfaceViewOnTop = findViewById(R.id.surfaceViewOnTop2_c2);

                    break;
                case R.id.linearLayout4:
                    button_take4_3_c.setVisibility(View.INVISIBLE);
                    mSurfaceViewOnTop = findViewById(R.id.surfaceViewOnTop2_c3);

                    break;
                case R.id.linearLayout5:
                    saveBtn.setVisibility(View.VISIBLE);
                    button_take4_4_c.setVisibility(View.INVISIBLE);
                    mSurfaceViewOnTop = findViewById(R.id.surfaceViewOnTop2_c4);

                    break;
            }

            // decode the byte array to a bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            // Rotate the picture to fit portrait mode
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);


            mat = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            listImage.add(mat);

            Log.d("Vision", "Height " + mat.rows() + " Width: " + mat.cols());

            Canvas canvas = null;
            try {
                canvas = mSurfaceViewOnTop.getHolder().lockCanvas(null);
                synchronized (mSurfaceViewOnTop.getHolder()) {
                    // Clear canvas
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                    // Scale the image to fit the SurfaceView
                    float scale = 1.0f * mSurfaceView1_c.getHeight() / bitmap.getHeight();
//                    Bitmap scaleImage = Bitmap.createScaledBitmap(bitmap, (int)(scale * bitmap.getWidth()), mSurfaceView1_c.getHeight() , false);
                    Bitmap scaleImage = Bitmap.createScaledBitmap(bitmap, (int) (mSurfaceView1_c.getWidth()), mSurfaceView1_c.getHeight(), false);
                    Paint paint = new Paint();
                    // Set the opacity of the image
//                    paint.setAlpha(200);
                    // Draw the image with an offset so we only see one third of image.
//                    canvas.drawBitmap(scaleImage, -scaleImage.getWidth() * 2 / 3, 0, paint);
                    canvas.drawBitmap(scaleImage, 0, 0, paint);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    mSurfaceViewOnTop.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            // Start preview the camera again and set the take picture flag to true
            mCam.startPreview();
            safeToTakePicture = true;


            int i1_old = par_old.indexOfChild(mSurfaceView1_c);
            ViewGroup par_new;
            par_old.removeViewAt(i1_old);

            switch (par_old.getId()) {
                case R.id.linearLayout2:
                    par_new = (ViewGroup) mSurfaceView2_c.getParent();
                    par_new.addView(mSurfaceView1_c, i1_old);
                    break;
                case R.id.linearLayout3:
                    par_new = (ViewGroup) mSurfaceView3_c.getParent();
                    par_new.addView(mSurfaceView1_c, i1_old);
                    break;
                case R.id.linearLayout4:
                    par_new = (ViewGroup) mSurfaceView4_c.getParent();
                    par_new.addView(mSurfaceView1_c, i1_old);
                    break;
                case R.id.linearLayout5:
                    break;

            }
        }
    };


    public void saveUpAndDown(){
        Thread thread = new Thread(imageProcessingRunnableUpAndDown);
        thread.start();
    }

    private final Runnable imageProcessingRunnableUpAndDown = new Runnable() {
        @Override
        public void run() {
            showProcessingDialog();

            try {
                long[] tempobjadrUp = new long[2];
                tempobjadrUp[0] = listImageUp.get(0).getNativeObjAddr();
                tempobjadrUp[1] = listImageUp.get(1).getNativeObjAddr();

                long[] tempobjadrDn = new long[2];
                tempobjadrDn[0] = listImageDn.get(0).getNativeObjAddr();
                tempobjadrDn[1] = listImageDn.get(1).getNativeObjAddr();

                // Create a Mat to store the final panorama image
                Mat resultUp = new Mat();
                Mat resultDn = new Mat();
                Mat resultC = new Mat();

                // Call the Open CV C++ Code to perform stitching process
                NativePanorama.processPanorama(tempobjadrUp, resultUp.getNativeObjAddr());
                NativePanorama.processPanorama(tempobjadrDn, resultDn.getNativeObjAddr());

                long[] tempobjadrC = new long[2];
                tempobjadrC[0] = resultUp.getNativeObjAddr();
                tempobjadrC[1] = resultDn.getNativeObjAddr();
                NativePanorama.processPanoramaC(tempobjadrC, resultC.getNativeObjAddr());

                File sdcard = Environment.getExternalStorageDirectory();
                final String fileNameUp = sdcard.getAbsolutePath() + "/opencv_z1_" + System.currentTimeMillis() + ".png";
                final String fileNameDn = sdcard.getAbsolutePath() + "/opencv_z2_" + System.currentTimeMillis() + ".png";
                final String fileNameC = sdcard.getAbsolutePath() + "/opencv_z3_" + System.currentTimeMillis() + ".png";

                Imgcodecs.imwrite(fileNameUp, resultUp);
                Imgcodecs.imwrite(fileNameDn, resultDn);
                Imgcodecs.imwrite(fileNameC, resultC);
                fileName_g = fileNameDn;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeText(getApplicationContext(), "File saved at: " + fileNameUp, LENGTH_LONG).show();
                    }
                });

                listImageUp.clear();
                listImageDn.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

            closeProcessingDialog();
        }
    };


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
                int elems = listImage.size();
                long[] tempobjadr = new long[elems];
                for (int i = 0; i < elems; i++) {
                    tempobjadr[i] = listImage.get(i).getNativeObjAddr();
                }
                // Create a Mat to store the final panorama image
                Mat result = new Mat();

                // Call the Open CV C++ Code to perform stitching process
                NativePanorama.processPanorama(tempobjadr, result.getNativeObjAddr());

                Log.d("Vision", "Height " + result.rows() + " Width: " + result.cols());
                // Save the image to internal storage
                File sdcard = Environment.getExternalStorageDirectory();
                final String fileName = sdcard.getAbsolutePath() + "/opencv_" + System.currentTimeMillis() + ".png";
                fileName_g = fileName;
                final Bitmap tempBitmap = convertMatToBitMap(result);
                Imgcodecs.imwrite(fileName, result);

                // ..
                String folder_main1 = "Stitching_images";

                File f1 = new File(getApplicationContext().getExternalFilesDir(null), folder_main1);
                if (!f1.exists()) {
                    f1.mkdirs();
                }

                final String fileName1 = f1.getAbsolutePath() + "/stitched_" + System.currentTimeMillis() + ".png";
                fileName_g = fileName1;
                Imgcodecs.imwrite(fileName1, result);

                stitchedImage = convertMatToBitMap(result);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        makeText(getApplicationContext(), "File saved at: " + fileName, LENGTH_LONG).show();
                    }
                });

                listImage.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

            closeProcessingDialog();
        }
    };

    private static Bitmap convertMatToBitMap(Mat input) {
        Bitmap bmp = null;
        Mat rgb = new Mat();
        Imgproc.cvtColor(input, rgb, Imgproc.COLOR_BGR2RGB);

        try {
            bmp = Bitmap.createBitmap(rgb.cols(), rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(rgb, bmp);
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return bmp;
    }


    private void showProcessingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCam.stopPreview();
                ringProgressDialog = ProgressDialog.show(CameraPreviewActivity.this, "", "Stitching in Progress", true);
                ringProgressDialog.setCancelable(false);
            }
        });
    }

    private void closeProcessingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mCam.startPreview();
                ringProgressDialog.dismiss();
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);

                // Opening Result activity
                Intent intent = new Intent(getApplicationContext(), ResultViewActivity.class);
                String str = "file Name";
                intent.putExtra("file_name", fileName_g);

                startActivity(intent);


//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(fileName_g), "image/*");
//                startActivity(intent);
            }
        });
    }


    SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
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
            Camera.Size myBestSize = getBestPreviewSize(myParameters);

            if (myBestSize != null) {
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


    private Camera.Size getBestPreviewSize(Camera.Parameters parameters) {
        Camera.Size bestSize = null;
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        bestSize = sizeList.get(0);
        for (int i = 1; i < sizeList.size(); i++) {
            if ((sizeList.get(i).width * sizeList.get(i).height) >
                    (bestSize.width * bestSize.height)) {
                bestSize = sizeList.get(i);
            }
        }
        return bestSize;
    }

    private void saveBitmap(Bitmap bmp) {
        String filename = "/sdcard/testPano.bmp";
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

//        checkRunTimePermission();
        mCam = Camera.open(0); // 0 for back camera
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isPreview) {
            mCam.stopPreview();
        }
        mCam.release();
        mCam = null;
        isPreview = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_panaroma, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        noinspection SimplifiableIfStatement
        if (id == R.id.action_open_2x2) {
            startActivity(new Intent(this, BasicMainActivity.class));
            return true;
        }
        if (id == R.id.action_open_result) {
            startActivity(new Intent(this, ResultViewActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showDialog() {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(CameraPreviewActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Please capture four (4) photos of the ART from starting from four (4) corners of the ART so that a good quality image can be produced.");

        // Set Alert Title
//        builder.setTitle("Alert !");
        builder.setTitle("Instructions !");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // When the user click yes button
                                // then app will close
//                                finish();
                            }
                        });


        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }



}