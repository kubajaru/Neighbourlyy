package com.example.neighbourlyy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Pet> {
    private List<Pet> pet;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Pet> objects) {
        super(context, resource, objects);
        this.pet = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);

        TextView name = listItem.findViewById(R.id.nameTV);
        name.setText(pet.get(position).name);

        TextView breed = listItem.findViewById(R.id.breedTB);
        breed.setText("Breed: " + pet.get(position).breed);

        TextView weight = listItem.findViewById(R.id.weightTV);
        weight.setText("Weight: " + pet.get(position).weight + " kg");

        TextView details = listItem.findViewById(R.id.detailsTV);
        details.setText("Details: " + pet.get(position).details);

        TextView from = listItem.findViewById(R.id.fromTV);
        from.setText("Available from: " + pet.get(position).from);

        TextView to = listItem.findViewById(R.id.toTV);
        to.setText("Available to: " + pet.get(position).to);

        TextView from2 = listItem.findViewById(R.id.fromTV2);
        from2.setText("Available from: " + pet.get(position).from2);

        TextView to2 = listItem.findViewById(R.id.toTV2);
        to2.setText("Available to: " + pet.get(position).to2);

        TextView from3 = listItem.findViewById(R.id.fromTV3);
        from3.setText("Available from: " + pet.get(position).from3);

        TextView to3 = listItem.findViewById(R.id.toTV3);
        to3.setText("Available to: " + pet.get(position).to3);
        return listItem;
    }

    /* TODO
        None.
     */
}
