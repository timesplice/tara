package com.tara.tara.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sreeram Ajay on 18-02-2017.
 */

/**
 * Created by hotel and sent to user as a response from hotel for HotelOrder Request
 */
@IgnoreExtraProperties
public class UserOrderModel {
    public String hotel;
    String table;
    String user;
    String order;
    double waitingTime=0.0;
    String chefReply;
    double bill=0.0;
    Long timeStamp=-1L;
    Boolean delivered = false;
    Boolean payment=false;

    public UserOrderModel() {
    }

    public UserOrderModel(String hotel, String table,String user,String order) {
        this.hotel = hotel;
        this.table = table;
        this.user = user;
        this.order = order;

    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Double waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getChefReply() {
        return chefReply;
    }

    public void setChefReply(String chefReply) {
        this.chefReply = chefReply;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(Double bill) {
        this.bill = bill;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }
}