package sg.edu.rp.c347.p09quiz;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MainActivity extends AppCompatActivity {

    EditText etC;
    Button btnSave,btnRead,btnShow;
    String coord, folderLocation;
    TextView tvC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etC = findViewById(R.id.editTextCord);
        btnSave = findViewById(R.id.button);
        btnRead = findViewById(R.id.buttonRead);
        btnShow = findViewById(R.id.buttonShow);
        tvC = findViewById(R.id.textViewC);

        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";



        if (!checkPermission()) {
            String msg = "Permission not granted to retrieve location info";
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {

            File folder = new File(folderLocation);
            if (folder.exists() == false) {
                boolean result = folder.mkdir();
                if (result == true) {
                    Log.d("File Read/Write", "Folder created");
                }
            }

        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coord = etC.getText().toString();

                try {
                    folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/quiz";

                    File targetFile = new File(folderLocation, "quiz.txt");

                    FileWriter writer = new FileWriter(targetFile, false);
                    //false will overwrite with the first line
                    //true adds to the line
                    writer.write(coord + "\n");
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Failed to write!",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }




            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File targetFile = new File(folderLocation, "quiz.txt");
                if (targetFile.exists() == true) {
                    String data = "";

                    try {
                        FileReader reader = new FileReader(targetFile);
                        BufferedReader br = new BufferedReader(reader);
                        String line = br.readLine();
                        while (line!= null) {
                            data += line + "\n";
                            line = br.readLine();
                        }
                        br.close();
                        reader.close();

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to read!",Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }

                    Log.d("Content",data);
                    tvC.setText(data);
                }
            }
        });


        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SecondActivity.class);
                String loc = tvC.getText().toString();

                String[] latlng = loc.split(" ",2);
                Log.d("LATLNG",latlng[1]);
                String lat = latlng[0];
                String lng = latlng[1];
                Log.d("TEST",lat);
                i.putExtra("lat",lat);
                i.putExtra("lng",lng);

                startActivity(i);
            }
        });
    }







    private boolean checkPermission(){
        int permissionCheck_WRITEEXT = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_READEXT = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck_WRITEEXT == PermissionChecker.PERMISSION_GRANTED
                || permissionCheck_READEXT == PermissionChecker.PERMISSION_GRANTED
        || permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
        || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


}
