package ast.fit.bstu.bd5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText key1, key2, var1;
    private TextView result, show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String fname="5Lab.txt";
        show= findViewById(R.id.showing);
        key1 = findViewById(R.id.key1);
        key2 = findViewById(R.id.key2);
        var1 = findViewById(R.id.var1);
        result= findViewById(R.id.result);
        /*try {
            File fe= new File(MainActivity.super.getFilesDir(),fname);
            RandomAccessFile raf = new RandomAccessFile(fe, "rw");
            show.setText("");
            while(raf.getFilePointer()<raf.length()) {
                show.setText(show.getText() + raf.readLine()+"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();}
*/
        refresh(fname);
        Button but0 = (Button) findViewById(R.id.wipe);
        but0.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                File fel= new File(MainActivity.super.getFilesDir(),fname);
                try {
                    fel.delete();
                    fel.createNewFile();
                    refresh(fname);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }});

        Button but = (Button) findViewById(R.id.write);
        but.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(existfile(fname)==true&& key1.getText().length()==5 && var1.getText().length()==10)
                {
                    File f= new File(MainActivity.super.getFilesDir(),fname);
                    try {
                        RandomAccessFile raf = new RandomAccessFile(f, "rw");
                        raf.seek(0);
                        long pointer=0, pointerend=0;
                        for(int i=0; raf.getFilePointer()<raf.length();i++)
                        {
                            pointer=raf.getFilePointer();
                            String res= raf.readLine();
                            pointerend=raf.getFilePointer();
                            String[] temps = res.split("([:]|[;])");
                            if(hashcode(temps[0]).equals(hashcode(key1.getText().toString())))
                            {
                               if(temps[0].equals(key1.getText().toString()))
                                {
                                    raf.seek(pointer+6);
                                    rewrite(raf);
                                    //если что-то случится вставить сюда саму функцию внутренность
                                    refresh(fname);
                                    return;
                                }
                                else if((pointerend-pointer-17)>2)
                                {
                                    for(int a = 2; a< (pointerend-pointer-1)*2/17; a+=2)
                                    {
                                        if(temps[a].equals(key1.getText().toString()))
                                        {
                                            raf.seek(pointer+6+(a/2)*17);
                                           rewrite(raf);
                                           refresh(fname);
                                            return;
                                        }
                                    }
                                    writo(raf,pointerend);
                                    refresh(fname);
                                    return;
                                }
                               else {
                                    writo(raf,pointerend);
                                   refresh(fname);
                                    return;
                                }
                            }
                            raf.seek(pointerend+1);
                        }
                        raf.seek(raf.length());
                        raf.writeBytes(key1.getText().toString()+":"+var1.getText().toString()+";");
                        raf.writeBytes("\n");
                        raf.close();
                        Toast.makeText(MainActivity.this, "Записано! ", Toast.LENGTH_LONG).show();
                        refresh(fname);
                        return;
                    }
                    catch (IOException e) {
                        Log.d("LOG",e.getMessage());
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Введите ключ длиной 5 символов и значение длиной 10", Toast.LENGTH_LONG).show();
                }
            }
        });
        Button but1 = (Button) findViewById(R.id.find);
        but1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(existfile(fname)==true&& +key2.getText().length()==5)
                {
                    File f= new File(MainActivity.super.getFilesDir(),fname);
                    try {
                        RandomAccessFile raf = new RandomAccessFile(f, "rw");
                        raf.seek(0);
                        long pointer=0, pointerend=0;
                        for(int i=0; raf.getFilePointer()<raf.length();i++)
                        {
                            Log.d("LOG",i+"ifu");
                            pointer=raf.getFilePointer();
                            String res= raf.readLine();
                            pointerend=raf.getFilePointer();
                            String[] temps = res.split("([:]|[;])");
                            if(hashcode(temps[0]).equals(hashcode(key2.getText().toString())))
                            {
                                if(temps[0].equals(key2.getText().toString()))
                                {
                                    Toast.makeText(MainActivity.this, "Совпадение ключа", Toast.LENGTH_LONG).show();
                                    result.setText(temps[1]);
                                    return;
                                }
                                else
                                {
                                    for(int a = 2; a< (pointerend-pointer-1)*2/17; a+=2)
                                    {
                                        if(temps[a].equals(key2.getText().toString()))
                                        {
                                            Toast.makeText(MainActivity.this, "Совпадение в строке", Toast.LENGTH_LONG).show();
                                            result.setText(temps[a+1]);
                                            return;
                                        }
                                    }
                                }
                            }
                            raf.seek(pointerend);
                        }
                        result.setText("Нет такого ключа");
                        return;
                    } catch (IOException e) {
                        Log.d("LOG",e.getMessage());
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Введите ключ длиной 5 символов", Toast.LENGTH_LONG).show();
                }
            }
        });
        if(!existfile(fname)) {
            AlertDialog.Builder b= new AlertDialog.Builder(this);
            b.setTitle("Файл не найден. Создать?"+ fname).setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try{
                        File f= new File(MainActivity.super.getFilesDir(),fname);

                        RandomAccessFile raf = new RandomAccessFile(f, "rw");
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
        if(f.exists()) {rc=true;}
        else Log.d("LOG","Файл не найден");
        return rc;
    }

    public String hashcode(String a) {
        String temp= a.substring(a.length() - 1);
        return temp;
    }
    private void refresh(String fname)
    {
        try {
            File fe= new File(MainActivity.super.getFilesDir(),fname);
        RandomAccessFile raf =  new RandomAccessFile(fe, "rw");
        show.setText("");
        while(raf.getFilePointer()<raf.length()) {
            show.setText(show.getText() + raf.readLine()+"\n"); }
        }
        catch (IOException e) {
        e.printStackTrace();
        }
    }

    private void rewrite(RandomAccessFile raf)
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
}
