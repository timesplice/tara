package com.tara.tara.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by M1032467 on 2/19/2017.
 */
@IgnoreExtraProperties
public class HotelModel {
    String name;
    double ratingValue;
    int ratingCount;
    public Map<String, String> offers = new HashMap<>();

    public HotelModel() {

    }

    public HotelModel(String name, double ratingValue, int ratingCount, Map<String, String> offers) {
        this.name = name;
        this.ratingValue = ratingValue;
        this.ratingCount = ratingCount;
        this.offers = offers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Map<String, String> getOffers() {
        return offers;
    }

    public void setOffers(Map<String, String> offers) {
        this.offers = offers;
    }
}

