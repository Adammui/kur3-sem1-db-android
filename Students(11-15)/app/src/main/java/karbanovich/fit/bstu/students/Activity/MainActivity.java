package karbanovich.fit.bstu.students.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import karbanovich.fit.bstu.students.Activity.AddActivities.AddActivity;
import karbanovich.fit.bstu.students.Activity.ShowActivities.ShowActivity;
import karbanovich.fit.bstu.students.ContentProvider.StudentsContract;
import karbanovich.fit.bstu.students.R;

public class MainActivity extends AppCompatActivity {

    private Button addActivity;
    private Button editActivity;
    private Button showActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Uri: ", StudentsContract.CONTENT_GROUP_URI.toString());

        binding();
        setListeners();
    }

    private void binding() {
        addActivity = findViewById(R.id.btnAddActivity);
        editActivity = findViewById(R.id.btnEditActivity);
        showActivity = findViewById(R.id.btnShowActivity);
    }

    private void setListeners() {
        addActivity.setOnClickListener(view ->
                startActivity(new Intent(this, AddActivity.class)));
        editActivity.setOnClickListener(view ->
                startActivity(new Intent(this, StudentsActivity.class)));
        showActivity.setOnClickListener(view ->
                startActivity(new Intent(this, ShowActivity.class)));
    }

    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }
}