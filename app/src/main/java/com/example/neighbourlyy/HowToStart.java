package com.example.neighbourlyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HowToStart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_start);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ImageButton howBtn = findViewById(R.id.howToStart_backBtn);
        howBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HowToStart.this, MainMenu.class);
                startActivity(i);
            }
        });
    }
}