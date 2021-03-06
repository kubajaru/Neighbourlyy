package com.example.neighbourlyy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.math.BigDecimal;

public class Pet implements Parcelable {
    public String name;
    public String breed;
    public String weight;
    public String details;
    public String owner;
    public String from;
    public String to;
    public String from2;
    public String to2;
    public String from3;
    public String to3;

    public Pet(String name, String breed, String weight, String details, String owner) {
        this.name = name;
        this.breed = breed;
        this.weight = weight;
        this.details = details;
        this.owner = owner;
    }

    public Pet() {
    }

    protected Pet(Parcel in) {
        name = in.readString();
        breed = in.readString();
        weight = in.readString();
        details = in.readString();
        owner = in.readString();
        from = in.readString();
        to = in.readString();
        from2 = in.readString();
        to2 = in.readString();
        from3 = in.readString();
        to3 = in.readString();
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(breed);
        dest.writeString(weight);
        dest.writeString(details);
        dest.writeString(owner);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(from2);
        dest.writeString(to2);
        dest.writeString(from3);
        dest.writeString(to3);
    }

    /* TODO
        None.
     */
}
