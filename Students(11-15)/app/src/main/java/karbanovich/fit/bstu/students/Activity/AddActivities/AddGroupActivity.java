package karbanovich.fit.bstu.students.Activity.AddActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import karbanovich.fit.bstu.students.Activity.AddActivities.AddActivity;
import karbanovich.fit.bstu.students.Database.DbHelper;
import karbanovich.fit.bstu.students.Database.FacultyDb;
import karbanovich.fit.bstu.students.Database.GroupDb;
import karbanovich.fit.bstu.students.Helper.SpinnerSetter;
import karbanovich.fit.bstu.students.Helper.SpinnerSetterResult;
import karbanovich.fit.bstu.students.Model.Faculty;
import karbanovich.fit.bstu.students.Model.Group;
import karbanovich.fit.bstu.students.R;

public class AddGroupActivity extends AppCompatActivity {

    private Spinner spnrFaculty;
    private EditText course;
    private EditText groupName;
    private Button addGroup;

    private SQLiteDatabase db;
    private List<Faculty> listFaculties;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        binding();
        setListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        spnrFaculty = findViewById(R.id.spnrFaculty);
        course = findViewById(R.id.edtCourse);
        groupName = findViewById(R.id.edtGroupName);
        addGroup = findViewById(R.id.btnAddGroup);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();

        SpinnerSetterResult<Faculty> spnrResult = SpinnerSetter.setFaculties(getApplicationContext());
        listFaculties = spnrResult.getList();
        ArrayAdapter<String> facultyAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnrResult.getStr());
        spnrFaculty.setAdapter(facultyAdapter);
    }

    private void setListeners() {
        addGroup.setOnClickListener(view -> {
            try {
                int course = Integer.parseInt(this.course.getText().toString());
                String groupName = this.groupName.getText().toString();

                Group group = new Group(listFaculties.get(spnrFaculty.getSelectedItemPosition()).getId(), course, groupName, null);

                if(GroupDb.add(db, group) != -1) {
                    Toast.makeText(this, "Группа успешно добавлен", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        course.setText("");
        groupName.setText("");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AddActivity.class));
    }
}