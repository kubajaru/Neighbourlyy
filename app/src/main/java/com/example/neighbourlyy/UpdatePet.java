package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class UpdatePet extends AppCompatActivity {
    private static final String TAG = "UpdatePetActivity";
    private String id;
    private EditText fromTV;
    private EditText toTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Intent i = getIntent();
        Pet chosenPet = (Pet) i.getParcelableExtra("chosenPet");

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
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    Pet pet = postSnapShot.getValue(Pet.class);
                    if (pet.owner.equals(chosenPet.owner) & pet.name.equals(chosenPet.name)) {
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
                DatabaseReference deleteRef = database.getReference("pets/" + id);
                deleteRef.removeValue();

                Intent i = new Intent(UpdatePet.this, PetsList.class);
                startActivity(i);
            }
        });

        fromTV = findViewById(R.id.updatePet_fromTV);
        fromTV.setFocusable(false);
        fromTV.setText(chosenPet.from);

        toTV = findViewById(R.id.updatePet_toTV);
        toTV.setFocusable(false);
        toTV.setText(chosenPet.to);

        Button updateBtn = findViewById(R.id.updatePet_updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameTV.getText().toString().isEmpty() & !breedTV.getText().toString().isEmpty() & !weightTV.getText().toString().isEmpty() & !detailsTV.getText().toString().isEmpty()) {
                    if (timeOverlap(fromTV.getText().toString() ,toTV.getText().toString())) {
                        HashMap<String, String> updatedPet = new HashMap<>();
                        updatedPet.put("name", nameTV.getText().toString());
                        updatedPet.put("breed", breedTV.getText().toString());
                        updatedPet.put("weight", weightTV.getText().toString());
                        updatedPet.put("details", detailsTV.getText().toString());
                        updatedPet.put("owner", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        updatedPet.put("from", fromTV.getText().toString());
                        updatedPet.put("to",toTV.getText().toString());
                        HashMap<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put("pets/" + id, updatedPet);

                        DatabaseReference ref = database.getReference();
                        ref.updateChildren(childUpdates);

                        Intent i = new Intent(UpdatePet.this, PetsList.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(UpdatePet.this, getString(R.string.incorrect_time), Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "User provided incorrect time interval");
                    }
                } else {
                    Toast.makeText(UpdatePet.this, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "User provided incorrect data");
                }
            }
        });

        ImageButton backBtn = findViewById(R.id.updatePet_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UpdatePet.this, PetsList.class);
                startActivity(i);
            }
        });
    }

    public void showFromTimePicker2 (View v){
        DialogFragment newFragment = new AddPet.TimePickerFragment(fromTV);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showToTimePicker2 (View v){
        DialogFragment newFragment = new AddPet.TimePickerFragment(toTV);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        private EditText text;

        public TimePickerFragment(EditText v) {
            this.text = v;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            String time;
            if (hourOfDay < 10 & minute < 10) {
                time = "0" + hourOfDay + ":0" + minute;
            } else if (hourOfDay > 10 & minute < 10) {
                time = "" + hourOfDay + ":0" + minute;
            } else if (hourOfDay < 10 & minute > 10) {
                time = "0" + hourOfDay + ":" + minute;
            } else {
                time = "" + hourOfDay + ":" + minute;
            }
            text.setText(time);
        }
    }

    private boolean timeOverlap (String from, String to) {
        int fromHour = Integer.parseInt(from.substring(0, 2));
        int fromMin = Integer.parseInt(from.substring(3, 5));
        int toHour = Integer.parseInt(to.substring(0, 2));
        int toMin = Integer.parseInt(to.substring(3, 5));

        if (fromHour < toHour) {
            return true;
        } else if (fromHour == toHour & fromMin < toMin) {
            return true;
        } else {
            return false;
        }
    }
}