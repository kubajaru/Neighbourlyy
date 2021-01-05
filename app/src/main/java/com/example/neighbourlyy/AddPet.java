package com.example.neighbourlyy;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddPet extends AppCompatActivity {
    private static final String TAG = "AddPetActivity";
    private ArrayList<String> names = new ArrayList<>();
    private EditText fromTV;
    private EditText toTV;
    private Date from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);

        Intent i = getIntent();
        names = i.getStringArrayListExtra("names");


        EditText petNameText = findViewById(R.id.addPet_nameTV);
        petNameText.addTextChangedListener(new TextValidator(petNameText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText breedText = findViewById(R.id.addPet_breedTV);
        breedText.addTextChangedListener(new TextValidator(breedText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText weightText = findViewById(R.id.addPet_weightTV);
        weightText.addTextChangedListener(new TextValidator(weightText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText FromText = findViewById(R.id.addPet_fromTV);
        FromText.addTextChangedListener(new TextValidator(FromText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText toText = findViewById(R.id.addPet_toTV);
        toText.addTextChangedListener(new TextValidator(toText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText detailsText = findViewById(R.id.addPet_detailsTV);
        detailsText.addTextChangedListener(new TextValidator(detailsText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        fromTV = findViewById(R.id.addPet_fromTV);
        fromTV.setFocusable(false);

        toTV = findViewById(R.id.addPet_toTV);
        toTV.setFocusable(false);

        Button saveBtn = findViewById(R.id.addPet_saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!petNameText.getText().toString().isEmpty() & !breedText.getText().toString().isEmpty() & !weightText.getText().toString().isEmpty() & !detailsText.getText().toString().isEmpty()) {
                    if (!names.contains(petNameText.getText().toString())) {
                        if (timeOverlap(fromTV.getText().toString(), toTV.getText().toString())) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("pets");
                            HashMap<String, String> newPet = new HashMap<>();
                            newPet.put("name", petNameText.getText().toString());
                            newPet.put("breed", breedText.getText().toString());
                            newPet.put("weight", weightText.getText().toString());
                            newPet.put("details", detailsText.getText().toString());
                            newPet.put("from", fromTV.getText().toString());
                            newPet.put("to", toTV.getText().toString());
                            newPet.put("owner", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            myRef.push().setValue(newPet);
                            Intent i = new Intent(AddPet.this, PetsList.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                            Toast.makeText(AddPet.this, getString(R.string.incorrect_time), Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "User provided overlapping time interval");
                        }
                    } else {
                        Toast.makeText(AddPet.this, getString(R.string.incorrect_name), Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "User provided name which already exists");
                    }
                } else {
                    Toast.makeText(AddPet.this, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "User provided incorrect data");
                }
            }
        });

        ImageButton backBtn = findViewById(R.id.addPet_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddPet.this, PetsList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        /* TODO
            Check toast
         */

    }

    public void showFromTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment(fromTV);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showToTimePicker(View v) {
        DialogFragment newFragment = new TimePickerFragment(toTV);
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

    private boolean timeOverlap(String from, String to) {
        int fromHour = Integer.parseInt(from.substring(0,2));
        int fromMin = Integer.parseInt(from.substring(3,5));
        int toHour =  Integer.parseInt(to.substring(0,2));
        int toMin = Integer.parseInt(to.substring(3,5));

        if (fromHour < toHour) {
            return true;
        } else if (fromHour == toHour & fromMin < toMin) {
            return true;
        } else {
            return false;
        }
    }

}