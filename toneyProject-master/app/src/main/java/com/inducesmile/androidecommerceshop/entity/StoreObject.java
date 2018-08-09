package com.inducesmile.androidecommerceshop.entity;


public class StoreObject {

    private String name;
    private String description;
    private String address;
    private String phone;
    private String email;
    private String opening_time;

    public StoreObject(String name, String description, String address, String phone, String email, String opening_time) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.opening_time = opening_time;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getOpening_time() {
        return opening_time;
    }
}
