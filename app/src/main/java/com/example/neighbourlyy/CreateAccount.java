package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {
    private static final String TAG = "CreateAccountActivity";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[@#$%^+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,20}" +             //at least 8 characters
                    "$");

    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^\\d{2}[- ]{0,1}\\d{3}$");

    private FirebaseAuth mAuth;
    Geocoder coder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        coder = new Geocoder(this);
        EditText emailText = findViewById(R.id.editTextTextEmailAddress);
        emailText.addTextChangedListener(new TextValidator(emailText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyString));
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    textView.setError(getString(R.string.invalidEmail));
                }
            }
        });

        EditText passwordText = findViewById(R.id.editTextTextPassword);
        passwordText.addTextChangedListener(new TextValidator(passwordText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
                else if (!PASSWORD_PATTERN.matcher(text).matches()) {
                    textView.setError(getString(R.string.invalidPassword));
                }
            }
        });

        EditText nameText = findViewById(R.id.editTextTextPersonName);
        nameText.addTextChangedListener(new TextValidator(nameText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText phoneNumberText = findViewById(R.id.editTextPhone);
        phoneNumberText.addTextChangedListener(new TextValidator(phoneNumberText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText postalCodeText = findViewById(R.id.PostalCodeTV);
        postalCodeText.addTextChangedListener(new TextValidator(postalCodeText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                } else if (!POSTAL_CODE_PATTERN.matcher(text).matches()) {
                    textView.setError(getString(R.string.invalidPostalCode));
                }
            }
        });

        EditText cityText = findViewById(R.id.CityTV);
        cityText.addTextChangedListener(new TextValidator(cityText) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        EditText streetAndNumber = findViewById(R.id.StreetNumberTV);
        streetAndNumber.addTextChangedListener(new TextValidator(streetAndNumber) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) {
                    textView.setError(getString(R.string.noEmptyField));
                }
            }
        });

        Button signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(emailText.getText().toString(), passwordText.getText().toString(), nameText.getText().toString(), phoneNumberText.getText().toString(), createAddressString(postalCodeText.getText().toString(), cityText.getText().toString(), streetAndNumber.getText().toString()));
            }
        });

        Button logInBtn = findViewById(R.id.loginBtn);
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateAccount.this, LogIn.class);
                startActivity(i);
            }
        });
    }

    public void createAccount(String email, String password, String name, String phoneNumber, String address) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() & PASSWORD_PATTERN.matcher(password).matches()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();
                        newUser.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.i(TAG, "Users display name updated correctly.");
                                        }
                                        else {
                                            Log.e(TAG, "Updating user display name failed");
                                        }
                                    }
                                });
                        newUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("users");
                                    myRef.child(newUser.getUid()).child("Name").setValue(newUser.getDisplayName());
                                    myRef.child(newUser.getUid()).child("Phone number").setValue(phoneNumber);
                                    myRef.child(newUser.getUid()).child("Address").setValue(address);
                                    try {
                                        List<Address> addressList = coder.getFromLocationName(address, 2);
                                        if (addressList != null) {
                                            myRef.child(newUser.getUid()).child("Longitude").setValue(addressList.get(0).getLongitude());
                                            myRef.child(newUser.getUid()).child("Latitude").setValue(addressList.get(0).getLatitude());
                                        } else {
                                            Toast.makeText(CreateAccount.this, getString(R.string.addressCodingFailed), Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "Address not found. List of addresses is null");
                                        }
                                    }
                                    catch (IOException ex){
                                        Toast.makeText(CreateAccount.this, getString(R.string.coderFail), Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Unexpected error occured during coding address into coordinates.", ex);
                                    }
                                    Intent i = new Intent(CreateAccount.this, MainMenu.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(CreateAccount.this, getString(R.string.sendingEmailFailed), Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "Sending email to new user failed");
                                }
                            }
                        });
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(CreateAccount.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "createUserWithPasswordAndEmail failed.");
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Some field are incorrect!", Toast.LENGTH_SHORT).show();
        }
    }

    public String createAddressString(String postalCode, String city, String streetAndNumber) {
        return "" + streetAndNumber + ", " + postalCode + " " + city;
    }
}
