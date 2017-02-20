package com.tara.tara.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by M1032467 on 2/18/2017.
 */

public class FoodMenuModel  implements Serializable {
    String foodId;
    String name;
    String shortDesc;
    String desc;
    double price;
    String category;
    int inStockCount;
    String imageUrl;
    public int starCount = 0;
    public int avgStars = 0;
    /*star rating range from 0 to 10*/
    public Map<String, Integer> stars = new HashMap<>();

    public FoodMenuModel() {

    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public FoodMenuModel(String foodId, String name, String shortDesc, String desc, double price, String category, String imageUrl) {
        this.foodId = foodId;

        this.name = name;
        this.shortDesc = shortDesc;
        this.desc = desc;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        inStockCount = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getInStockCount() {
        return inStockCount;
    }

    public void setInStockCount(int inStockCount) {
        this.inStockCount = inStockCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public int getAvgStars() {
        return avgStars;
    }

    public void setAvgStars(int avgStars) {
        this.avgStars = avgStars;
    }

    public Map<String, Integer> getStars() {
        return stars;
    }

    public void setStars(Map<String, Integer> stars) {
        this.stars = stars;
    }
}
