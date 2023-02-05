package rudenia.fit.bstu.lab15;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import rudenia.fit.bstu.lab15.database.DbHelper;

public class BadStudentsActivity extends AppCompatActivity implements View.OnClickListener {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText mDateWith, mDateOn;
    private String dateWith, dateOn;
    private Spinner mFaculty;
    private GridView mResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bad_students);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        mDateWith = findViewById(R.id.date_with);
        mDateOn = findViewById(R.id.date_on);
        mFaculty = findViewById(R.id.faculty);
        mResult =  findViewById(R.id.result);

        findViewById(R.id.button_select).setOnClickListener(this);

        initSpiner();
    }


    protected void initSpiner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getFacultys());
        mFaculty.setAdapter(adapter);
    }

    protected ArrayList<String> getFacultys() {
        ArrayList<String> data = new ArrayList<>();
        String query = "select FACULTY.FACULTY as faculty from FACULTY";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int facultyIndex = cursor.getColumnIndex("faculty");
            do {
                data.add(cursor.getString(facultyIndex));
            } while (cursor.moveToNext());
        }
        return data;
    }

    protected boolean formatDateIsCorrect(String date) {
        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        return df.parse(date, new ParsePosition(0)) != null;
    }

    protected boolean getPeriod(){
        if(formatDateIsCorrect(mDateWith.getText().toString())) {
            dateWith = mDateWith.getText().toString();
            if(formatDateIsCorrect(mDateOn.getText().toString())) {
                dateOn = mDateOn.getText().toString();
                return true;
            } else {
                mDateOn.setError("Неверный формат даты");
                mDateOn.requestFocus();
                return false;
            }
        } else {
            mDateWith.setError("Неверный формат даты");
            mDateWith.requestFocus();
            return false;
        }
    }

    protected void selectStudentsWhereMarkLessThenFourOnFaculty() {
        if(getPeriod()) {
            mResult.setNumColumns(4);
            mResult.setAdapter(null);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.add("FACULTY");
            adapter.add("GROUP");
            adapter.add("NAME");
            adapter.add("MARK");

            String query = "select FACULTY, groupName, strudentName, MARK from studentProgressGroup "
                    + "where FACULTY = '" + mFaculty.getSelectedItem().toString() + "' "
                    + "and EXAMDATE BETWEEN '" + dateWith + "' and '" + dateOn + "' "
                    + "and MARK < 4 "
                    + "order by MARK desc";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                int facultyIndex = cursor.getColumnIndex("FACULTY");
                int groupNameIndex = cursor.getColumnIndex("groupName");
                int strudentNameIndex = cursor.getColumnIndex("strudentName");
                int markIndex = cursor.getColumnIndex("MARK");
                do {
                    adapter.add(cursor.getString(facultyIndex));
                    adapter.add(cursor.getString(groupNameIndex));
                    adapter.add(cursor.getString(strudentNameIndex));
                    adapter.add(cursor.getString(markIndex));
                } while (cursor.moveToNext());
            }
            mResult.setAdapter(adapter);
            cursor.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select:
                selectStudentsWhereMarkLessThenFourOnFaculty();
                break;
        }
    }
}