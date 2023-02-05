package karbanovich.fit.bstu.students.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import karbanovich.fit.bstu.students.Database.DbHelper;
import karbanovich.fit.bstu.students.Helper.SpinnerSetter;
import karbanovich.fit.bstu.students.Helper.SpinnerSetterResult;
import karbanovich.fit.bstu.students.Model.Group;
import karbanovich.fit.bstu.students.Model.Student;
import karbanovich.fit.bstu.students.R;

public class StudentsActivity extends AppCompatActivity {

    private Spinner spnrGroup;
    private ListView listViewStudents;

    private SQLiteDatabase db;
    private CustomListAdapter customListAdapter;
    private List<Group> listGroups;
    private ArrayList<Student> listStudentsByGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        getSupportActionBar().hide();

        binding();
        setListeners();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    private void binding() {
        spnrGroup = findViewById(R.id.spnrGroup);
        listViewStudents = findViewById(R.id.listViewStudents);

        db = new DbHelper(getApplicationContext()).getReadableDatabase();

        SpinnerSetterResult<Group> spnrGroupResult = SpinnerSetter.setGroups(getApplicationContext());
        listGroups = spnrGroupResult.getList();
        ArrayAdapter<String> groupAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spnrGroupResult.getStr());
        spnrGroup.setAdapter(groupAdapter);

        listStudentsByGroup = new ArrayList<>();
        customListAdapter = new CustomListAdapter(this, listStudentsByGroup);
        listViewStudents.setAdapter(customListAdapter);
    }

    private void setListeners() {
        spnrGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerSetterResult<Student> spnrStudentResult =
                        SpinnerSetter.setStudents(getApplicationContext(),
                                listGroups.size(),
                                listGroups.get(spnrGroup.getSelectedItemPosition()).getId());

                customListAdapter.updateStudentsList(spnrStudentResult.getList());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {return;}
        });
    }

    public class CustomListAdapter extends BaseAdapter {

        private ArrayList<Student> students;
        private Context context;


        public CustomListAdapter(Context context, ArrayList<Student> students) {
            this.context = context;
            this.students = students;
        }

        @Override
        public int getCount() {return students.size();}

        @Override
        public Object getItem(int i) {return null;}

        @Override
        public long getItemId(int i) {return 0;}

        public void updateStudentsList(ArrayList<Student> students) {
            this.students.clear();
            this.students.addAll(students);
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int pos, View vw, ViewGroup viewGroup) {
            View view = getLayoutInflater().inflate(R.layout.students_item, null);

            TextView itemName = (TextView) view.findViewById(R.id.itemName);
            TextView itemBirthday = (TextView) view.findViewById(R.id.itemBirthday);
            TextView itemAddress = (TextView) view.findViewById(R.id.itemAddress);

            itemName.setText(students.get(pos).getId() + " - " + students.get(pos).getName());
            itemBirthday.setText(students.get(pos).getBirthday().toString());
            itemAddress.setText(students.get(pos).getAddress());

            view.setOnClickListener(v -> {
                Intent intent = new Intent(StudentsActivity.this, SelectedStudentActivity.class);
                intent.putExtra("Student", students.get(pos));
                startActivity(intent);
            });

            return view;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}