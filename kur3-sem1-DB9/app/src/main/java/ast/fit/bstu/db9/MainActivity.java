package ast.fit.bstu.db9;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText id,t,f;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDBHelper mDbHelper = new MyDBHelper(this);
        db = getBaseContext().openOrCreateDatabase("9-lab.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS simpletable (ID integer, F REAL, T TEXT)");

        id=findViewById(R.id.editTextTextPersonName3);
        f=findViewById(R.id.editTextTextPersonName4);
        t=findViewById(R.id.editTextTextPersonName5);
        Button insert= findViewById(R.id.button);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContentValues v=new ContentValues();
                if(!seek()) {
                    v.put("ID", String.valueOf(id.getText()));
                    v.put("F", String.valueOf(f.getText()));
                    v.put("T", String.valueOf(t.getText()));
                    long rowid = db.insert("simpletable", null, v);
                    Toast.makeText(MainActivity.this, "Записано! " + String.format(String.valueOf(rowid)), Toast.LENGTH_LONG).show();
                    clear();
                }
                else
                    Toast.makeText(MainActivity.this, "Уже есть с таким ключом! ", Toast.LENGTH_LONG).show();

            }
        });
        Button select= findViewById(R.id.button2);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c= db.query("simpletable", new String[]{"ID","F","T"},
                        "ID = ?", new String[]{String.valueOf(id.getText())},null, null, null);

                if(c.moveToFirst())
                {
                    do{
                        //id.setText(c.getInt(0));
                        f.setText(String.valueOf(c.getFloat(1)));
                        t.setText(c.getString(2));

                    }while (c.moveToNext());
                }
                else
                    clear();
                if(f.getText().length()==0)
                    Toast.makeText(MainActivity.this, "Нет такого! ", Toast.LENGTH_LONG).show();
            }
        });
        Button selectraw= findViewById(R.id.button3);
        selectraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor c= db.rawQuery("select ID,F,T from simpletable where ID= ?", new String[]{String.valueOf(id.getText())});

                if(c.moveToFirst())
                {
                    do{
                        //id.setText(c.getInt(0));
                        f.setText(String.valueOf(c.getFloat(1)));
                        t.setText(c.getString(2));

                    }while (c.moveToNext());
                }
                else
                    clear();
                if(f.getText().length()==0)
                    Toast.makeText(MainActivity.this, "Нет такого! ", Toast.LENGTH_LONG).show();

            }
        });
        Button upd= findViewById(R.id.button4);
        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues v=new ContentValues();
                if(seek()) {
                    v.put("ID", String.valueOf(id.getText()));
                    v.put("F", String.valueOf(f.getText()));
                    v.put("T", String.valueOf(t.getText()));
                    int c= db.update("simpletable",v,"ID=?",new String[]{String.valueOf(id.getText())});
                    Toast.makeText(MainActivity.this, "Изменено! " , Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Нет такого поля чтобы поменять! ", Toast.LENGTH_LONG).show();
                }clear();
            }
        });
        Button delete= findViewById(R.id.button5);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues v=new ContentValues();
                if(seek()) {
                    int c= db.delete("simpletable","ID= ?",new String[]{String.valueOf(id.getText())});
                    Toast.makeText(MainActivity.this, "Удалено! " , Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Нет такого поля чтобы удалить! ", Toast.LENGTH_LONG).show();
                } clear();
            }
        });
    }

    private void clear()
    {
        id.setText("");
        f.setText("");
        t.setText("");
    }
    private boolean seek()
    {
        Cursor c= db.query("simpletable", new String[]{"ID","F","T"},
                "ID = ?", new String[]{String.valueOf(id.getText())},null, null, null);

        if(c.moveToFirst())
        { return true; }
        else
        {return false;}
    }
    public class MyDBHelper extends SQLiteOpenHelper{

        public MyDBHelper( Context context) {
            super(context, "simpletable", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Toast.makeText(MainActivity.this, "БД была создана>", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            Toast.makeText(MainActivity.this, "Бд была изменена", Toast.LENGTH_SHORT).show();

        }
    }
}