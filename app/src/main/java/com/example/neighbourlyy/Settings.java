package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Settings extends AppCompatActivity {
    private User currentUser;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent i = getIntent();
        currentUser = i.getParcelableExtra("currentUser");

        database = FirebaseDatabase.getInstance();
        EditText phoneNumberTV = findViewById(R.id.ETPhoneEdit);
        phoneNumberTV.setText(currentUser.phoneNumber);
        phoneNumberTV.addTextChangedListener(new TextValidator(phoneNumberTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText nameTV = findViewById(R.id.ETNameEdit);
        nameTV.setText(currentUser.name);
        nameTV.addTextChangedListener(new TextValidator(nameTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText postalCodeTV = findViewById(R.id.ETPostalEdit);
        postalCodeTV.setText(currentUser.postalCode);
        postalCodeTV.addTextChangedListener(new TextValidator(postalCodeTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText cityTV = findViewById(R.id.ETCityEdit);
        cityTV.setText(currentUser.city);
        cityTV.addTextChangedListener(new TextValidator(cityTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText streetTV = findViewById(R.id.ETStreetEdit);
        streetTV.setText(currentUser.street);
        streetTV.addTextChangedListener(new TextValidator(streetTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        Button confirmBtn = findViewById(R.id.settings_confirrmBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameTV.getText().toString().isEmpty() & !phoneNumberTV.getText().toString().isEmpty() & !postalCodeTV.getText().toString().isEmpty() & !cityTV.getText().toString().isEmpty() & !streetTV.getText().toString().isEmpty()) {
                    updateUser(nameTV.getText().toString(), phoneNumberTV.getText().toString(), postalCodeTV.getText().toString(), cityTV.getText().toString(), streetTV.getText().toString());
                    Intent i = new Intent(Settings.this, MainMenu.class);
                    startActivity(i);
                } else {
                    Toast.makeText(Settings.this, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateUser(String name, String phoneNumber, String postalCode, String city, String street) {
        HashMap<String, String> updatedUser = new HashMap<>();
        updatedUser.put("name", name);
        updatedUser.put("phoneNumber", phoneNumber);
        updatedUser.put("postalCode", postalCode);
        updatedUser.put("city", city);
        updatedUser.put("street", street);
        DatabaseReference ref = database.getReference();
        HashMap<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid(), updatedUser);
        ref.updateChildren(childUpdates);
    }
}