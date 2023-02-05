package ast.fit.bstu.bd11_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    Calendar selectedStart;
    Calendar selectedFinish;

    Spinner spinner;
    EditText editPar;
    EditText editDay;
    EditText editMonth;
    EditText editYear;
    TextView textStart;
    TextView textFinish;
    String[] wayToSelect = new String[]{"Days", "Months", "Years"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        databaseHelper.onCreate(db);

        editDay = findViewById(R.id.editDay);
        editMonth = findViewById(R.id.editMonth);
        editYear = findViewById(R.id.editYear);

        selectedFinish = Calendar.getInstance();
        selectedStart = Calendar.getInstance();

        textFinish = findViewById(R.id.textFinish);
        textFinish.setText(dateFormat.format(selectedFinish.getTime()));
        textStart = findViewById(R.id.textStart);
        textStart.setText(dateFormat.format(selectedStart.getTime()));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wayToSelect);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void alterAverangeMark() {
        db.execSQL("drop view if exists " + DatabaseHelper.VIEW_AVERANGE_MARK);
        db.execSQL("create view " + DatabaseHelper.VIEW_AVERANGE_MARK +
                " as select " + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_NAME + "," +
                " AVG(" + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_MARK + ") " +
                "from " + DatabaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                " WHERE (strftime('%s', " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) > strftime('%s','" + dateFormat.format(selectedStart.getTime()) + "')) and (" +
                "strftime('%s', " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) < strftime('%s','" + dateFormat.format(selectedFinish.getTime()) + "'))" +
                "GROUP By (" + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_NAME + ")");
    }


    public void AverangeMarkOnClick(View view) {
        alterAverangeMark();
        cursor = db.rawQuery("select * from " + DatabaseHelper.VIEW_AVERANGE_MARK, new String[]{});
        String result = "";
        if (cursor.moveToFirst())
            do {
                result += cursor.getString(0) + " " + cursor.getDouble(1) + '\n';
            } while (cursor.moveToNext());
        dialogs(result);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        db.close();
        if (cursor != null) cursor.close();
    }


    public void dialogs(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s);
        builder.setPositiveButton("Ok", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Integer enteredValue;

    public void datePicker(View view) {
        try {
            enteredValue = Integer.parseInt(String.valueOf(editPar.getText()));
        } catch (NumberFormatException o) {
            dialogs("Введите корректные данные");
            return;
        }
        selectedStart = Calendar.getInstance();
        selectedFinish = Calendar.getInstance();
        if (spinner.getSelectedItem() == "Days") {
            selectedStart.set(Calendar.DAY_OF_MONTH, selectedStart.get(Calendar.DAY_OF_MONTH) - enteredValue);
        } else {
            if (spinner.getSelectedItem() == "Months") {
                selectedStart.set(Calendar.MONTH, selectedStart.get(Calendar.MONTH) - enteredValue);
            } else if (spinner.getSelectedItem() == "Years") {
                selectedStart.set(Calendar.YEAR, selectedStart.get(Calendar.YEAR) - enteredValue);
            }
        }
        textStart.setText(dateFormat.format(selectedStart.getTime()));
    }


    public void alterBestStudents() {
        db.execSQL("drop view if exists " + DatabaseHelper.VIEW_BEST_STUDENT);
        db.execSQL("create view " + DatabaseHelper.VIEW_BEST_STUDENT +
                " as select " + DatabaseHelper.TABLE_GROUP_NAME + "." + DatabaseHelper.GROUP_COLUMN_IDGROUP + "," + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_NAME + ",AVG(" + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_MARK + ")" +
                "from " + DatabaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_GROUP_NAME + " " +
                "on " + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDGROUP + "=" + DatabaseHelper.TABLE_GROUP_NAME + "." + DatabaseHelper.GROUP_COLUMN_IDGROUP + " " +
                " GROUP By " + DatabaseHelper.TABLE_GROUP_NAME + "." + DatabaseHelper.GROUP_COLUMN_IDGROUP+
                " ORDER By " + "AVG(" + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_MARK + ") desc");
    }


    public void BestStudentOnClick(View view) {
        alterBestStudents();
        String result = "";
        cursor = db.rawQuery("select * from " + DatabaseHelper.VIEW_BEST_STUDENT
                , null);
        if (cursor.moveToFirst())
            do {
                result += cursor.getInt(0) + "  " + cursor.getString(1) + "(" + cursor.getDouble(2) + ")\n";
            } while (cursor.moveToNext());
        dialogs(result);
    }

    int year, month, day;

    public void OnClickFinish(View view) {
        try {
            year = Integer.parseInt(String.valueOf(editYear.getText()));
            month = Integer.parseInt(String.valueOf(editMonth.getText()));
            day = Integer.parseInt(String.valueOf(editDay.getText()));
        } catch (NumberFormatException o) {
            dialogs("Введите корректные данные");
            return;
        }
        selectedFinish.set(Calendar.YEAR, year);
        selectedFinish.set(Calendar.MONTH, month - 1);
        selectedFinish.set(Calendar.DAY_OF_MONTH, day);
        textFinish.setText(dateFormat.format(selectedFinish.getTime()));
    }

    public void OnClickStart(View view) {
        try {
            year = Integer.parseInt(String.valueOf(editYear.getText()));
            month = Integer.parseInt(String.valueOf(editMonth.getText()));
            day = Integer.parseInt(String.valueOf(editDay.getText()));
        } catch (NumberFormatException o) {
            dialogs("Введите корректные данные");
            return;
        }
        selectedStart.set(Calendar.YEAR, year);
        selectedStart.set(Calendar.MONTH, month - 1);
        selectedStart.set(Calendar.DAY_OF_MONTH, day);
        textStart.setText(dateFormat.format(selectedStart.getTime()));
    }

    public void alterBadStudent() {
        db.execSQL("drop view if exists " + DatabaseHelper.VIEW_BAD_STUDENT);
        db.execSQL("create view " + DatabaseHelper.VIEW_BAD_STUDENT + " as select " + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_NAME + "," +
                DatabaseHelper.TABLE_SUBJECT_NAME + "." + DatabaseHelper.SUBJECT_COLUMN_SUBJECT + "," +
                DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_MARK +
                " from " + DatabaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_SUBJECT_NAME + " " +
                "on " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_IDSUBJECT + "=" + DatabaseHelper.TABLE_SUBJECT_NAME + "." + DatabaseHelper.SUBJECT_COLUMN_IDSUBJECT + " " +
                " WHERE ((strftime('%s', " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) > strftime('%s','" + dateFormat.format(selectedStart.getTime()) + "')) and (" +
                "strftime('%s', " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) < strftime('%s','" + dateFormat.format(selectedFinish.getTime()) + "'))) and(" +
                DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_MARK + "<4)");
    }

    public void BadStudentOnClick(View view) {
        alterBadStudent();
        cursor = db.rawQuery("select * from " + DatabaseHelper.VIEW_BAD_STUDENT, new String[]{});
        String result = "";
        if (cursor.moveToFirst())
            do {
                result += cursor.getString(0) + " " + cursor.getString(1) + "(" + cursor.getInt(2) + ")\n";
            } while (cursor.moveToNext());
        dialogs(result);
    }


    public void alterGroupsStat() {
        db.execSQL("drop view if exists " + DatabaseHelper.VIEW_GROUP_STATISTICS);
        db.execSQL("create view " + DatabaseHelper.VIEW_GROUP_STATISTICS +
                " as select " + DatabaseHelper.TABLE_SUBJECT_NAME + "." + DatabaseHelper.SUBJECT_COLUMN_SUBJECT + "," + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDGROUP + "," +
                " AVG(" + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_MARK + ") " +
                "from " + DatabaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_SUBJECT_NAME + " " +
                "on " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_IDSUBJECT + "=" + DatabaseHelper.TABLE_SUBJECT_NAME + "." + DatabaseHelper.SUBJECT_COLUMN_IDSUBJECT + " " +
                " WHERE (strftime('%s', " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) > strftime('%s','" + dateFormat.format(selectedStart.getTime()) + "')) and (" +
                "strftime('%s', " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) < strftime('%s','" + dateFormat.format(selectedFinish.getTime()) + "')) " +
                " GROUP By " + DatabaseHelper.TABLE_SUBJECT_NAME + "." + DatabaseHelper.SUBJECT_COLUMN_SUBJECT + "," + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDGROUP);
    }


    public void groupsStatOnClick(View view) {
        alterGroupsStat();
        String result = "";
        cursor = db.rawQuery("select * from " + DatabaseHelper.VIEW_GROUP_STATISTICS, new String[]{});

        if (cursor.moveToFirst())
            do {
                result += "\t" + cursor.getString(0) + " " + cursor.getInt(1) + "(" + cursor.getDouble(2) + ")\n";
            } while (cursor.moveToNext());
        dialogs(result);
    }

    public void alterFacultyStat() {
        db.execSQL("drop view if exists " + DatabaseHelper.VIEW_FACULTY_STATISTICS);
        db.execSQL("create view " + DatabaseHelper.VIEW_FACULTY_STATISTICS +
                " as select " + DatabaseHelper.TABLE_FACULTY_NAME + "." + DatabaseHelper.FACULTY_COLUMN_FACULTY +
                ", AVG(" + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_MARK + ") " +
                "from " + DatabaseHelper.TABLE_STUDENT_NAME + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_PROGRESS_NAME + " " +
                "on " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_IDSTUDENT + "=" + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDSTUDENT + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_GROUP_NAME + " " +
                "on " + DatabaseHelper.TABLE_GROUP_NAME + "." + DatabaseHelper.GROUP_COLUMN_IDGROUP + "=" + DatabaseHelper.TABLE_STUDENT_NAME + "." + DatabaseHelper.STUDENT_COLUMN_IDGROUP + " " +
                "Inner JOIN " + DatabaseHelper.TABLE_FACULTY_NAME + " " +
                "on " + DatabaseHelper.TABLE_FACULTY_NAME + "." + DatabaseHelper.FACULTY_COLUMN_IDFACULTY + "=" + DatabaseHelper.TABLE_GROUP_NAME + "." + DatabaseHelper.GROUP_COLUMN_FACULTY + " " +
                " WHERE ((strftime('%s', " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) > strftime('%s','" + dateFormat.format(selectedStart.getTime()) + "')) and (" +
                "strftime('%s', " + DatabaseHelper.TABLE_PROGRESS_NAME + "." + DatabaseHelper.PROGRESS_COLUMN_EXAMDATE + " ) < strftime('%s','" + dateFormat.format(selectedFinish.getTime()) + "')))" +
                " Group by (" + DatabaseHelper.TABLE_FACULTY_NAME + "." + DatabaseHelper.FACULTY_COLUMN_FACULTY + ")");
    }

    public void facultyStatOnClick(View view) {
        alterFacultyStat();
        String result = "";
        cursor = db.rawQuery("select * from " + DatabaseHelper.VIEW_FACULTY_STATISTICS +
                        " Order by 2 asc"
                , new String[]{});

        if (cursor.moveToFirst())
            do {
                result += cursor.getString(0) + "\n\t" + "Средний бал: " + cursor.getDouble(1) + '\n';
            } while (cursor.moveToNext());
        dialogs(result);
    }
    public void seestudents(View view)
    {
        String result = "";
        cursor = db.rawQuery("select "+ DatabaseHelper.STUDENT_COLUMN_NAME+" from " + DatabaseHelper.TABLE_STUDENT_NAME
                , new String[]{});

        if (cursor.moveToFirst())
            do {
                result += cursor.getString(0) + '\n';
            } while (cursor.moveToNext());
        dialogs(result);
    }

    public void addoddstudents(View view)
    {
        try {
            db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_STUDENT_NAME +
                    " (" + DatabaseHelper.STUDENT_COLUMN_IDGROUP + "," + DatabaseHelper.STUDENT_COLUMN_NAME + "," + DatabaseHelper.STUDENT_COLUMN_BIRTHDATE + "," + DatabaseHelper.STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (1,'Иван NTCN','15.12.2001','ул. Белорусская 21');");
            db.execSQL("INSERT INTO " + DatabaseHelper.TABLE_STUDENT_NAME +
                    " (" + DatabaseHelper.STUDENT_COLUMN_IDGROUP + "," + DatabaseHelper.STUDENT_COLUMN_NAME + "," + DatabaseHelper.STUDENT_COLUMN_BIRTHDATE + "," + DatabaseHelper.STUDENT_COLUMN_ADDRESS + ") " +
                    " VALUES (1,'Иван ntcn','15.12.2001','ул. Белорусская 21');");
        }
        catch(SQLException e)
        {
            dialogs(e.getMessage());
        }
    }
    public void deloleg(View view)
    {
        try {
            db.execSQL("DELETE FROM STUDENT WHERE IDSTUDENT=10");
        }catch(SQLException e)
        {
            dialogs("НИЗЯ ОТЧИСЛИТЬ ОЛЕГА"+e.getMessage());
        }
    }
}