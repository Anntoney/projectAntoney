package com.inducesmile.androidecommerceshop.utils;


public class EventMessage {

    private String subtotal;
    private String itemCount;

    public EventMessage(String subtotal, String itemCount) {
        this.subtotal = subtotal;
        this.itemCount = itemCount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
}
