package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Contact extends AppCompatActivity {
    private static final String TAG = "ContactActivity";
    private String phoneNumber;
    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent i = getIntent();
        Pet chosenPet = (Pet)i.getParcelableExtra("chosenPet");

        TextView nameTV = findViewById(R.id.help_nameTV);
        nameTV.setText(chosenPet.name);

        TextView breedTV = findViewById(R.id.help_breedTV);
        breedTV.setText(chosenPet.breed);

        TextView weightTV = findViewById(R.id.help_weightTV);
        weightTV.setText(chosenPet.weight);

        TextView detailsTV = findViewById(R.id.help_detailsTV);
        detailsTV.setText(chosenPet.details);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference phoneRef = database.getReference("users/" + chosenPet.owner + "/Phone number");
        phoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phoneNumber = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Data read of phone number cancelled: " + error);

            }
        });

        DatabaseReference nameRef = database.getReference("users/" + chosenPet.owner + "/Name");
        nameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullName = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Data read of full name cancelled: " + error);
            }
        });

        Button helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(phoneNumber);
                if (!phoneNumber.isEmpty()) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:" + phoneNumber));
                smsIntent.putExtra("sms_body", String.format(getString(R.string.smsContent), fullName, FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                if (smsIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(smsIntent);
                } else {
                    Log.e(TAG, "SMS activity not opened.");
                }
            }
            }
        });

    }
}