package com.tara.tara.model;

/**
 * Created by M1032467 on 2/17/2017.
 */

public class CategoriesModel {
    private String id;

    private String categoryName;

    private String image;

    public CategoriesModel(String id, String categoryName, String image) {
        this.id = id;
        this.categoryName = categoryName;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}