package rudenia.fit.bstu.lab15;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;

import androidx.appcompat.app.AppCompatActivity;
import rudenia.fit.bstu.lab15.database.DbHelper;

public class FacultyComparisonActivity extends AppCompatActivity implements View.OnClickListener {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText mDateWith, mDateOn;
    private String dateWith, dateOn;
    private GridView mResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_comparison);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        mDateWith = findViewById(R.id.date_with);
        mDateOn = findViewById(R.id.date_on);
        mResult =  findViewById(R.id.result);

        findViewById(R.id.button_select).setOnClickListener(this);
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

    protected void selectAvgMarkByFaculty() {
        if(getPeriod()) {
            mResult.setNumColumns(2);
            mResult.setAdapter(null);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.add("FACULTY");
            adapter.add("AVG_MARK");

            String query = "select FACULTY, round(avg(MARK), 1) avg_mark from studentProgressGroup "
                    + "where EXAMDATE BETWEEN '" + dateWith + "' and '" + dateOn + "' "
                    + "group by FACULTY "
                    + "order by avg_mark asc";
            Cursor c = db.rawQuery(query, null);


            if (c.moveToFirst()) {
                int facultyIndex = c.getColumnIndex("FACULTY");
                int avgMarkIndex = c.getColumnIndex("avg_mark");
                do {
                    adapter.add(c.getString(facultyIndex));
                    adapter.add(c.getString(avgMarkIndex));
                } while (c.moveToNext());
            }
            mResult.setAdapter(adapter);
            c.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_select:
                selectAvgMarkByFaculty();
                break;
        }
    }
}
