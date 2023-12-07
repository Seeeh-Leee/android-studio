package com.example.viewver2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubActivity extends AppCompatActivity {
    public static String new_password = "1234";
    public String note_msg="origin";

    TextView textview, note_view;
    EditText password, password_again, note_enter;
    Button enter, home, save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        save = findViewById(R.id.save_note);
        note_enter = findViewById(R.id.note_enter);
        note_view = findViewById(R.id.note_view);
        textview = findViewById(R.id.checkbox);

//      Decryption process
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

        String saved_note = sharedPreferences.getString("note", "");

//        init setting
        note_view.setText(saved_note);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String pw = bundle.getString("password");
        textview.setText("you entered " + pw);

        password = findViewById(R.id.change_new);
        password_again = findViewById(R.id.change_again);

//      changing password
        enter = findViewById(R.id.change_enter_button);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password_change = password.getText().toString();
                String password_change_ver = password_again.getText().toString();

                //validate password
                if (TextUtils.isEmpty(password_change) || TextUtils.isEmpty(password_change_ver)) {
                    Toast.makeText(SubActivity.this, "Empty input", Toast.LENGTH_LONG).show();
                } else if (password_change.equals(password_change_ver)) {
                    // wrong password
                    if(!Password_Validation(password_change)){
                        Toast.makeText(SubActivity.this, "Check the alert", Toast.LENGTH_LONG).show();
                        textview.setText("<!-- alert-->\n*The password must be at least 8 characters long" +
                                " and contain capital letters, lowercase letters, " +
                                "special characters, and numbers*");
                    }
                    else {
                        Toast.makeText(SubActivity.this, "Password has changed", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SubActivity.this, MainActivity.class);
                        new_password = password_change;
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(SubActivity.this, "Password didn't match", Toast.LENGTH_LONG).show();
            }
        });

//      move to Home screen
        home = findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//      saving the note
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note_view.setText(note_enter.getText());

//        Encryption process
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

                SharedPreferences.Editor spfEditor = sharedPreferences.edit();

                spfEditor.putString("note", note_enter.getText().toString());
                spfEditor.commit();
            }
        });
    }

    public static boolean Password_Validation(String password)
    {
        if(password.length()>=8)
        {
            Pattern sLetter = Pattern.compile("[a-z]");
            Pattern bLetter = Pattern.compile("[A-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

            Matcher hassLetter = sLetter.matcher(password);
            Matcher hasbLetter = bLetter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hassLetter.find() && hasbLetter.find() && hasDigit.find() && hasSpecial.find();
        }
        else
            return false;
    }
}
