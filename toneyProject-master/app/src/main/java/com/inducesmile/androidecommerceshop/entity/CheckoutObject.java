package com.inducesmile.androidecommerceshop.entity;


public class CheckoutObject {

    private String company_name;
    private String company_address;
    private int settings_tax;
    private String settings_symbol;
    private int settings_shipping;

    public CheckoutObject(String company_name, String company_address, int settings_tax, String settings_symbol, int settings_shipping) {
        this.company_name = company_name;
        this.company_address = company_address;
        this.settings_tax = settings_tax;
        this.settings_symbol = settings_symbol;
        this.settings_shipping = settings_shipping;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getCompany_address() {
        return company_address;
    }

    public int getSettings_tax() {
        return settings_tax;
    }

    public String getSettings_symbol() {
        return settings_symbol;
    }

    public int getSettings_shipping() {
        return settings_shipping;
    }
}
