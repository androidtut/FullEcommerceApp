package com.example.bestecommerce.models;

public class CategoriesModels {
    private int id;
    private String name,brief,image;

    public CategoriesModels(int id, String name, String brief, String image) {
        this.id = id;
        this.name = name;
        this.brief = brief;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
