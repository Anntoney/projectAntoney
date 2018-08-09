package com.inducesmile.androidecommerceshop.entity;

public class MenuCategoryObject {

    private int menu_id;
    private String menu_name;
    private String menu_image;

    public MenuCategoryObject(int menu_id, String menu_name, String menu_image) {
        this.menu_id = menu_id;
        this.menu_name = menu_name;
        this.menu_image = menu_image;
    }

    public MenuCategoryObject(String menu_name, String menu_image) {
        this.menu_name = menu_name;
        this.menu_image = menu_image;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public String getMenu_image() {
        return menu_image;
    }
}
