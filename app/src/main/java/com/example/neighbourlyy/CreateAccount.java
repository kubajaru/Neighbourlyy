package com.example.neighbourlyy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
    }

    public void createAccount(String email, String password) {
        boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
}
