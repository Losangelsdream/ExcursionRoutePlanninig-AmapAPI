package com.example.admin.activitytest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2018/4/19.
 */

public class Detail {
    private int Trans_Mode;
//    private List<String> Bus_Start = new ArrayList<>();
//    private List<String> Bus_Des = new ArrayList<>();
//    private List<String> Bus_Direction = new ArrayList<>();
//    private List<String> Bus_Station_Number = new ArrayList<>();
//    private List<Float> Bus_duration = new ArrayList<>();
//    private List<String> Walk_distance = new ArrayList<>();
//    private List<Float> Walk_time = new ArrayList<>();

    private String Bus_Start;
    private String Bus_Des;
    private String Bus_Direction;
    private String Bus_Station_Number;
    private Float  Bus_duration;
    private Float Walk_distance;
    private Long  Walk_time;
    private String start_name;

    public Detail(int Trans_mode,String My_name)
    {
        this.Trans_Mode=Trans_mode;
        this.start_name=My_name;
    }


    public Detail(int Trans_Mode,String Bus_Start,String Bus_Des,String Bus_Direction,String Bus_Station_Number,Float Bus_duration)
    {
        this.Trans_Mode=Trans_Mode;
        this.Bus_Des=Bus_Des;
        this.Bus_Direction=Bus_Direction;
        this.Bus_duration=Bus_duration;
        this.Bus_Start=Bus_Start;
        this.Bus_Station_Number=Bus_Station_Number;

    }

    public Detail(int Trans_Mode,Float Walk_distance,Long Walk_duration)
    {
        this.Trans_Mode=Trans_Mode;
        this.Walk_distance=Walk_distance;
        this.Walk_time=Walk_duration;
    }

    public String to_MyString()
    {
        String s = "公交起点"+getBus_Start()+"步行距离"+getWalk_distance()+"模式"+getTrans_Mode();
        return s;
    }

    public String getBus_Start() {
        return Bus_Start;
    }

    public void setBus_Start(String bus_Start) {
        Bus_Start = bus_Start;
    }

    public String getBus_Des() {
        return Bus_Des;
    }

    public void setBus_Des(String bus_Des) {
        Bus_Des = bus_Des;
    }

    public String getBus_Direction() {
        return Bus_Direction;
    }

    public void setBus_Direction(String bus_Direction) {
        Bus_Direction = bus_Direction;
    }

    public String getBus_Station_Number() {
        return Bus_Station_Number;
    }

    public void setBus_Station_Number(String bus_Station_Number) {
        Bus_Station_Number = bus_Station_Number;
    }

    public Float getBus_duration() {
        return Bus_duration;
    }

    public void setBus_duration(Float bus_duration) {
        Bus_duration = bus_duration;
    }


    public Float getWalk_distance() {
        return Walk_distance;
    }

    public void setWalk_distance(Float walk_distance) {
        Walk_distance = walk_distance;
    }

    public Long getWalk_time() {
        return Walk_time;
    }

    public void setWalk_time(Long walk_time) {
        Walk_time = walk_time;
    }

    public int getTrans_Mode() {
        return Trans_Mode;
    }

    public void setTrans_Mode(int trans_Mode) {
        Trans_Mode = trans_Mode;
    }

    public String getStart_name() {
        return start_name;
    }

    public void setStart_name(String start_name) {
        this.start_name = start_name;
    }
}
