package com.example.neighbourlyy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainMenu extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView header = findViewById(R.id.mainMenu_greetingTV);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        header.setText(String.format(getString(R.string.greeting), user.getDisplayName()));

        Button logOutBtn = findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainMenu.this, CreateAccount.class);
                startActivity(i);
                finish();
            }
        });

        Button mapBtn = findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, Map.class);
                startActivity(i);
            }
        });

        Button howBtn = findViewById(R.id.mainMenu_howBtn);
        howBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, HowToStart.class);
                startActivity(i);
            }
        });

        Button petBtn = findViewById(R.id.PetBtn);
        petBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, PetsList.class);
                startActivity(i);
            }
        });
    }

    /* TODO
        Add settings for password management.
        Add "How to" activity.
     */
}