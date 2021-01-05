package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class UpdatePet extends AppCompatActivity {
    private static final String TAG = "UpdatePetActivity";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet);

        Intent i = getIntent();
        Pet chosenPet = (Pet)i.getParcelableExtra("chosenPet");

        EditText nameTV = findViewById(R.id.updatePet_nameTV);
        nameTV.setText(chosenPet.name);
        nameTV.addTextChangedListener(new TextValidator(nameTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyString));
                }
            }
        });

        EditText breedTV = findViewById(R.id.updatePet_breedTV);
        breedTV.setText(chosenPet.breed);
        breedTV.addTextChangedListener(new TextValidator(breedTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyString));
                }
            }
        });

        EditText weightTV = findViewById(R.id.updatePet_weightTV);
        weightTV.setText(chosenPet.weight);
        weightTV.addTextChangedListener(new TextValidator(weightTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyString));
                }
            }
        });

        EditText detailsTV = findViewById(R.id.updatePet_detailsTV);
        detailsTV.setText(chosenPet.details);
        detailsTV.addTextChangedListener(new TextValidator(detailsTV) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyString));
                }
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference idRef = database.getReference("pets");
        idRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot: snapshot.getChildren()) {
                    Pet pet = postSnapShot.getValue(Pet.class);
                    if (pet.owner.equals(chosenPet.owner) & pet.name.equals(chosenPet.name)){
                       id = postSnapShot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button deleteBtn = findViewById(R.id.updatePet_deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference deleteRef = database.getReference("pets/"+id);
                deleteRef.removeValue();

                Intent i = new Intent(UpdatePet.this, PetsList.class);
                startActivity(i);
            }
        });

        Button updateBtn = findViewById(R.id.updatePet_updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameTV.getText().toString().isEmpty() & !breedTV.getText().toString().isEmpty() & !weightTV.getText().toString().isEmpty() & !detailsTV.getText().toString().isEmpty()) {
                    HashMap<String, String> updatedPet = new HashMap<>();
                    updatedPet.put("name", nameTV.getText().toString());
                    updatedPet.put("breed", breedTV.getText().toString());
                    updatedPet.put("weight", weightTV.getText().toString());
                    updatedPet.put("details", detailsTV.getText().toString());
                    updatedPet.put("owner", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    HashMap<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("pets/"+id, updatedPet);

                    DatabaseReference ref = database.getReference();
                    ref.updateChildren(childUpdates);

                    Intent i = new Intent(UpdatePet.this, PetsList.class);
                    startActivity(i);
                } else {
                    Toast.makeText(UpdatePet.this, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "User provided incorrect data");
                }
            }
        });
    }
}