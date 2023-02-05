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

import androidx.appcompat.app.AppCompatActivity;
import rudenia.fit.bstu.lab15.database.DbHelper;

public class GroupComparison2Activity extends AppCompatActivity  implements View.OnClickListener {

    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private EditText mDateWith, mDateOn;
    private String dateWith, dateOn;
    private int numberOfSelectedRadioButton = 0;
    private Spinner mStuedentName, mGroupsId;
    private GridView mResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_comparison2);

        dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        mDateWith = findViewById(R.id.date_with);
        mDateOn = findViewById(R.id.date_on);
        mResult = findViewById(R.id.result);
        mStuedentName = findViewById(R.id.student_name);
        mGroupsId = findViewById(R.id.groups_id);

        findViewById(R.id.radio_button1).setOnClickListener(this);
        findViewById(R.id.radio_button2).setOnClickListener(this);
        findViewById(R.id.radio_button3).setOnClickListener(this);
        findViewById(R.id.button_select).setOnClickListener(this);

        initSpiners();
    }


    protected void initSpiners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getStudents());
        mStuedentName.setAdapter(adapter);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getGroupsId());
        mGroupsId.setAdapter(adapter);
    }

    protected ArrayList<String> getStudents() {
        ArrayList<String> data = new ArrayList<>();
        String query = "select STUDENT.NAME as students from STUDENT";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int studentNameIndex = cursor.getColumnIndex("students");
            do {
                data.add(cursor.getString(studentNameIndex));
            } while (cursor.moveToNext());
        }
        return data;
    }

    protected ArrayList<String> getGroupsId() {
        ArrayList<String> data = new ArrayList<>();
        String query = "select GROUP_.IDGROUP as groupsId from GROUP_";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int groupsIdIndex = cursor.getColumnIndex("groupsId");
            do {
                data.add(cursor.getString(groupsIdIndex));
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

    protected void selectAvgForEveryStudentBySubject(){
        if (getPeriod()) {
            mResult.setNumColumns(4);
            mResult.setAdapter(null);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.add("IDGROUP");
            adapter.add("SUBJECT");
            adapter.add("NAME");
            adapter.add("AVG_MARK");

            String query = "select IDGROUP, SUBJECT, NAME, round(avg(MARK), 1) avg_mark from subjectProgress "
                    + "where EXAMDATE BETWEEN '" + dateWith + "' and '" + dateOn + "' "
                    + "group by SUBJECT, NAME "
                    + "order by IDGROUP asc";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                int idGroupIndex = cursor.getColumnIndex("IDGROUP");
                int idSubjectIndex = cursor.getColumnIndex("SUBJECT");
                int nameIndex = cursor.getColumnIndex("NAME");
                int avgMarkIndex = cursor.getColumnIndex("avg_mark");
                do {
                    adapter.add(cursor.getString(idGroupIndex));
                    adapter.add(cursor.getString(idSubjectIndex));
                    adapter.add(cursor.getString(nameIndex));
                    adapter.add(cursor.getString(avgMarkIndex));
                } while (cursor.moveToNext());
            }
            mResult.setAdapter(adapter);
            cursor.close();
        }
    }

    protected void selectAvgBySpecificStudent(){
        if(getPeriod()) {
            mResult.setNumColumns(3);
            mResult.setAdapter(null);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.add("NAME");
            adapter.add("SUBJECT");
            adapter.add("AVG_MARK");

            String query = "select NAME, SUBJECT, round(avg(MARK), 1) avg_mark from subjectProgress "
                    + "where EXAMDATE BETWEEN '" + dateWith + "' and '" + dateOn + "' and NAME = '" + mStuedentName.getSelectedItem().toString() + "' "
                    + "group by SUBJECT";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("NAME");
                int idSubjectIndex = cursor.getColumnIndex("SUBJECT");
                int avgMarkIndex = cursor.getColumnIndex("avg_mark");
                do {
                    adapter.add(cursor.getString(nameIndex));
                    adapter.add(cursor.getString(idSubjectIndex));
                    adapter.add(cursor.getString(avgMarkIndex));
                } while (cursor.moveToNext());
            }
            mResult.setAdapter(adapter);
            cursor.close();
        }
    }

    protected void selectAvgBySpecificGroup(){
        if(getPeriod()) {
            mResult.setNumColumns(2);
            mResult.setAdapter(null);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            adapter.add("ID_GROUP");
            adapter.add("AVG_MARK");

            String query = "select IDGROUP, round(avg(MARK), 1) avg_mark from subjectProgress "
                    + "where EXAMDATE BETWEEN '" + dateWith + "' and '" + dateOn + "' and IDGROUP = '" + mGroupsId.getSelectedItem().toString() + "' "
                    + "group by IDGROUP";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                int IDGRColIndex = cursor.getColumnIndex("IDGROUP");
                int MARKColIndex = cursor.getColumnIndex("avg_mark");
                do {
                    adapter.add(cursor.getString(IDGRColIndex));
                    adapter.add(cursor.getString(MARKColIndex));
                } while (cursor.moveToNext());
            }
            mResult.setAdapter(adapter);
            cursor.close();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_button1:
                mStuedentName.setVisibility(View.GONE);
                mGroupsId.setVisibility(View.GONE);
                numberOfSelectedRadioButton = 0;
                break;
            case R.id.radio_button2:
                mStuedentName.setVisibility(View.VISIBLE);
                mGroupsId.setVisibility(View.GONE);
                numberOfSelectedRadioButton = 1;
                break;
            case R.id.radio_button3:
                mStuedentName.setVisibility(View.GONE);
                mGroupsId.setVisibility(View.VISIBLE);
                numberOfSelectedRadioButton = 2;
                break;
            case R.id.button_select:
                if(numberOfSelectedRadioButton == 0) {
                    selectAvgForEveryStudentBySubject();
                } else if(numberOfSelectedRadioButton == 1) {
                    selectAvgBySpecificStudent();
                } else if(numberOfSelectedRadioButton == 2) {
                    selectAvgBySpecificGroup();
                }
                break;
        }
    }
}