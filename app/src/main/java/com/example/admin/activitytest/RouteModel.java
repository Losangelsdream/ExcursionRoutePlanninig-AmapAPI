package com.example.admin.activitytest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2018/3/29.
 */

public class RouteModel {
    private int ID;
    private double totalTime;
    private int totalCost;
    private List<String> scenicSpot = new ArrayList<>();

    private List<Double> RoutePartTime=new ArrayList<>();
    private List<Integer> RoutePartCost = new ArrayList<>();
    private List<Integer> RoutePartCategory = new ArrayList<>();



    public RouteModel(int id,double totalTime,int totalCost,List<String> scenicSpot)
    {
       this.ID=id;
       this.totalTime=totalTime;
       this.totalCost=totalCost;
       this.scenicSpot = scenicSpot;
//       this.RoutePartTime=RoutePartTime;
//       this.RoutePartCost=RoutePartCost;
//       this.RoutePartCategory=RoutePartCategory;
    }

    public List<Double> getRoutePartTime() {
        return RoutePartTime;
    }

    public void setRoutePartTime(List<Double> routePartTime) {
        RoutePartTime = routePartTime;
    }

    public List<String> getScenicSpot() {
        return scenicSpot;
    }

    public void setScenicSpot(List<String> scenicSpot) {
        this.scenicSpot = scenicSpot;
    }


    public List<Integer> getRoutePartCategory() {
        return RoutePartCategory;
    }

    public void setRoutePartCategory(List<Integer> routePartCategory) {
        RoutePartCategory = routePartCategory;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public List<Integer> getRoutePartCost() {
        return RoutePartCost;
    }

    public void setRoutePartCost(List<Integer> routePartCost) {
        RoutePartCost = routePartCost;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
