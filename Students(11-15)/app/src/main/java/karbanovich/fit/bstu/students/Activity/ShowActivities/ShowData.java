package karbanovich.fit.bstu.students.Activity.ShowActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import karbanovich.fit.bstu.students.Activity.ShowActivities.ShowActivity;
import karbanovich.fit.bstu.students.R;

public class ShowData extends AppCompatActivity {

    private ListView listViewShowData;
    private String[] strData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        binding();
    }

    private void binding() {
        listViewShowData = findViewById(R.id.listViewShowData);

        Intent intent = getIntent();
        strData = intent.getExtras().getStringArray("strData");

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, strData);
        listViewShowData.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ShowActivity.class));
    }
}