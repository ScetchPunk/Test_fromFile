package com.example.test_fromfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class test_result extends AppCompatActivity {

    TextView textReceived;
    TextView txtResultCount;
    TextView txtResultMark;
    String text;
    RadioButton radioButton;
    Integer mark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        //text = getIntent().getExtras().getString("text");
        //mark = getIntent().getExtras().getInt("mark");

        textReceived = (TextView) findViewById(R.id.txtFullNameResult);
        //textReceived.setText(text);

        txtResultCount= (TextView) findViewById(R.id.txtResultCount);
        //txtResultCount.setText(mark.toString());

        txtResultMark= (TextView) findViewById(R.id.txtResultMark);
        //Integer result=(mark*10/2);
        //txtResultMark.setText(result.toString());
    }
    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        Toast.makeText(test_result.this,"There is no turning back!",Toast.LENGTH_LONG).show();
        return;
    }
}