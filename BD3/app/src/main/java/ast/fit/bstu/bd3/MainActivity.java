package ast.fit.bstu.bd3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    TextView absolute, name, path, rw, external;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        absolute = findViewById(R.id.absolute);
        name = findViewById(R.id.name);
        path = findViewById(R.id.path);
        rw = findViewById(R.id.readWrite);
        external = findViewById(R.id.external);
    }
    public class FILE{
        File file;
        FILE(File f){
            file=f;
        }
        void getAbsolute(){
            absolute.setText("Absolute: "+ file.getAbsolutePath());
        }
        void getName(){
            name.setText("Name: "+ file.getName());
        }
        void getPath(){
            path.setText("Path: "+ file.getPath());
        }
        void getRW(){
            String temp;
            if(file.canRead()) temp="yes";
            else temp="no";
            if(file.canWrite()) temp+="/yes";
            else temp+="/no";
            rw.setText("Read/Write: "+ temp);
        }
        void getEx(){
            external.setText("External state: "+ Environment.getExternalStorageState());
        }
    }

    public void GETFILESDIR(View view) {

         File filesDir = super.getFilesDir();
         FILE f=new FILE(filesDir);
         f.getAbsolute();
         f.getName();f.getPath();
         f.getRW();f.getEx();
         /*
         absolute.setText(absolute.getText()+ filesDir.getAbsolutePath());
         name.setText(name.getText()+ filesDir.getName());
         path.setText(path.getText()+ filesDir.getPath());
         String temp;
         if(filesDir.canRead()) temp="yes";
         else temp="no";
         if(filesDir.canWrite()) temp+="/yes";
         else temp+="/no";
         rw.setText(rw.getText()+ temp);
         external.setText(external.getText()+ Environment.getExternalStorageState());
         */
    }
    public void GETCACHEDIR(View view) {
        File cacheDir= super.getCacheDir();
        FILE f=new FILE(cacheDir);
        f.getAbsolute();
        f.getName();f.getPath();
        f.getRW();f.getEx();
    }
    public void GETEXTERNALFILESDIR(View view){
        File exFilesDir= super.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        FILE f=new FILE(exFilesDir);
        f.getAbsolute();
        f.getName();f.getPath();
        f.getRW();f.getEx();
    }
    public void GETEXTERNALCACHEDIR(View view){
        File exCacheDir= super.getExternalCacheDir();
        FILE f=new FILE(exCacheDir);
        f.getAbsolute();
        f.getName();f.getPath();
        f.getRW();f.getEx();
    }
    public void GETEXTERNALSTORAGEDIRECTORY(View view){
        File exStorageDir= Environment.getExternalStorageDirectory();
        FILE f=new FILE(exStorageDir);
        f.getAbsolute();
        f.getName();f.getPath();
        f.getRW();f.getEx();
    }
    public void GETEXTERNALSTORAGEPUBLICDIRECTORY(View view){
        File exStoragePublic= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        FILE f=new FILE(exStoragePublic);
        f.getAbsolute();
        f.getName();f.getPath();
        f.getRW();f.getEx();
    }

}