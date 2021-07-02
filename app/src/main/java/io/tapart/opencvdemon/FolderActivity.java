package io.tapart.opencvdemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class FolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        String folder_main = "NewFolder";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
        }

        String destPath = getApplicationContext().getExternalFilesDir(null).getAbsolutePath();
        Toast.makeText(getApplicationContext(), destPath, Toast.LENGTH_LONG).show();

        TextView textView = findViewById(R.id.textViewFolderName);
        textView.setText(destPath);
        textView.setText(getApplicationContext().getExternalFilesDir(null).getAbsolutePath());

        String folder_main1 = "NewFolder12345";

        File f1 = new File(getApplicationContext().getExternalFilesDir(null), folder_main1);
        if (!f1.exists()) {
            f1.mkdirs();
        }

        String folder_main2 = "NewFolder1234567891011";

        File f2 = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath(), folder_main2);
        if (!f2.exists()) {
            f2.mkdirs();
        }

    }
}