package karbanovich.fit.bstu.contentproviderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity {

    private EditText groupId;
    private EditText name;
    private EditText birthday;
    private Calendar date;
    private EditText address;
    private Button addStudent;

    private final Uri STUDENT_URI =
            Uri.parse("content://com.karbanovich.studentdb.authority/STUDENT");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        binding();
    }

    private void binding() {
        groupId = findViewById(R.id.edtGroupId);
        name = findViewById(R.id.edtStudentName);
        birthday = findViewById(R.id.edtStudentBirthday);
        date = Calendar.getInstance();
        address = findViewById(R.id.edtStudentAddress);
        addStudent = findViewById(R.id.btnAddStudent);

        addStudent.setOnClickListener(view -> {
            try {
                ContentValues values = new ContentValues();

                values.put("IDGROUP", groupId.getText().toString());
                values.put("NAME", name.getText().toString());
                values.put("BIRTHDAY", birthday.getText().toString());
                values.put("ADDRESS", address.getText().toString());

                getContentResolver().insert(STUDENT_URI, values);

                Toast.makeText(this, "Студент успешно добавлен", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ConcreteStudentActivity.class));
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ConcreteStudentActivity.class));
    }
}