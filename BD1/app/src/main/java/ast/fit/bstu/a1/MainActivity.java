package ast.fit.bstu.a1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //TODO сделать ввод чего-нибудь
    private int io=1;
    public void change(View view) {
        TextView i = findViewById(R.id.but);
        EditText e = findViewById(R.id.editName);
        if(io%2==0)
            i.setText("BYE "+e.getText());
        else i.setText("HELLO "+ e.getText());
        io+=1;
    }
}