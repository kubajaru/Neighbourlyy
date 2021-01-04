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
        breed.setText(pet.get(position).breed);
        TextView weight = listItem.findViewById(R.id.weightTV);
        weight.setText(pet.get(position).weight);
        TextView details = listItem.findViewById(R.id.detailsTV);
        details.setText(pet.get(position).details);

        return listItem;
    }
}
