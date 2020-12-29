package com.example.neighbourlyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static int TIME_OUT = 2000; //Time to launch the another activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent i = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(MainActivity.this, CreateAccount.class);
                    startActivity(i);
                    finish();
                }
            }
        }, TIME_OUT);
    }
}