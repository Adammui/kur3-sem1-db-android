package karbanovich.fit.bstu.contentproviderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConcreteStudentActivity extends AppCompatActivity {

    private EditText searchStudentId;
    private Button getStudent;
    private EditText studentName;
    private EditText avgMark;
    private Button deleteSelectedStudent;
    private Button editSelectedStudent;
    private Button addStudent;

    private int idOfFoundStudent;
    private final Uri STUDENT_URI =
            Uri.parse("content://com.karbanovich.students.authority/STUDENT");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_student);

        binding();
        setListeners();
    }

    private void binding() {
        searchStudentId = findViewById(R.id.edtTxtSearchStudentId);
        getStudent = findViewById(R.id.btnGetStudent);
        studentName = findViewById(R.id.edtTxtStudentName);
        avgMark = findViewById(R.id.edtTxtAvgMark);
        deleteSelectedStudent = findViewById(R.id.btnDeleteSelectedStudent);
        editSelectedStudent = findViewById(R.id.btnEditSelectedStudent);
        addStudent = findViewById(R.id.btnAddStudent);
        idOfFoundStudent = -1;
    }

    private void setListeners() {
        getStudent.setOnClickListener(view -> {
            try {
                String id = searchStudentId.getText().toString();
                if (id.equals("")) {
                    Toast.makeText(this, "Введите ID студента", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = getContentResolver().query(Uri.parse(STUDENT_URI.toString() + "/" + id),
                        null, null, null, null);

                if(!cursor.moveToFirst()) {
                    idOfFoundStudent = -1;
                    studentName.getText().clear();
                    avgMark.getText().clear();
                    Toast.makeText(this, "Такого студента нет", Toast.LENGTH_SHORT).show();
                } else {
                    idOfFoundStudent = Integer.parseInt(id);
                    studentName.setText(cursor.getString(0));
                    avgMark.setText(cursor.getString(1));
                }

                cursor.close();
            } catch (Exception e) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });

        deleteSelectedStudent.setOnClickListener(view -> {
            if(idOfFoundStudent == -1) {
                Toast.makeText(this, "Студент не выбран", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                getContentResolver().delete(Uri.parse(STUDENT_URI.toString() + "/" + idOfFoundStudent),
                        null, null);

                idOfFoundStudent = -1;
                searchStudentId.getText().clear();
                studentName.getText().clear();
                avgMark.getText().clear();

                Toast.makeText(this, "Студент удален", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });

        editSelectedStudent.setOnClickListener(view -> {
            if(idOfFoundStudent == -1) {
                Toast.makeText(this, "Студент не выбран", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                ContentValues values = new ContentValues();

                values.put("NAME", studentName.getText().toString());

                getContentResolver().update(Uri.parse(STUDENT_URI.toString() + "/" + idOfFoundStudent),
                        values, null, null);

                Toast.makeText(this, "Студент успешно изменен", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });

        addStudent.setOnClickListener(view ->
            startActivity(new Intent(this, AddStudentActivity.class)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}