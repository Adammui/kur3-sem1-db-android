package karbanovich.fit.bstu.contentproviderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConcreteGroupActivity extends AppCompatActivity {

    private EditText searchGroupId;
    private Button getGroup;
    private EditText groupId;
    private EditText head;
    private EditText countOfStudents;
    private Button deleteSelectedGroup;
    private Button editSelectedGroup;
    private Button addGroup;

    private int idOfFoundGroup;
    private final Uri GROUPS_URI =
            Uri.parse("content://com.karbanovich.students.authority/GROUP");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_group);

        binding();
        setListeners();
    }

    private void binding() {
        searchGroupId = findViewById(R.id.edtTxtSearchGroupId);
        getGroup = findViewById(R.id.btnGetGroup);
        groupId = findViewById(R.id.edtTxtGroupId);
        head = findViewById(R.id.edtTxtHead);
        countOfStudents = findViewById(R.id.edtTxtCountOfStudents);
        deleteSelectedGroup = findViewById(R.id.btnDeleteSelectedGroup);
        editSelectedGroup = findViewById(R.id.btnEditSelectedGroup);
        addGroup = findViewById(R.id.btnAddGroup);
        idOfFoundGroup = -1;
    }

    private void setListeners() {
        getGroup.setOnClickListener(view -> {
            try {
                String id = searchGroupId.getText().toString();
                if(id.equals("")) {
                    Toast.makeText(this, "Введите номер группы", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cursor cursor = getContentResolver().query(Uri.parse(GROUPS_URI.toString() + "/" + id),
                        null, null, null, null);

                if(!cursor.moveToFirst()) {
                    idOfFoundGroup = -1;
                    groupId.getText().clear();
                    head.getText().clear();
                    countOfStudents.getText().clear();
                    Toast.makeText(this, "Такой группы нет", Toast.LENGTH_SHORT).show();
                } else {
                    idOfFoundGroup = Integer.parseInt(id);
                    groupId.setText(id);
                    if(cursor.getString(0) == null)
                        head.setText("-");
                    else
                        head.setText(cursor.getString(0));
                    countOfStudents.setText(cursor.getString(1));
                }

                cursor.close();
            } catch (Exception e ) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });

        deleteSelectedGroup.setOnClickListener(view -> {
            if(idOfFoundGroup == -1) {
                Toast.makeText(this, "Группа не выбрана", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                getContentResolver().delete(Uri.parse(GROUPS_URI.toString() + "/" + idOfFoundGroup),
                        null, null);

                idOfFoundGroup = -1;
                searchGroupId.getText().clear();
                groupId.getText().clear();
                head.getText().clear();
                countOfStudents.getText().clear();

                Toast.makeText(this, "Группа удалена", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });

        editSelectedGroup.setOnClickListener(view -> {
            if(idOfFoundGroup == -1) {
                Toast.makeText(this, "Группа не выбрана", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                ContentValues values = new ContentValues();

                values.put("IDGROUP", Integer.parseInt(groupId.getText().toString()));
                values.put("HEAD", head.getText().toString());

                getContentResolver().update(Uri.parse(GROUPS_URI.toString() + "/" + idOfFoundGroup),
                        values, null, null);

                Toast.makeText(this, "Группа успешно изменена", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
                Log.d("Error: ", e.getMessage());
            }
        });

        addGroup.setOnClickListener(view ->
            startActivity(new Intent(this, AddGroupActivity.class)));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}