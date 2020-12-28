package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,20}" +               //at least 8 characters
                    "$");


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        EditText emailText = findViewById(R.id.editTextTextEmailAddress);
        emailText.addTextChangedListener(new TextValidator(emailText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError("Field cannot be empty!");
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    textView.setError("Invalid email address!");
                }
            }
        });

        EditText passwordText = findViewById(R.id.editTextTextPassword);
        passwordText.addTextChangedListener(new TextValidator(passwordText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError("Field cannot be empty!");
                }
                else if (!validatePassword(text)) {
                    textView.setError("Invalid password!");
                }
            }
        });

        Button signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(emailText.getText().toString(), passwordText.getText().toString());
            }
        });

        Button logInBtn = findViewById(R.id.loginBtn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAccount.this, LogIn.class);
                startActivity(i);
            }
        });

    }

    public void createAccount(String email, String password) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() & validatePassword(password)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Intent i = new Intent(CreateAccount.this, MainMenu.class);
                        startActivity(i);
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(CreateAccount.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Some field are incorrect!", Toast.LENGTH_SHORT).show();
        }
        /* TODO
        Dodać wysłanie maila aktywacyjnego
         */
    }

    private boolean validatePassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
