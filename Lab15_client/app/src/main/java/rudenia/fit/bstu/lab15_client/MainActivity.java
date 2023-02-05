package rudenia.fit.bstu.lab15_client;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String AUTHORITY_GROUP = "by.bstu.providers.GroupsList";
    public static final String AUTHORITY_STUDENT = "by.bstu.providers.StudentsList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.get_groups).setOnClickListener(this);
        findViewById(R.id.get_students).setOnClickListener(this);
    }


    public String getGroups() {
        String string = "";
        Uri uri = Uri.parse("content://" + AUTHORITY_GROUP + "/groups");

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                string += "ID группы: " + cursor.getInt(0) + "\n" +
                        "\tФакультет:    " + cursor.getString(1) + "\n" +
                        "\tКурс:         " + cursor.getString(2) + "\n" +
                        "\tНаименование: " + cursor.getString(3) + "\n\n";
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return string;
    }

    public String getStudents() {
        String string = "";
        Uri uri = Uri.parse("content://" + AUTHORITY_STUDENT + "/student");

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                string += "ID группы: " + cursor.getInt(0) + "\n" +
                        "\tID студента:   " + cursor.getString(1) + "\n" +
                        "\tИмя:           " + cursor.getString(2) + "\n" +
                        "\tДата рождения: " + cursor.getString(3) + "\n" +
                        "\tАдрес:         " + cursor.getString(4) + "\n\n";
            }
            while (cursor.moveToNext());
            cursor.close();
        }

        return string;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_groups:
                Intent intentGroup = new Intent(MainActivity.this, ViewActivity.class);
                intentGroup.putExtra("view", getGroups());
                startActivity(intentGroup);
                break;
            case R.id.get_students:
                Intent intentStudent = new Intent(MainActivity.this, ViewActivity.class);
                intentStudent.putExtra("view", getStudents());
                startActivity(intentStudent);
                break;
        }
    }
}