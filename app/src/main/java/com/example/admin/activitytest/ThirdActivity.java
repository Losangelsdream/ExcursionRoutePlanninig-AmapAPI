package com.example.admin.activitytest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {
    private List<RouteModel> list = null;
    private List<String> scenicSpot = null;
    private List<String> scenicSpot2 = null;
    private List<String> scenicSpot3 = null;
    private ListView listview = null;
    private RouteAdapter adapter = null;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routeshow);
        context = ThirdActivity.this;
        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();
        initData();
        listview = (ListView) findViewById(R.id.bottom_listview);
        adapter = new RouteAdapter(this, list);
        listview.setAdapter(adapter);


    }

    private void initData()
    {
//        scenicSpot.add("天安门");
//        scenicSpot.add("故宫");
//        scenicSpot.add("西单");
//        scenicSpot2.add("天安门");
//        scenicSpot2.add("东单");
//        scenicSpot3.add("毛主席纪念馆");
//        scenicSpot3.add("人民大会堂");
        list = new ArrayList<RouteModel>();
        list.add(new RouteModel( 4, 100));
        list.add(new RouteModel( 3, 500 ));
        list.add(new RouteModel( 5, 0));
    }
}
