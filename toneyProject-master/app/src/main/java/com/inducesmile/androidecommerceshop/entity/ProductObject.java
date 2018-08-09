package com.inducesmile.androidecommerceshop.entity;


public class ProductObject {

    private int id;
    private int categories_id;
    private String name;
    private String description;
    private String image;
    private String color;
    private int size;
    private int price;
    private int quantity;
    private String status;
    private String created_at;

    private int favorite;

    private String coupon_type;
    private int discount;
    private String ending_date;

    public ProductObject(int id, int categories_id, String name, String description, String image, String color, int size,
                         int price, int quantity, String status, String created_at, String coupon_type, int discount, String ending_date, int favorite) {
        this.id = id;
        this.categories_id = categories_id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.color = color;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.created_at = created_at;
        this.coupon_type = coupon_type;
        this.discount = discount;
        this.ending_date = ending_date;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public int getCategories_id() {
        return categories_id;
    }

    public String getNames() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImages() {
        return image;
    }

    public String getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public int getDiscount() {
        return discount;
    }

    public String getEnding_date() {
        return ending_date;
    }

    public int getFavorite() {
        return favorite;
    }
}
