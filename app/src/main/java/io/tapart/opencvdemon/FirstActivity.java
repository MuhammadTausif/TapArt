package io.tapart.opencvdemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class FirstActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private TextView textViewInstruction;
    private Button buttonTakePhoto, buttonTake4, buttonTake6, buttonTake9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        this.imageView = findViewById(R.id.image_view);
        // getting the buttons
        this.buttonTakePhoto = findViewById(R.id.button_first);
        this.buttonTake4 = findViewById(R.id.button_take4);
        this.buttonTake6 = findViewById(R.id.button_take6);
        this.buttonTake9 = findViewById(R.id.button_take9);

        buttonTake9.setOnClickListener(view1 -> {

        });
        buttonTake6.setOnClickListener(view1 -> {

        });
        buttonTake4.setOnClickListener(view1 -> {
           startActivity(new Intent(getApplicationContext(), CameraPreviewActivity.class));
        });


        // getting textView
        this.textViewInstruction = findViewById(R.id.textview_first);
        findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();

                // show and hide button
                buttonTakePhoto.setVisibility(View.GONE);
                buttonTake4.setVisibility(View.VISIBLE);
                buttonTake6.setVisibility(View.VISIBLE);
                buttonTake9.setVisibility(View.VISIBLE);

                buttonTake6.setEnabled(false);
                buttonTake9.setEnabled(false);

                // change text of instructions textView
                textViewInstruction.setText(R.string.instractions_to_select_images_no);
            }
        });
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
//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
//        {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(photo);
        Toast.makeText(getApplicationContext(), "Camera Activity", LENGTH_LONG).show();
//        }
    }
}