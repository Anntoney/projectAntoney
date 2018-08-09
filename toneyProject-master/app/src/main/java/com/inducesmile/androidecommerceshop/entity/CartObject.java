package com.inducesmile.androidecommerceshop.entity;


public class CartObject {

    private int id;
    private String name;
    private String image;
    private float price;
    private int quantity;
    private int discount;
    private String couponType;

    public CartObject(int id, String name, String image, float price, int quantity) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public CartObject(int id, String name, String image, float price, int quantity, int discount, String couponType) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.couponType = couponType;
    }

    public int getDiscount() {
        return discount;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
