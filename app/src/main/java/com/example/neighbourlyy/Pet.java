package com.example.neighbourlyy;

import java.util.Date;
import java.math.BigDecimal;

public class Pet {
    private int id;
    private String name;
    private String breed;
    private BigDecimal weight;
    private Date availableFrom;
    private Date availableTo;
    private String details;

    public Pet(int id, String name, String breed, BigDecimal weight, Date availableFrom, Date availableTo, String details) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.weight = weight;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }

    public void setAvailableTo(Date availableTo) {
        this.availableTo = availableTo;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public Date getAvailableTo() {
        return availableTo;
    }

    public String getDetails() {
        return details;
    }
}
