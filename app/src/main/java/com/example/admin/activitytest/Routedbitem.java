package com.example.admin.activitytest;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2018/4/24.
 */

public class Routedbitem extends DataSupport {
    private int ID;
    private float waytime;
    private int waycost;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getWaycost() {
        return waycost;
    }

    public void setWaycost(int waycost) {
        this.waycost = waycost;
    }

    public float getWaytime() {
        return waytime;
    }

    public void setWaytime(float waytime) {
        this.waytime = waytime;
    }
}
