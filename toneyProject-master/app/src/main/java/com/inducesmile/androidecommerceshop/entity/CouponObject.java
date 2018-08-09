package com.inducesmile.androidecommerceshop.entity;


public class CouponObject {

    private int id;
    private String coupon_name;
    private String coupon_code;
    private String coupon_type;
    private int discount;
    private String starting_date;
    private String ending_date;
    private int usage_limit;

    public CouponObject(int id, String coupon_name, String coupon_code, String coupon_type, int discount, String starting_date, String ending_date, int usage_limit) {
        this.id = id;
        this.coupon_name = coupon_name;
        this.coupon_code = coupon_code;
        this.coupon_type = coupon_type;
        this.discount = discount;
        this.starting_date = starting_date;
        this.ending_date = ending_date;
        this.usage_limit = usage_limit;
    }

    public int getId() {
        return id;
    }

    public String getCoupon_name() {
        return coupon_name;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public int getDiscount() {
        return discount;
    }

    public String getStarting_date() {
        return starting_date;
    }

    public String getEnding_date() {
        return ending_date;
    }

    public int getUsage_limit() {
        return usage_limit;
    }
}
