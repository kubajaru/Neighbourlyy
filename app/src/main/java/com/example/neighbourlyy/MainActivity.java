package com.example.neighbourlyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Time to launch the another activity
        int TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent i;
                if (user != null) {
                    i = new Intent(MainActivity.this, MainMenu.class);
                }
                else {
                    i = new Intent(MainActivity.this, CreateAccount.class);
                }
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }

    /* TODO
        None.
     */
}