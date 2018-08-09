package com.inducesmile.androidecommerceshop.entity;


public class DealObject {

    private String dealName;
    private String dealImage;
    private String dealPrice;
    private String dealDiscount;
    private String dealExpireDate;

    public DealObject(String dealName, String dealImage, String dealPrice, String dealDiscount, String dealExpireDate) {
        this.dealName = dealName;
        this.dealImage = dealImage;
        this.dealPrice = dealPrice;
        this.dealDiscount = dealDiscount;
        this.dealExpireDate = dealExpireDate;
    }

    public String getDealName() {
        return dealName;
    }

    public String getDealImage() {
        return dealImage;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public String getDealDiscount() {
        return dealDiscount;
    }

    public String getDealExpireDate() {
        return dealExpireDate;
    }
}
