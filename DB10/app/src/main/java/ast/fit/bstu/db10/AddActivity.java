package ast.fit.bstu.db10;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        EditText faculty= findViewById(R.id.faculty), course= findViewById(R.id.course), grname= findViewById(R.id.name),
                head= findViewById(R.id.head), stname= findViewById(R.id.namest), stgrname= findViewById(R.id.group);

        DatabaseHelper helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();

        Button addgr= findViewById(R.id.add_gr);
        addgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               helper.insertGroups( db, faculty.getText().toString(), Integer.parseInt(String.valueOf(course.getText())), grname.getText().toString(), head.getText().toString());
               db = helper.getReadableDatabase();
               Toast.makeText( AddActivity.this, "Добавлено! ", Toast.LENGTH_LONG).show();

            }
        });

        Button addst= findViewById(R.id.add_st);
        addst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               helper.insertStudent(db, Integer.parseInt(String.valueOf(stgrname.getText())), stname.getText().toString());
               db = helper.getReadableDatabase();
               Toast.makeText(AddActivity.this, "Добавлено! ", Toast.LENGTH_LONG).show();
            }
        });
    }
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }
}