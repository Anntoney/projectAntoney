package com.inducesmile.androidecommerceshop.entity;


public class UserObject {

    private int id;
    private String fullname;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String status;

    public UserObject(int id, String fullname, String username, String email, String address, String phone, String status) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.status = status;
    }

    public UserObject(String username, String email, String address, String phone, String status) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }
}
