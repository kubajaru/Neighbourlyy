package com.example.neighbourlyy;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String phoneNum;
    private String address;
    private String password;
    private List<Pet> pets = new ArrayList<>();

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }
}
