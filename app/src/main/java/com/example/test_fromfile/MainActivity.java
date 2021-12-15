package com.example.test_fromfile;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editTextFirstName;
    EditText editTextLastName;
    TextView txtTestName;
    Button btnStart;

    public static final String mPath = "угол.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //связываю поля с переменными в файле
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        btnStart = (Button) findViewById(R.id.btnStart);
        txtTestName= (TextView) findViewById(R.id.txtTestName);
        //todo пересмотреть как будет меняться название теста
        txtTestName.setText(mPath.substring(0,this.mPath.length()-4));

        // Todo додумать код, чтобы мне не нужно было запрашивать всю activity с мейна
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // здесь я просто передам все данные, что у меня будут на руках. это
                // Это фамилия, имя.
                Intent intent = new Intent(MainActivity.this, Test_section.class);
                intent.putExtra("first_name",editTextFirstName.getText().toString());
                intent.putExtra("last_name",editTextLastName.getText().toString());
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        Toast.makeText(MainActivity.this,"There is no turning back!",Toast.LENGTH_LONG).show();
        return;

    }
}