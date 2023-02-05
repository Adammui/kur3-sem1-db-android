package karbanovich.fit.bstu.students.Activity.AddActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import karbanovich.fit.bstu.students.Activity.MainActivity;
import karbanovich.fit.bstu.students.R;

public class AddActivity extends AppCompatActivity {

    private Button addFacultyActivity;
    private Button addGroupActivity;
    private Button addStudentActivity;
    private Button addSubjectActivity;
    private Button addProgressActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        binding();
        setListeners();
    }

    private void binding() {
        addFacultyActivity = findViewById(R.id.btnAddFacultyActivity);
        addGroupActivity = findViewById(R.id.btnAddGroupActivity);
        addStudentActivity = findViewById(R.id.btnAddStudentActivity);
        addSubjectActivity = findViewById(R.id.btnAddSubjectActivity);
        addProgressActivity = findViewById(R.id.btnAddProgressActivity);
    }

    private void setListeners() {
        addFacultyActivity.setOnClickListener(view ->
                startActivity(new Intent(this, AddFacultyActivity.class)));
        addGroupActivity.setOnClickListener(view ->
                startActivity(new Intent(this, AddGroupActivity.class)));
        addStudentActivity.setOnClickListener(view ->
                startActivity(new Intent(this, AddStudentActivity.class)));
        addSubjectActivity.setOnClickListener(view ->
                startActivity(new Intent(this, AddSubjectActivity.class)));
        addProgressActivity.setOnClickListener(view ->
                startActivity(new Intent(this, AddProgressActivity.class)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}