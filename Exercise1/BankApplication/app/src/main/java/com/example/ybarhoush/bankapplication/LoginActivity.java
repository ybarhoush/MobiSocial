package com.example.ybarhoush.bankapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /** Called when the user taps the Send button */
    public void login(View view) {
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }
}

