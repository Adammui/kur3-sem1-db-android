package ast.fit.bstu.bd7;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


///7 работа с файлами, пути
//8 потоковая запись и чтение
//9 буферы
//10 random access file
//11 сериализация- копия из бит, xml-язык описания стукруры, древовид
//12 jSON java script object notation tojson fromjson

public class MainActivity extends AppCompatActivity {
    String path;
    ArrayList<Note> notes = new ArrayList<>();
    String currentDate = "15-10-2021";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path =  "lab7.json";
        setContentView(R.layout.activity_main);
        EditText note= findViewById(R.id.draft);
        CalendarView calendar= findViewById(R.id.calendarView);

        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(MainActivity.this, "Файл был создан", Toast.LENGTH_SHORT).show();
        } else {
            notes = (ArrayList<Note>) json.importFromJSON(MainActivity.this);
            Toast.makeText(MainActivity.this, "Файл уже существует", Toast.LENGTH_SHORT).show();
        }

        Button add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.getText().length() <= 0)
                    note.setError("Введите заметку!");
                else {
                    Note text = new Note();
                    text.date = currentDate;
                    Toast.makeText(MainActivity.this, note.getText(), Toast.LENGTH_SHORT).show();
                    text.note = note.getText().toString();
                    notes = (ArrayList<Note>) json.importFromJSON(MainActivity.this);
                    if (notes == null)
                        return;
                    boolean flag = false;
                    for (Note item : notes) {
                        if (item.date.equals(currentDate)) {
                            item.note = note.getText().toString();
                            flag = true;
                        }
                    }
                    if (!flag)
                        notes.add(text);
                    json.exportToJSON(MainActivity.this, notes);
                    if (!flag)
                        Toast.makeText(MainActivity.this, "Заметка сохранена!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "Заметка изменена!", Toast.LENGTH_SHORT).show();
                    note.clearFocus();
                }

            }
        });
        Button del=findViewById(R.id.delete);
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = 0;
                        if (note.getText().length() <= 0)
                            note.setError("Нельзя удалить пустую заметку");
                        else {
                            if (notes != null) {
                                for (int i = 0; i < notes.size(); i++) {
                                    if (notes.get(i).date.equals(currentDate))
                                        position = i;
                                }
                                notes.remove(position);
                                json.exportToJSON(MainActivity.this, notes);
                                note.setText("");
                                Toast.makeText(MainActivity.this, "Заметка удалена", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        Calendar calendarInstance = Calendar.getInstance();
        calendarInstance.set(2021, 9, 15);
        calendar.setDate(calendarInstance.getTimeInMillis());
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                note.setText("");
                if(mDay==18)
                {
                    note.setText("rrr");
                }
                if(mDay==20)
                {
                    note.setText("qwer");
                }
                currentDate = new StringBuilder().append(mMonth + 1)
                        .append("-").append(mDay).append("-").append(mYear)
                        .append(" ").toString();
                boolean flag = false;
                if (notes != null) {
                    for (Note item :
                            notes) {
                        if (item.date.equals(currentDate)) {
                            note.setText(item.note);
                            flag = true;
                        }
                    }
                    if (flag == false)
                        note.setText("");
                }

            }
        });

    }

    /*
    public JSONArray readFromJsonFile(String fileName){
        JSONArray result = new JSONArray();
        JSONObject obj = new JSONObject();
        try{
            String text = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);

            JSONObject obj1 = new JSONObject(text);
            JSONArray arr = obj1.getJSONArray("Notes");

            Toast.makeText(this, arr.toString(), Toast.LENGTH_LONG).show();

            for(int i = 0; i < arr.length(); i++){
                String note = arr.getJSONObject(i).getString("note");
                String date = arr.getJSONObject(i).getString("date");
                obj.put("note", note);
                obj.put("date", date );
                result.put(obj);
            }
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }
        return result;
    }*/
}