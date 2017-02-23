package com.tara.tara.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sreeram Ajay on 18-02-2017.
 */

/**
 * Created by user by making order and sent to Hotel
 */
@IgnoreExtraProperties
public class HotelOrderModel {
    public String user;
    public String hotel;
    public String table;
    public String order;
    Map<String,Integer> orderedItems=new HashMap<String,Integer>();
    Boolean payment=false;
    Boolean delivered=false;
    Long timeStamp=-1L;
    double waitingTime=0;

    public HotelOrderModel(){

    }
    public HotelOrderModel(String user, String hotel, String table,String order,Map<String,Integer> orderedItems) {
        this.user = user;
        this.hotel = hotel;
        this.table = table;
        this.order = order;
        this.orderedItems=orderedItems;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Map<String, Integer> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(Map<String, Integer> orderedItems) {
        this.orderedItems = orderedItems;
    }

    public Boolean getPayment() {
        return payment;
    }

    public void setPayment(Boolean payment) {
        this.payment = payment;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public double getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(double waitingTime) {
        this.waitingTime = waitingTime;
    }
}