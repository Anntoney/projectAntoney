package com.inducesmile.androidecommerceshop.entity;


public class NotificationObject {

    private String title;
    private String message;
    private String created_at;

    public NotificationObject(String title, String message, String created_at) {
        this.title = title;
        this.message = message;
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getCreated_at() {
        return created_at;
    }
}
