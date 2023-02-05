package karbanovich.fit.bstu.contentproviderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddGroupActivity extends AppCompatActivity {

    private EditText course;
    private EditText groupName;
    private Button addGroup;

    private final Uri GROUPS_URI =
            Uri.parse("content://com.karbanovich.studentdb.authority/GROUP");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        binding();
    }

    private void binding() {
        course = findViewById(R.id.edtCourse);
        groupName = findViewById(R.id.edtGroupName);
        addGroup = findViewById(R.id.btnAddGroup);

        addGroup.setOnClickListener(view -> {
            try {
                ContentValues values = new ContentValues();

                values.putNull("IDFACULTY");
                values.put("COURSE", Integer.parseInt(course.getText().toString()));
                values.put("NAME", groupName.getText().toString());
                values.putNull("HEAD");

                getContentResolver().insert(GROUPS_URI, values);

                Toast.makeText(this, "Группа успешно добавлена", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ConcreteGroupActivity.class));
            } catch (Exception e ) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ConcreteGroupActivity.class));
    }
}