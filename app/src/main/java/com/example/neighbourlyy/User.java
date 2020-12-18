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

    public User(int id, String fullName, String email, String phoneNum, String address, String password) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNum = phoneNum;
        this.address = address;
        this.password = password;
    }

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

    public boolean addPet(Pet pet) {
        return pets.add(pet);
    }

    public boolean removePet(Pet pet) {
        return pets.remove(pet);
    }

    public Pet getPetById(int id) {
        for (Pet pet : pets) {
            if (pet.getId() == id) {
                return pet;
            }
        }
        return null;
    }

    public boolean removePetById(int id) {
        return pets.remove(getPetById(id));
    }
}
