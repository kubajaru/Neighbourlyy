package com.example.neighbourlyy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Map extends AppCompatActivity {
    private List<Pet> pets;
    private CustomAdapter adapter;
    private ListView list;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean permission;
    private double latitude = 0.0;
    private double longitude = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        ImageButton backBtn = findViewById(R.id.map_BackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Map.this, MainMenu.class);
                startActivity(i);
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            permission = true;

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                //latitude = location.getLatitude();
                                //longitude = location.getLongitude();
                            }
                        }
                    });
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissions( new String[] { Manifest.permission.ACCESS_FINE_LOCATION}, 666);
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("pets");
        DatabaseReference myRef = database.getReference();
        pets = new ArrayList<Pet>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pets.clear();
                for (DataSnapshot postSnapshot: snapshot.child("pets").getChildren()) {
                    Pet pet = postSnapshot.getValue(Pet.class);
                    if (!pet.owner.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        if (permission) {
                            pets.add(pet);
                            User user = snapshot.child("users/" + pet.owner).getValue(User.class);
                            String address = "" + user.street + ", " + user.postalCode + " " + user.city;

                            //locationAddress.getAddressFromLocation(address, getApplicationContext(), new GeoCoderHandler());

                            Thread fred = new Thread() {
                                @Override
                                public void run() {
                                    Geocoder geocoder = new Geocoder(getApplicationContext());
                                    String result = null;
                                    try {
                                        List addressList = geocoder.getFromLocationName(address, 1);
                                        if (addressList != null && addressList.size() > 0) {
                                            Address address = (Address)addressList.get(0);
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(address.getLatitude()).append(":");
                                            sb.append(address.getLongitude());
                                            result = sb.toString();
                                        }
                                    } catch (IOException e) {

                                    } finally {
                                        if (result != null) {

                                            String token[] = result.split(":");
                                            latitude = Double.parseDouble(token[0]);
                                            longitude = Double.parseDouble(token[1]);
                                        }
                                    }
                                }
                            };
                            fred.start();

                            try {
                                fred.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            System.out.println(longitude + ":" + latitude);

                        } else {
                            Toast.makeText(Map.this, "Random list", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list = findViewById(R.id.listViewAll);
        adapter = new CustomAdapter(this, 0, pets);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Map.this, Contact.class);
                i.putExtra("chosenPet", pets.get(position));
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 666:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    Intent i = new Intent(Map.this, Map.class);
                    startActivity(i);
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    permission = false;
                    Toast.makeText(Map.this, "Without this permission you will see whole database of pets without filtering.", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}

