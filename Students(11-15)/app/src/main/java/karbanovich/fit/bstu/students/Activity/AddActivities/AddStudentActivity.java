package karbanovich.fit.bstu.students.Activity.AddActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import karbanovich.fit.bstu.students.Activity.AddActivities.AddActivity;
import karbanovich.fit.bstu.students.Database.DbHelper;
import karbanovich.fit.bstu.students.Database.GroupDb;
import karbanovich.fit.bstu.students.Database.StudentDb;
import karbanovich.fit.bstu.students.Helper.DateTimeHelper;
import karbanovich.fit.bstu.students.Helper.SystemHelper;
import karbanovich.fit.bstu.students.Model.Group;
import karbanovich.fit.bstu.students.Model.Student;
import karbanovich.fit.bstu.students.R;

public class AddStudentActivity extends AppCompatActivity {

    private Spinner spnrGroup;
    private EditText name;
    private EditText birthday;
    private Calendar date;
    private EditText address;
    private CheckBox isHead;
    private Button addStudent;

    private SQLiteDatabase db;
    private List<Group> listGroups;
    private String[] strGroups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        binding();
        setDateListeners();
        setButtonListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        spnrGroup = findViewById(R.id.spnrGroup);
        name = findViewById(R.id.edtStudentName);
        birthday = findViewById(R.id.edtStudentBirthday);
        date = Calendar.getInstance();
        address = findViewById(R.id.edtStudentAddress);
        isHead = findViewById(R.id.chckIsHeadOfGroup);
        addStudent = findViewById(R.id.btnAddStudent);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();

        setGroups();
        ArrayAdapter<String> groupAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strGroups);
        spnrGroup.setAdapter(groupAdapter);
    }

    private void setDateListeners() {
        birthday.setInputType(InputType.TYPE_NULL);

        DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
            birthday.setText(DateTimeHelper.getGeneralDateFormat(year, monthOfYear + 1, dayOfMonth));
        };

        DatePickerDialog birthdayDialog = new DatePickerDialog(this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));

        birthday.setOnClickListener(view -> {
            SystemHelper.hideKeyboard(this);
            birthdayDialog.show();
        });
        birthday.setOnFocusChangeListener((view, hasFocus) -> {
            if(hasFocus) {
                SystemHelper.hideKeyboard(this);
                birthdayDialog.show();
            }
        });
    }

    private void setButtonListeners() {
        addStudent.setOnClickListener(view -> {
            try {
                int groupId = listGroups.get(spnrGroup.getSelectedItemPosition()).getId();
                String name = this.name.getText().toString();
                LocalDate birthday = LocalDate.of(
                        Integer.parseInt(this.birthday.getText().toString().substring(6, 10)),
                        Integer.parseInt(this.birthday.getText().toString().substring(3, 5)),
                        Integer.parseInt(this.birthday.getText().toString().substring(0, 2)));
                String address = this.address.getText().toString();

                Student student = new Student(groupId, name, birthday, address);

                if(StudentDb.add(db, student) != -1) {
                    if(this.isHead.isChecked())
                        GroupDb.updateHead(db, student.getGroupId(), student.getName());

                    Toast.makeText(this, "Студент успешно добавлен", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
            } catch (SQLiteConstraintException e) {
                Toast.makeText(this, "Много студентов", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        name.setText("");
        birthday.setText("");
        address.setText("");
        isHead.setChecked(false);
    }

    private void setGroups() {
        listGroups = new ArrayList<>();
        Cursor cursor = GroupDb.getAll(db);

        if(cursor.getCount() == 0) {
            strGroups = new String[] { "-" };
            return;
        }

        strGroups = new String[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            Group group = new Group();

            group.setId(cursor.getInt(cursor.getColumnIndexOrThrow("IDGROUP")));
            group.setName(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
            group.setCourse(cursor.getInt(cursor.getColumnIndexOrThrow("COURSE")));

            listGroups.add(i, group);
            strGroups[i++] = group.getId() + " " + group.getName() + "-" + group.getCourse();
        }
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AddActivity.class));
    }
}