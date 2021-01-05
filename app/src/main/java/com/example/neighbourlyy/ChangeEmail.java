package com.example.neighbourlyy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChangeEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}