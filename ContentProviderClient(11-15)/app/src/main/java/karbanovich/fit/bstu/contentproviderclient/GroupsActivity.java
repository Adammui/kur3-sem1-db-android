package karbanovich.fit.bstu.contentproviderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class GroupsActivity extends AppCompatActivity {

    private Button deleteAllGroups;
    private ListView groups;
    private String[] groupData;

    private final Uri GROUPS_URI =
            Uri.parse("content://com.karbanovich.students.authority/GROUP");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        binding();
    }

    private void binding() {
        deleteAllGroups = findViewById(R.id.btnDeleteAllGroups);
        groups = findViewById(R.id.listViewGroups);

        try {
            Cursor cursor = getContentResolver().query(GROUPS_URI,
                    null, null, null, null);
            groupData = new String[cursor.getCount()];
            int i = 0;

            if(cursor.getCount() == 0)
                deleteAllGroups.setEnabled(false);

            while (cursor.moveToNext()) {
                groupData[i++] = "Номер группы: " + cursor.getString(0) + "\r\n" +
                        "\t\tСтароста: " + cursor.getString(1) + "\r\n" +
                        "\t\tКол-во человек: " + cursor.getString(2);
            }
            cursor.close();

            ArrayAdapter<String> adapter = new ArrayAdapter(this,
                    android.R.layout.simple_list_item_1, groupData);
            groups.setAdapter(adapter);


            deleteAllGroups.setOnClickListener(view -> {
                getContentResolver().delete(GROUPS_URI, null, null);
                Toast.makeText(this, "Группы успешно удалены", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
            });
        } catch (Exception e) {
            Toast.makeText(this, "Неизвестная ошибка", Toast.LENGTH_SHORT).show();
            Log.d("Error: ", e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}