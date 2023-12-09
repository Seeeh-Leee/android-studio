package com.example.viewver2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
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
                MasterKey masterkey = null;
                try {
                    masterkey = new MasterKey.Builder(getApplicationContext(), MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                            .build();
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                SharedPreferences sharedPreferences = null;
                try {
                    sharedPreferences = EncryptedSharedPreferences
                            .create(getApplicationContext(),
                                    "filename",
                                    masterkey,
                                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String correct_password = sharedPreferences.getString("password", "");

                if (correct_password.length() ==0) correct_password = "12341234";
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