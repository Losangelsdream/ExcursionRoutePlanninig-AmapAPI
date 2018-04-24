package com.example.admin.activitytest;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.util.IOUtils;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.ae.route.route.Route;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataInitActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {
    private Context context;
    private JSONArray jsonArr;
    private RouteSearch routeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_init);
        Button createDatabase = (Button) findViewById(R.id.createButton);
        Button calculateButton  = (Button)findViewById(R.id.calculateButton);
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        createDatabase.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Connector.getDatabase();
                String s = getJson("poi.json",DataInitActivity.this);
                jsonArr=JSONArray.parseArray(s);
                System.out.println("aaaa");
                System.out.println(jsonArr.size());
                DataSupport.deleteAll("poidbitem");

                for(int i=0; i<=jsonArr.size()-1;i++)
                {
                    JSONObject job = jsonArr.getJSONObject(i);
                    job.get("poi_name");
                    job.get("poi_image1");
                    job.get("poi_image2");
                    job.get("poi_image3");
                    job.get("poi_playtime");
                    job.get("poi_ticket_cost");
                    String temp =job.get("poi_latlon").toString().replace("[\"","").replace("\"]","");
                    String[] as = temp.split(",");
                    String lati = as[0];
                    String longti = as[1];
                    System.out.println("纬度"+lati);
                    System.out.println("经度"+longti);
                    Poidbitem poidbitem = new Poidbitem();
                    poidbitem.setName(job.get("poi_name").toString().replace("[\"","").replace("\"]",""));
                    poidbitem.setTime_cost((Float.parseFloat(job.get("poi_playtime").toString().replace("[\"","").replace("\"]",""))));
                    poidbitem.setMoney_cost((Float.parseFloat(job.get("poi_ticket_cost").toString().replace("[\"","").replace("\"]",""))));
                    poidbitem.setLantitude(lati);
                    poidbitem.setLongtitude(longti);
                    poidbitem.save();
                }

            }
        });

    }


    public static String getJson(String fileName,Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
