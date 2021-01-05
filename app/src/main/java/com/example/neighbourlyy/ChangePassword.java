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

import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {
    private static final String TAG = "ChangePasswordActivity";
    private FirebaseUser user;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[@#$%^+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,20}" +             //at least 8 characters
                    "$");
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent i = getIntent();
        currentUser = i.getParcelableExtra("name");

        user = FirebaseAuth.getInstance().getCurrentUser();

        EditText oldPassword = findViewById(R.id.ETCurPas);
        oldPassword.addTextChangedListener(new TextValidator(oldPassword) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText newPassword = findViewById(R.id.ETNewPas);
        newPassword.addTextChangedListener(new TextValidator(newPassword) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                } else if (!PASSWORD_PATTERN.matcher(text).matches()) {
                    textView.setError(getString(R.string.invalidPassword));
                }
            }
        });

        EditText secondNewPassword = findViewById(R.id.ETRepPas);
        secondNewPassword.addTextChangedListener(new TextValidator(secondNewPassword) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                } else if (!PASSWORD_PATTERN.matcher(text).matches()){
                    textView.setError(getString(R.string.invalidPassword));
                }
            }
        });

        Button confirm = findViewById(R.id.signInBtn6);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword.getText().toString());
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (newPassword.getText().toString().equals(secondNewPassword.getText().toString())) {
                                if (PASSWORD_PATTERN.matcher(newPassword.getText().toString()).matches() & PASSWORD_PATTERN.matcher(secondNewPassword.getText().toString()).matches()) {
                                    user.updatePassword(newPassword.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User password updated.");
                                                        Intent i = new Intent(ChangePassword.this, MainMenu.class);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(ChangePassword.this, "Something went wrong. Try again", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(ChangePassword.this, "Password not strong enough", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(ChangePassword.this, "Password do not match", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ChangePassword.this, "Old password is incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        ImageButton backBtn = findViewById(R.id.settings_backBtn2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChangePassword.this, MainMenu.class);
                startActivity(i);
            }
        });
    }
}