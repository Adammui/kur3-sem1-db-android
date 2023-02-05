package ast.fit.bstu.bd6_2;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText date, name, surname,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String fname = "6Lab.txt";
        File fPublic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fname);
        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        phone = findViewById(R.id.phone);
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{READ_EXTERNAL_STORAGE}, 1);
            finish();
        }
        else {

        }
        Button but1 = (Button) findViewById(R.id.find);
        but1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (existfile(fPublic) == true) {

                    Toast.makeText(MainActivity.this, "вижу файл", Toast.LENGTH_LONG).show();
                    FileReader fr = null;
                    try {
                        fr = new FileReader(fPublic);
                        String temp [];
                        BufferedReader reader = new BufferedReader(fr);
                        String line = reader.readLine();
                        Toast.makeText(MainActivity.this, line.toString(), Toast.LENGTH_LONG).show();
                        while(line!= null)
                        {
                            temp = line.split("[;]");
                            if(temp[3].equals(date.getText().toString()))
                            {
                                name.setText(temp[1]);
                                surname.setText(temp[2]);
                                phone.setText(temp[0]);
                                Toast.makeText(MainActivity.this, temp[1], Toast.LENGTH_LONG).show();
                            }
                            line = reader.readLine();
                        }
                    } catch (IOException e) {
                        Log.d("cerf",e.getMessage());
                    }


                } else {
                    Toast.makeText(MainActivity.this, "Файл отсутствует", Toast.LENGTH_LONG).show();
                }
            }
        });
        if ( !existfile(fPublic)) {
            Toast.makeText(MainActivity.this, "Файл отсутствует", Toast.LENGTH_LONG).show();

        }

    }
    private boolean existfile(File f)
    {
        boolean rc=false;
        if(f.exists()) {rc=true;}
        else Log.d("LOG","Файл не найден");
        return rc;
    }
}