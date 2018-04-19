package com.example.admin.activitytest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

import java.util.ArrayList;
import java.util.List;

public class Route_plan_show_Activity extends AppCompatActivity  implements AdapterView.OnItemClickListener{
    private List<RouteModel> list = null;
    private List<String> scenicSpot = new ArrayList<String>();
    private List<String> scenicSpot2 = new ArrayList<String>();
    private List<String> scenicSpot3 = new ArrayList<String>();
    private ListView listview = null;
    private TextView StartingPoint_view;
    private TextView EndPoint_view;
    private RouteAdapter adapter = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routeshow);
        context = Route_plan_show_Activity.this;
        MapView mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();

        Intent intent = getIntent();
        String startpoint = intent.getStringExtra("startpoint");
        String destination = intent.getStringExtra("destination");
        list = (ArrayList<RouteModel>) intent.getSerializableExtra("routepoi");

        StartingPoint_view = (TextView) findViewById(R.id.start_address);
        EndPoint_view = (TextView) findViewById(R.id.end_address);
        StartingPoint_view.setText(startpoint);
        EndPoint_view.setText(destination);

        initData();
        listview = (ListView) findViewById(R.id.bottom_listview);
        adapter = new RouteAdapter(this, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);


    }

    private void initData()
    {
        System.out.println(list.get(0).getTotalTime());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        RouteModel route_choice = list.get(position);
        ArrayList<String> route_poi = (ArrayList<String>) route_choice.getScenicSpot();

        Intent intent = new Intent(Route_plan_show_Activity.this, NavActivity.class);
        intent.putStringArrayListExtra("route_poi",route_poi);
        intent.putExtra("route",route_choice);
        startActivity(intent);
    }
}
