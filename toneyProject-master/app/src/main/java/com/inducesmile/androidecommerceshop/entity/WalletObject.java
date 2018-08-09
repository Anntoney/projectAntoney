package com.inducesmile.androidecommerceshop.entity;


public class WalletObject {

    private String amount;
    private String paypal;
    private String creditcard;
    private String pod;

    public WalletObject(String amount, String paypal, String creditcard, String pod) {
        this.amount = amount;
        this.paypal = paypal;
        this.creditcard = creditcard;
        this.pod = pod;
    }

    public String getAmount() {
        return amount;
    }

    public String getPaypal() {
        return paypal;
    }

    public String getCreditcard() {
        return creditcard;
    }

    public String getPod() {
        return pod;
    }
}
