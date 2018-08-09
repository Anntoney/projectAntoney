package com.inducesmile.notification;


public class NotifyObject {

    private String title;
    private String content;
    private String date;

    public NotifyObject(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
