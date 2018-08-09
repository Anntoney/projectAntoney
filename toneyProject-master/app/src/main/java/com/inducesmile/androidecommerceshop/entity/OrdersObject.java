package com.inducesmile.androidecommerceshop.entity;

public class OrdersObject {

    private int id;
    private int user_id;
    private String payment_method;
    private String status;
    private String created_at;

    public OrdersObject(int id, int user_id, String payment_method, String status, String created_at) {
        this.id = id;
        this.user_id = user_id;
        this.payment_method = payment_method;
        this.status = status;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }
}
