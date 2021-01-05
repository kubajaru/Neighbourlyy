package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets_list);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pets");
        pets = new ArrayList<Pet>();
        names = new ArrayList<String>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pets.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Pet pet = postSnapshot.getValue(Pet.class);
                    if (pet.owner.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        pets.add(pet);
                        names.add(pet.name);
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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PetsList.this, UpdatePet.class);
                i.putExtra("chosenPet", pets.get(position));
                startActivity(i);
            }
        });


        Button addPetBtn = findViewById(R.id.AddPetBtn);
        addPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PetsList.this, AddPet.class);
                i.putExtra("names", names);
                startActivity(i);
            }
        });

        ImageButton backBtn = findViewById(R.id.petsList_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PetsList.this, MainMenu.class);
                startActivity(i);
            }
        });

    }

    /* TODO
        None
     */
}