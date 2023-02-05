package ast.fit.bstu.db10;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ArrayList<String> head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        load();
        Button add = findViewById(R.id.button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
        EditText id= findViewById(R.id.NAMEt), newo= findViewById(R.id.NAMEn);
        Button update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.updateGroups( db, Integer.parseInt(String.valueOf(id.getText())) , Integer.parseInt(String.valueOf(newo.getText())) );
                load();
            }
        });
        Button del = findViewById(R.id.delete);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.deleteGroups( db, Integer.parseInt( String.valueOf(id.getText())) );
                load();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        db.close();
        cursor.close();
    }
    void load()
    {
        ListView lv = findViewById(R.id.list), lvst= findViewById(R.id.stlist);
        head = new ArrayList<>();
        cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_GROUPS_NAME, null);
        if (cursor.moveToFirst()) {
            do { head.add(cursor.getString(2)); } while (cursor.moveToNext());
        }
        if (head.isEmpty())
            head.add("Пусто");

        String[] headers = new String[] { "Name" , "_id" };

        SimpleCursorAdapter userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                cursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        lv.setAdapter(userAdapter);

        Cursor cursor2 = db.rawQuery("select * from " + DatabaseHelper.TABLE_STUDENTS_NAME, null);
        String[] headers2 = new String[] { "Name" , "IDGroup" };
        SimpleCursorAdapter userAdapter2 = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                cursor2, headers2, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        lvst.setAdapter(userAdapter2);
    }
}
