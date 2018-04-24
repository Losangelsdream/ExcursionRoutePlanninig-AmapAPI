package com.example.admin.activitytest;

import com.amap.api.services.core.LatLonPoint;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2018/4/24.
 */

public class Poidbitem extends DataSupport {
    private String name;
    private float time_cost;
    private float money_cost;
    private String lantitude;
    private String longtitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTime_cost() {
        return time_cost;
    }

    public void setTime_cost(float time_cost) {
        this.time_cost = time_cost;
    }

    public float getMoney_cost() {
        return money_cost;
    }

    public void setMoney_cost(float money_cost) {
        this.money_cost = money_cost;
    }


    public String getLantitude() {
        return lantitude;
    }

    public void setLantitude(String lantitude) {
        this.lantitude = lantitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }
}

