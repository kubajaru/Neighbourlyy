package com.example.neighbourlyy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddPet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        EditText petNameText = findViewById(R.id.PetNameTV);
        petNameText.addTextChangedListener(new TextValidator(petNameText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText breedText = findViewById(R.id.PetBreedTV);
        breedText.addTextChangedListener(new TextValidator(breedText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText weightText = findViewById(R.id.PetWeightTV);
        weightText.addTextChangedListener(new TextValidator(weightText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText FromText = findViewById(R.id.fromTV);
        FromText.addTextChangedListener(new TextValidator(FromText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText toText = findViewById(R.id.ToTV);
        toText.addTextChangedListener(new TextValidator(toText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText detailsText = findViewById(R.id.PetDetailsTV);
        detailsText.addTextChangedListener(new TextValidator(detailsText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        Button saveBtn = findViewById(R.id.SaveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!petNameText.getText().toString().isEmpty() | !breedText.getText().toString().isEmpty() | !weightText.getText().toString().isEmpty() | !detailsText.getText().toString().isEmpty()) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("pets");
                    HashMap<String, String> newPet = new HashMap<>();
                    newPet.put("name", petNameText.getText().toString());
                    newPet.put("breed", breedText.getText().toString());
                    newPet.put("weight", weightText.getText().toString());
                    newPet.put("details", detailsText.getText().toString());
                    newPet.put("owner", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    myRef.push().setValue(newPet);
                    Intent i = new Intent(AddPet.this, PetsList.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Toast.makeText(AddPet.this, "Provide all the information.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}