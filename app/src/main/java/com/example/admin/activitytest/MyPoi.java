package com.example.admin.activitytest;

import com.amap.api.services.core.LatLonPoint;

import java.io.Serializable;

/**
 * Created by Admin on 2018/4/18.
 */

public class MyPoi implements Serializable {
    private String name;
    private double time_cost;
    private int money_cost;
    private LatLonPoint latlonp;

    public MyPoi(String name,double time_cost,int money_cost,LatLonPoint latlonp) {
        this.name = name;
        this.time_cost = time_cost;
        this.money_cost = money_cost;
        this.latlonp = latlonp;
    }
    public MyPoi()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTime_cost() {
        return time_cost;
    }

    public void setTime_cost(double time_cost) {
        this.time_cost = time_cost;
    }

    public int getMoney_cost() {
        return money_cost;
    }

    public void setMoney_cost(int money_cost) {
        this.money_cost = money_cost;
    }

    public LatLonPoint getLatlonp() {
        return latlonp;
    }

    public void setLatlonp(LatLonPoint latlonp) {
        this.latlonp = latlonp;
    }
}
