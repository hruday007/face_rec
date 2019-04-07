package com.example.attendance;


import android.content.Intent;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity  {

    private EditText usernameField;
    private EditText passwordField;
    private String USERNAME = "admin";
    private String PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);

        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String enteredUsername = usernameField.getText().toString().trim();
                final String enteredPassword = passwordField.getText().toString().trim();

                if (enteredUsername.length() == 0 || enteredPassword.length() == 0){
                    Toast.makeText(LoginActivity.this, "Enter username and password", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (enteredUsername.equals(USERNAME) && enteredPassword.equals(PASSWORD))
                        openActivity();

                    else {
                        Toast.makeText(LoginActivity.this, "Your username or password is incorrrect", Toast.LENGTH_SHORT).show();
                        Log.d("WTFTag", "enteredPassword:" + enteredPassword + "||" + "enteredUsernaem:" + enteredUsername);
                    }

                }
            }
        });
    }

    private void openActivity() {
       Intent intent = new Intent(this, MainActivity.class);
       startActivity(intent);
    }

}