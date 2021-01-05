package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmail extends AppCompatActivity {
    private static final String TAG = "ChangeEmailActivity";
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        user = FirebaseAuth.getInstance().getCurrentUser();

        EditText oldEmail = findViewById(R.id.ETCurrentEmail);
        oldEmail.addTextChangedListener(new TextValidator(oldEmail) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText newEmail = findViewById(R.id.ETEmailNew);
        newEmail.addTextChangedListener(new TextValidator(newEmail) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText password = findViewById(R.id.editTextTextPassword3);
        password.addTextChangedListener(new TextValidator(password) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        Button confirm = findViewById(R.id.signInBtn2);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!oldEmail.getText().toString().isEmpty() & !newEmail.getText().toString().isEmpty() & !password.getText().toString().isEmpty()) {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password.getText().toString());
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (user.getEmail().equals(oldEmail.getText().toString())) {
                                    user.updateEmail(newEmail.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User email address updated.");
                                                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Intent i = new Intent(ChangeEmail.this, MainMenu.class);
                                                                    startActivity(i);
                                                                } else {
                                                                    Toast.makeText(ChangeEmail.this, "Email is not valid", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(ChangeEmail.this, "Something went wrong during update", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(ChangeEmail.this, "Old email is incorrect", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ChangeEmail.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(ChangeEmail.this, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                }
            }

        });

        ImageButton backBtn = findViewById(R.id.email_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangeEmail.this, MainMenu.class);
                startActivity(i);
            }
        });
    }
}