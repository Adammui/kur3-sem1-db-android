package karbanovich.fit.bstu.students.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Calendar;

import karbanovich.fit.bstu.students.Activity.AddActivities.AddActivity;
import karbanovich.fit.bstu.students.Database.DbHelper;
import karbanovich.fit.bstu.students.Database.StudentDb;
import karbanovich.fit.bstu.students.Model.Student;
import karbanovich.fit.bstu.students.R;

public class SelectedStudentActivity extends AppCompatActivity {

    private EditText name;
    private EditText birthday;
    private Calendar date;
    private EditText address;
    private Button saveStudent;
    private Button deleteStudent;

    private Student selectedStudent;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_student);

        binding();
        setListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        name = findViewById(R.id.edtStudentName);
        birthday = findViewById(R.id.edtStudentBirthday);
        date = Calendar.getInstance();
        address = findViewById(R.id.edtStudentAddress);
        saveStudent = findViewById(R.id.btnAddStudent);
        deleteStudent = findViewById(R.id.btnDeleteStudent);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();

        Intent intent = getIntent();
        selectedStudent = (Student) intent.getSerializableExtra("Student");

        name.setText(selectedStudent.getName());
        birthday.setText(selectedStudent.getBirthday().toString());
        address.setText(selectedStudent.getAddress());
    }

    private void setListeners() {
        saveStudent.setOnClickListener(view -> {
            try {
                Student student = new Student();
                student.setId(selectedStudent.getId());
                student.setGroupId(selectedStudent.getGroupId());
                student.setName(name.getText().toString());
                LocalDate birthday = LocalDate.of(
                        Integer.parseInt(this.birthday.getText().toString().substring(0, 4)),
                        Integer.parseInt(this.birthday.getText().toString().substring(5, 7)),
                        Integer.parseInt(this.birthday.getText().toString().substring(8, 10)));

                student.setBirthday(birthday);
                student.setAddress(address.getText().toString());

                StudentDb.updateStudent(db, student);
                Toast.makeText(this, "Студент успешно изменен", Toast.LENGTH_SHORT).show();
            } catch (Exception e ) {
                Toast.makeText(this, "Ошибка изменения", Toast.LENGTH_SHORT).show();
            }
        });

        deleteStudent.setOnClickListener(view -> {
            try {
                if(StudentDb.deleteStudent(db, selectedStudent) > 0) {
                    Toast.makeText(this, "Студент успешно удален", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, StudentsActivity.class));
                }
            } catch (SQLiteConstraintException e) {
                Toast.makeText(this, "Мало студентов", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка удаления", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, StudentsActivity.class));
    }
}