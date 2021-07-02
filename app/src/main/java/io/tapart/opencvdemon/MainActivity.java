package io.tapart.opencvdemon;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    String[] paths;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
//        System.loadLibrary("Stitcher");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
//        TextView tv = findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
    native static String[] stitchImages_1(String[] paths);

    native static String[] stitchImages_p(String[] paths);

    native static String[] stitchIt(String[] paths);


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void btnFlip_click(View view) {
        // To open up a gallery browser
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);

//        stitchImages_1(paths);
    }


    // To handle when an image is selected from the browser, add the following to your Activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {

//                // currImageURI is the global variable I'm using to hold the content:// URI of the image
//                Uri currImageURI = data.getData();
//                ClipData clipData = data.getClipData();
//                int path_len = clipData.getItemCount();
//                paths = new String[path_len];
//
//                for (int i = 0; i < path_len; i++) {
//                    Uri uri = clipData.getItemAt(i).getUri();
//                    paths[i] = getFilePathFromURI(uri);
//
//                }
//                String[] rp = stitchIt(paths);
//                int a = 0;
//
//                Toast.makeText(getApplicationContext(), rp[0], Toast.LENGTH_LONG).show();
            }
        }
    }


    // And to convert the image URI to the direct file system path of the image file
//    public String getRealPathFromURI(Uri contentUri) {
//
//        // can post image
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(contentUri,
//                proj, // Which columns to return
//                null,       // WHERE clause; which rows to return (all rows)
//                null,       // WHERE clause selection arguments (none)
//                null); // Order-by clause (ascending by name)
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//
//        return cursor.getString(column_index);
//    }

//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }

//    public String getRealPathFromURI_1(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String s = cursor.getString(column_index);
//        return cursor.getString(column_index);
//    }

//    private static String getFilePathFromURI( Uri contentUri) {
//        //copy file and send new file path
//        String fileName = getFileName(contentUri);
//        if (!TextUtils.isEmpty(fileName)) {
//            String TEMP_DIR_PATH = Environment.getExternalStorageDirectory().getPath();
//            File copyFile = new File(TEMP_DIR_PATH + File.separator + fileName);
//            Log.d("DREG", "FilePath copyFile: " + copyFile);
//
//            String path = copyFile.getAbsolutePath();
//            return copyFile.getAbsolutePath();
//        }
//        return null;
//    }
//    public static String getFileName(Uri uri) {
//        if (uri == null) return null;
//        String fileName = null;
//        String path = uri.getPath();
//        int cut = path.lastIndexOf('/');
//        if (cut != -1) {
//            fileName = path.substring(cut + 1);
//        }
//        return fileName;
//    }

}