package ast.fit.bstu.bd5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {


    private static final int STORAGE_PERMISSION_CODE = 101;
    private EditText date, name, surname,phone, key;
    private TextView result, show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String fname = "6Lab.txt";
        result = findViewById(R.id.result);
        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        phone = findViewById(R.id.phone);
        key = findViewById(R.id.key);
        File fPrivate = new File(MainActivity.super.getFilesDir(), fname);
        File fPublic = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fname);
        try {
            Toast.makeText(MainActivity.this, fPublic.getCanonicalPath(), Toast.LENGTH_LONG).show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        //if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
        //        PackageManager.PERMISSION_DENIED) {
        //    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        //    finish();
        //}
        //else {
            Button but0 = (Button) findViewById(R.id.wipe);
            but0.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                        fPrivate.delete();
                        fPublic.delete();
                        fPublic.createNewFile();
                        fPrivate.createNewFile();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            });

            Button but = (Button) findViewById(R.id.write);
            but.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (existfile(fPrivate) == true) {

                        String str= phone.getText().toString()+";"+name.getText().toString()+";"+
                                surname.getText().toString()+";"+date.getText().toString();
                        write(fPrivate, str);
                    } else {
                        Toast.makeText(MainActivity.this, "не получается записать", Toast.LENGTH_LONG).show();
                    }
                }
            });
            Button but2 = (Button) findViewById(R.id.writepublic);
            but2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (existfile(fPublic) == true) {
                        String str= phone.getText().toString()+";"+name.getText().toString()+";"+
                                surname.getText().toString()+";"+date.getText().toString();
                        write(fPublic, str);
                        Toast.makeText(MainActivity.this, "Записано"+ fPublic.getAbsolutePath(), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(MainActivity.this, "не получается прочитаь", Toast.LENGTH_LONG).show();
                    }
                }
            });
            Button but1 = (Button) findViewById(R.id.find);
            but1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (existfile(fPublic) == true) {

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
                                if(temp[1].equals(key.getText().toString()) || temp[2].equals(key.getText().toString()))
                                {
                                  result.setText(temp[0]);
                                    Toast.makeText(MainActivity.this, temp[1], Toast.LENGTH_LONG).show();
                                }
                                line = reader.readLine();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Toast.makeText(MainActivity.this, "Файл отсутствует", Toast.LENGTH_LONG).show();
                    }
                }
            });
            if ( !existfile(fPublic)) { //!existfile(fPrivate)||
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("Файл не найден. Создать?" + fname).setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            fPublic.createNewFile();
                            fPublic.setReadOnly();
                            fPublic.setWritable(true);
                            fPublic.setExecutable(true);

                            //fPrivate.createNewFile();
                            Log.d("LOG", "Файл создан");
                        } catch (IOException e) {
                            Log.d("LOG", "Ошибка при создании"+ e.getMessage());
                        }
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("LOG", "Отмена создания файла");
                        System.exit(0);
                    }
                });
                AlertDialog ad = b.create();
                ad.show();
            }
        }
    //}
    private boolean existfile(File f)
    {
        boolean rc=false;
        if(f.exists()) {rc=true;}
        else Log.d("LOG","Файл не найден");
        return rc;
    }

    private void write(File path, String str)
    {
        try {
            PrintWriter prWr = new PrintWriter(new BufferedWriter(new FileWriter(path, true)));
            prWr.println(str);
            prWr.close();
        }
        catch (IOException e)
        {
            Log.d("Log", e.getMessage());
        }
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*private void rewrite(RandomAccessFile raf)
    {
        try {
            // тут можно сделать вопрос типа хотите перезаписать знаечние
            raf.writeBytes(var1.getText().toString()+";");
            raf.close();
        }
        catch (IOException exception) {

            exception.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "Перезаписано значение для данного ключа", Toast.LENGTH_LONG).show();
    }
    private void writo(RandomAccessFile raf,long pointerend)
    {
        try {
            raf.seek(pointerend);

        byte[] bytes = new byte[(int) (raf.length()-pointerend)];
        raf.read(bytes);
        raf.seek(pointerend-1);
        raf.writeBytes(key1.getText().toString()+":"+var1.getText().toString()+";");
        raf.writeBytes("\n");
        raf.write(bytes);
        raf.close();
        }
        catch (IOException exception) {
        exception.printStackTrace();
    }
        Toast.makeText(MainActivity.this, "Записано в цепочку", Toast.LENGTH_LONG).show();
    }

     */
}
