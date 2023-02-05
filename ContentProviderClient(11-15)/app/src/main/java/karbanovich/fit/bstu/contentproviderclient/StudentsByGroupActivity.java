package karbanovich.fit.bstu.contentproviderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class StudentsByGroupActivity extends AppCompatActivity {

    private Button deleteAllStudentsByGroup;
    private EditText searchGroupId;
    private Button getStudents;
    private ListView studentsByGroup;

    private int selectedGroupId;
    private String[] studentData;
    private final Uri STUDENTS_URI =
            Uri.parse("content://com.karbanovich.students.authority/STUDENT");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_by_group);

        binding();
        setListeners();
    }

    private void binding() {
        deleteAllStudentsByGroup = findViewById(R.id.btnDeleteAllStudentsByGroup);
        searchGroupId = findViewById(R.id.edtTxtSearchGroupId);
        getStudents = findViewById(R.id.btnGetStudents);
        studentsByGroup = findViewById(R.id.listViewStudentsByGroup);
        selectedGroupId = -1;
    }

    private void setListeners() {
        getStudents.setOnClickListener(view -> {
            try {
                if(searchGroupId.getText().toString().equals("")) {
                    selectedGroupId = -1;
                    Toast.makeText(this, "Введите номер группы", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedGroupId = Integer.parseInt(searchGroupId.getText().toString());

                Cursor cursor = getContentResolver().query(STUDENTS_URI,
                        null, searchGroupId.getText().toString(), null, null);
                studentData = new String[cursor.getCount()];
                int i = 0;

                if(cursor.getCount() == 0) {
                    selectedGroupId = -1;
                    deleteAllStudentsByGroup.setEnabled(false);
                }
                else
                    deleteAllStudentsByGroup.setEnabled(true);

                while (cursor.moveToNext()) {
                    studentData[i++] = "ID: " + cursor.getString(0) + "\r\n" +
                            "\t\tИмя: " + cursor.getString(2) + "\r\n" +
                            "\t\tДата рождения: " + cursor.getString(3) + "\r\n" +
                            "\t\tАдрес: " + cursor.getString(4) + "\r\n";
                }
                cursor.close();

                ArrayAdapter<String> adapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1, studentData);
                studentsByGroup.setAdapter(adapter);
            } catch (Exception e ) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });

        deleteAllStudentsByGroup.setOnClickListener(view -> {
            try {
                if (selectedGroupId == -1) {
                    Toast.makeText(this, "В группе нет студентов", Toast.LENGTH_SHORT).show();
                    return;
                }

                getContentResolver().delete(STUDENTS_URI, String.valueOf(selectedGroupId), null);
                Toast.makeText(this, "Студенты успешно удалены", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            } catch (Exception e ) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });
    }
}