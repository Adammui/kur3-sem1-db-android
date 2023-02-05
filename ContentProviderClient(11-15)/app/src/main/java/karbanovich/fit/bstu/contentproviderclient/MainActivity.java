package karbanovich.fit.bstu.contentproviderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button getAllGroups;
    private Button getConcreteGroup;
    private Button getStudentsByGroup;
    private Button getConcreteStudent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding();
        setListeners();
    }

    private void binding() {
        getAllGroups = findViewById(R.id.btnGetAllGroups);
        getConcreteGroup = findViewById(R.id.btnGetConcreteGroup);
        getStudentsByGroup = findViewById(R.id.btnGetStudentsByGroup);
        getConcreteStudent = findViewById(R.id.btnGetConcreteStudent);
    }

    private void setListeners() {
        getAllGroups.setOnClickListener(view ->
                startActivity(new Intent(this, GroupsActivity.class)));
        getConcreteGroup.setOnClickListener(view ->
                startActivity(new Intent(this, ConcreteGroupActivity.class)));
        getStudentsByGroup.setOnClickListener(view ->
                startActivity(new Intent(this, StudentsByGroupActivity.class)));
        getConcreteStudent.setOnClickListener(view ->
            startActivity(new Intent(this, ConcreteStudentActivity.class)));
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}