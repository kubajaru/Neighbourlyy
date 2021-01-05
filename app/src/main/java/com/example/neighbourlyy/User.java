package com.example.neighbourlyy;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String name;
    public String phoneNumber;
    public String postalCode;
    public String city;
    public String street;

    public User(String name, String phoneNumber, String postalCode, String city, String street) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
    }

    public User() {
    }

    protected User(Parcel in) {
        name = in.readString();
        phoneNumber = in.readString();
        postalCode = in.readString();
        city = in.readString();
        street = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(postalCode);
        dest.writeString(city);
        dest.writeString(street);
    }
}
