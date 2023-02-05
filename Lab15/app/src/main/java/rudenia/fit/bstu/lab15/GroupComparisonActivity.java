package rudenia.fit.bstu.lab15;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import rudenia.fit.bstu.lab15.database.DbHelper;

public class GroupComparisonActivity extends AppCompatActivity implements View.OnClickListener {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText mDateWith, mDateOn;
    private String dateWith, dateOn;
    private CheckBox mCheckBox;
    private boolean subjectIsChecked;
    private Spinner mSubject;
    private GridView mResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_comparison);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        mDateWith = findViewById(R.id.date_with);
        mDateOn = findViewById(R.id.date_on);
        mCheckBox = findViewById(R.id.checkBox);
        mSubject = findViewById(R.id.subject);
        mResult = findViewById(R.id.result);

        findViewById(R.id.button_select).setOnClickListener(this);
        findViewById(R.id.checkBox).setOnClickListener(this);

        initSpiner();
    }


    protected void initSpiner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getSubjects());
        mSubject.setAdapter(adapter);
    }

    protected ArrayList<String> getSubjects() {
        ArrayList<String> data = new ArrayList<>();
        String query = "select SUBJECT.SUBJECT as subject from SUBJECT";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int subjectIndex = cursor.getColumnIndex("subject");
            do {
                data.add(cursor.getString(subjectIndex));
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

    protected boolean getPeriod() {
        if (formatDateIsCorrect(mDateWith.getText().toString())) {
            dateWith = mDateWith.getText().toString();
            if (formatDateIsCorrect(mDateOn.getText().toString())) {
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

    protected void selectAvgMarkByGroups(){
        if(getPeriod()) {
            mResult.setNumColumns(2);
            mResult.setAdapter(null);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.add("ID_GROUP");
            adapter.add("AVG_MARK");

            String query = "select IDGROUP, round(avg(MARK), 1) avgMark from studentProgressGroup "
                    + "where EXAMDATE BETWEEN '" + dateWith + "' and '" + dateOn + "' "
                    + "group by IDGROUP";
            Cursor cursor = db.rawQuery(query, null);


            if (cursor.moveToFirst()) {
                int idGroupIndex = cursor.getColumnIndex("IDGROUP");
                int avgMarkIndex = cursor.getColumnIndex("avgMark");
                do {
                    adapter.add(cursor.getString(idGroupIndex));
                    adapter.add(cursor.getString(avgMarkIndex));
                } while (cursor.moveToNext());
            }
            mResult.setAdapter(adapter);
            cursor.close();
        }
    }

    protected void selectAvgMarkBySubject(){
        if(getPeriod()) {
            mResult.setNumColumns(3);
            mResult.setAdapter(null);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.add("ID_GROUP");
            adapter.add("ID_SUBJECT");
            adapter.add("AVG_MARK");

            String query = "select IDGROUP idGroup, SUBJECT subject, round(avg(MARK), 1) avgMark from subjectProgress "
                    + "where SUBJECT = '" + mSubject.getSelectedItem().toString() + "' "
                    + "and EXAMDATE BETWEEN '" + dateWith + "' and '" + dateOn + "' "
                    + "group by IDGROUP";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                int idGroupIndex = cursor.getColumnIndex("idGroup");
                int subjectIndex = cursor.getColumnIndex("subject");
                int avgMarkIndex = cursor.getColumnIndex("avgMark");
                do {
                    adapter.add(cursor.getString(idGroupIndex));
                    adapter.add(cursor.getString(subjectIndex));
                    adapter.add(cursor.getString(avgMarkIndex));
                } while (cursor.moveToNext());
            }
            mResult.setAdapter(adapter);
            cursor.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.checkBox:
                if(mCheckBox.isChecked()) {
                    mSubject.setVisibility(View.VISIBLE);
                    subjectIsChecked = true;
                } else {
                    mSubject.setVisibility(View.GONE);
                    subjectIsChecked = false;
                }
                break;
            case R.id.button_select:
                if(!mCheckBox.isChecked()) {
                    selectAvgMarkByGroups();
                } else {
                    selectAvgMarkBySubject();
                }
                break;
        }
    }
}