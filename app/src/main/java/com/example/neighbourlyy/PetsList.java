package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetsList extends AppCompatActivity {
    List<Pet> pets;
    ListView list;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets_list);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pets");
        pets = new ArrayList<Pet>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pets.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Pet pet = postSnapshot.getValue(Pet.class);
                    if (pet.owner.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        pets.add(pet);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list = findViewById(R.id.listView);
        adapter = new CustomAdapter(this, 0, pets);
        System.out.println(pets.size() + "here");
        list.setAdapter(adapter);


        Button addPetBtn = findViewById(R.id.AddPetBtn);
        addPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PetsList.this, AddPet.class);
                startActivity(i);
            }
        });
    }

    /* TODO
        Add clicable list elements.
     */
}