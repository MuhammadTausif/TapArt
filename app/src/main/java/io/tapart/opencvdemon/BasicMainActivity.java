package io.tapart.opencvdemon;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import org.opencv.core.Mat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.tapart.opencvdemon.R;

import static android.widget.Toast.LENGTH_LONG;

public class BasicMainActivity extends AppCompatActivity {

    private Button button_take_4_1, button_take_4_2, button_take_4_3, button_take_4_4;
    private ImageView imageView_take_4_1, imageView_take_4_2, imageView_take_4_3, imageView_take_4_4;
    int clickedButton;

    private Button captureBtn, saveBtn; // used to interact with capture and save Button in UI
    private SurfaceView mSurfaceView, mSurfaceViewOnTop; // used to display the camera frame in UI
    private Camera mCam;
    private boolean isPreview; // Is the camera frame displaying?
    private boolean safeToTakePicture = true; // Is it safe to capture a picture?

    private List<Mat> listImage = new ArrayList<>();

    Mat imageMat;
    Mat mat;

    static {
        System.loadLibrary("native-lib");
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
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button_take_4_1 = findViewById(R.id.button_take4_1);
        button_take_4_2 = findViewById(R.id.button_take4_2);
        button_take_4_3 = findViewById(R.id.button_take4_3);
        button_take_4_4 = findViewById(R.id.button_take4_4);

        imageView_take_4_1 = findViewById(R.id.imageView_take4_1);
        imageView_take_4_2 = findViewById(R.id.imageView_take4_2);
        imageView_take_4_3 = findViewById(R.id.imageView_take4_3);
        imageView_take_4_4 = findViewById(R.id.imageView_take4_4);

        button_take_4_1.setOnClickListener(view1 -> {
            clickedButton = button_take_4_1.getId();
            captureImage();
        });
        button_take_4_2.setOnClickListener(view1 -> {
            clickedButton = button_take_4_2.getId();
            captureImage();
        });
        button_take_4_3.setOnClickListener(view1 -> {
            clickedButton = button_take_4_3.getId();
            captureImage();
        });
        button_take_4_4.setOnClickListener(view1 -> {
            clickedButton = button_take_4_4.getId();
            captureImage();
        });

        OpenCVLoader.initDebug();

        isPreview = false;
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        mSurfaceView.getHolder().addCallback(mSurfaceCallback);

        mSurfaceViewOnTop = (SurfaceView) findViewById(R.id.surfaceViewOnTop1);
        mSurfaceViewOnTop.setZOrderOnTop(true);    // necessary
        mSurfaceViewOnTop.getHolder().setFormat(PixelFormat.TRANSPARENT);

//        captureBtn = findViewById(R.id.capture1);
//        captureBtn.setOnClickListener(captureOnClickListener);
//
//        saveBtn =  findViewById(R.id.save1);
//        saveBtn.setOnClickListener(saveOnClickListener);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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

                Toast.makeText(getApplicationContext(),
                        "Best Size:\n" +
                                String.valueOf(myBestSize.width) + " : " + String.valueOf(myBestSize.height),
                        Toast.LENGTH_LONG).show();
            }
        }

        ProgressDialog ringProgressDialog;

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

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

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
        mCam = Camera.open(0); // 0 for back camera
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(isPreview){
            mCam.stopPreview();
        }
        mCam.release();
        mCam = null;
        isPreview = false;
    }

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


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void captureImage() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap) data.getExtras().get("data");

        if (button_take_4_1.getId() == clickedButton) {
            imageView_take_4_1.setImageBitmap(photo);
        } else if (button_take_4_2.getId() == clickedButton) {
            imageView_take_4_2.setImageBitmap(photo);
        } else if (button_take_4_3.getId() == clickedButton) {
            imageView_take_4_3.setImageBitmap(photo);
        } else if (button_take_4_4.getId() == clickedButton) {
            imageView_take_4_4.setImageBitmap(photo);
        }
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
//        {
//        Bitmap photo = (Bitmap) data.getExtras().get("data");
//        imageView_take_4_1.setImageBitmap(photo);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_basic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_open_panaroma) {
            startActivity(new Intent(this, PanaromaActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        inflater.inflate(R.menu.menu, menu);
//
//        MenuItem item = menu.findItem(R.id.login_id);
//        item.setVisible(false);//
//        MenuItem item = menu.findItem(R.id.logOff_id);
//        item.setVisible(true);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
}