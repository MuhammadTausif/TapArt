package io.tapart.opencvdemon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        ImageView image = findViewById(R.id.image2);
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.icon_main);
//        image.setImageBitmap(bmp);



        // create the get Intent object
        Intent intent = getIntent();
//        String bitmapName = "bitmap";
//        Bitmap bitmap2 = (Bitmap) intent.getParcelableExtra(bitmapName);


//        Bundle extras = getIntent().getExtras();
//        byte[] byteArray = extras.getByteArray("picture");
//
//        Bitmap bitmap1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        ImageView image = findViewById(R.id.image2);

//        image.setImageBitmap(bitmap2);

        // receive the value by getStringExtra() method
        // and key must be same which is send by first activity
        String str = intent.getStringExtra("file_name");
//        Bundle stitchedImage = intent.getExtras();
        TextView textView = findViewById(R.id.text_test);
        textView.setText("Stitched Image Saved at: "+str);

        final Bitmap b = BitmapFactory.decodeFile(str);
//        image.setImageBitmap(b);

        PhotoView photoView =  findViewById(R.id.image2);
        photoView.setImageBitmap(b); // .setImageResource(R.drawable.image);



//        PhotoView photoView = (PhotoView) findViewById(R.id.image1);
//        photoView.setImageResource(R.drawable.pano1_1);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
}