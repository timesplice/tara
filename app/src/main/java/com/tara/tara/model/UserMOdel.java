package com.tara.tara.model;

/**
 * Created by M1032467 on 2/19/2017.
 */

public class UserModel {
    String name;
    String email;
    String userId;

    public UserModel() {

    }

    public UserModel(String name, String email, String userId) {
        this.name = name;
        this.email = email;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
