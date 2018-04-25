package com.example.admin.activitytest;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2018/4/24.
 */

public class Poidbitem2 extends DataSupport {
    private int Id;
    private String name;
    private float time_cost;
    private float money_cost;
    private String lantitude;
    private String longtitude;
    private List<Routedbitem> routedbitem1 = new ArrayList<Routedbitem>();

    public List<Routedbitem> getRoutedbitem1() {
        return routedbitem1;
    }

    public void setRoutedbitem1(List<Routedbitem> routedbitem1) {
        this.routedbitem1 = routedbitem1;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getLantitude() {
        return lantitude;
    }

    public void setLantitude(String lantitude) {
        this.lantitude = lantitude;
    }

    public float getMoney_cost() {
        return money_cost;
    }

    public void setMoney_cost(float money_cost) {
        this.money_cost = money_cost;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

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
}
