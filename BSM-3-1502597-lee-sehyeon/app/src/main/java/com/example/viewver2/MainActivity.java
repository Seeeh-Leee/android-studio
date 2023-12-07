package com.example.viewver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    EditText password;
    private Button button;
    public static String correct_password ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.enter_button);
        password = findViewById(R.id.password_input);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correct_password = SubActivity.new_password; //1234 불러옴
                String input_password = password.getText().toString();

                //validate password
                if (TextUtils.isEmpty(input_password)) {
                    Toast.makeText(MainActivity.this, "Empty input", Toast.LENGTH_LONG).show();
                }
                else if (input_password.equals(correct_password)) {
                        Toast.makeText(MainActivity.this, "Correct password", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, SubActivity.class);
                        String text = password.getText().toString();
                        intent.putExtra("password", text);
                        startActivity(intent);

                }
                else
                    Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_LONG).show();

            }
        });
    }

}