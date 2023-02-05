package ast.fit.bstu.bd4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String fname="Base_Lab.txt";

        if(!existfile(fname)) {
            AlertDialog.Builder b= new AlertDialog.Builder(this);
            b.setTitle("Файл не найден. Создать?"+ fname).setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    File f= new File(MainActivity.super.getFilesDir(),fname);
                    try{
                        f.createNewFile();
                        Log.d("LOG","Файл создан");
                    }
                    catch(IOException e) {
                        Log.d("LOG","Ошибка при создании");
                    }
                }
            }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.d("LOG","Отмена создания файла");
                    System.exit(0);
                }
            } );
            AlertDialog ad= b.create();
            ad.show();
        }
    }
    private boolean existfile(String fname)
    {
        boolean rc=false;
        File f= new File(super.getFilesDir(), fname);
        if(f.exists()) {rc=true; Log.d("Log 02","Файл существует");}
        else Log.d("Log 02","Файл не найден");
        return rc;
    }
    public void entr(View view)
    {
        String fname="Base_Lab.txt";
        EditText surname= findViewById(R.id.surname), name=findViewById(R.id.name);
        FileOutputStream fos=null;
        File f= new File(MainActivity.super.getFilesDir(),fname);
        try{
            String s=surname.getText().toString()+";"+ name.getText().toString()+";"+"\r\n";
            fos=openFileOutput(fname, MODE_APPEND);
            if(s.trim().length()<3) {
                Toast.makeText(getApplicationContext(), "Заполните поля!", Toast.LENGTH_SHORT).show();
                Log.d("log 02","Данные записаны");}
            else{
                fos.write(s.getBytes(StandardCharsets.UTF_8));
                Toast.makeText(getApplicationContext(), "Сохранено!", Toast.LENGTH_SHORT).show();
                Log.d("log 02","Данные записаны");}
        }
        catch(IOException e){
            Log.d("log 02","Файл не открыт");
            Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        }
    }
    public void show(View view)
    {
        TextView r= findViewById(R.id.result);
        String fname="Base_Lab.txt";
        FileInputStream fin=null;
        File f= new File(MainActivity.super.getFilesDir(),fname);
        try {
            fin=openFileInput(fname);
            byte[] bytes=new byte[fin.available()];
            fin.read(bytes);
            r.setText( new String (bytes));
            Log.d("log 02","Данные выведены");}
        catch (IOException e) {
            Log.d("log 02","Ошибка при выводе");
            Toast.makeText(getApplicationContext(), "Файл пуст или не создан", Toast.LENGTH_SHORT).show();
        }

    }
}