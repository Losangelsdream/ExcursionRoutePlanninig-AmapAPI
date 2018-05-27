package com.example.admin.activitytest;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.util.IOUtils;
import com.amap.api.maps.AMapUtils;
import com.amap.api.services.core.LatLonPoint;
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
import java.util.ArrayList;
import java.util.List;

import overlay.AMapUtil;

public class DataInitActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener,View.OnClickListener{
    private Context context;
    private JSONArray jsonArr;
    private RouteSearch routeSearch;
    private ArrayList<MyPoi> poiItemResult = new ArrayList<>();
    private String mCurrentCityName = "北京";
    private double waytime;
    private double waycost;
    private final int ROUTE_TYPE_BUS = 1;
    private int counter=1;
    private Routedbitem routedbitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_init);
        Connector.getDatabase();
        Button createDatabase = (Button) findViewById(R.id.createButton);
        Button calculateButton = (Button) findViewById(R.id.calculateButton);
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        createDatabase.setOnClickListener(this);
        calculateWayTime();

    }

    private void calculateWayTime() {
        LatLonPoint startpoint = new LatLonPoint(39.961580, 116.357770);
        List<Poidbitem> Poidbitems = DataSupport.findAll(Poidbitem.class);
        List<Integer> ChoiceID = new ArrayList<Integer>();


        for (int i = 0; i <= Poidbitems.size() - 1; i++) {
            LatLonPoint wayLat = new LatLonPoint(Double.parseDouble(Poidbitems.get(i).getLantitude()), Double.parseDouble(Poidbitems.get(i).getLongtitude()));
            float distance = AMapUtils.calculateLineDistance( AMapUtil.convertToLatLng(wayLat), AMapUtil.convertToLatLng(startpoint));
            if (distance<15000)
            {
                ChoiceID.add(i);
            }System.out.println(ChoiceID);
//            MyPoi wayPoi = new MyPoi(Poidbitems.get(i).getName(), Poidbitems.get(i).getTime_cost(), (int) Poidbitems.get(i).getMoney_cost(), wayLat);
//            poiItemResult.add(wayPoi);
        }
        //System.out.println(poiItemResult.size());//111

//        for (int i = 30; i<=poiItemResult.size()-50;i++ )  //20-2+
//        {
//            for (int j = 0; j <= poiItemResult.size() - 1; j++) {
//
//                final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(poiItemResult.get(i).getLatlonp(), poiItemResult.get(j).getLatlonp()); //起点是0  终点是146
//                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, AMapUtils.BUS_TIME_FIRST, mCurrentCityName, 0);
//                routeSearch.calculateBusRouteAsyn(query);
//            }
//
//        }
    }



    public static String getJson(String fileName,Context context)
        {
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
    public void onClick(View view) {
//            DataSupport.deleteAll("poidbitem");
//            String s = getJson("poi.json",this);
//            jsonArr=JSONArray.parseArray(s);
//            System.out.println("aaaa");
//            System.out.println(jsonArr.size());
//            int counter=2;
//            for(int i=0; i<=jsonArr.size()-1;i++)
//            {
//                JSONObject job = jsonArr.getJSONObject(i);
//                job.get("poi_name");
//                job.get("poi_image1");
//                job.get("poi_image2");
//                job.get("poi_image3");
//                job.get("poi_playtime");
//                job.get("poi_ticket_cost");
//                String temp =job.get("poi_latlon").toString().replace("[\"","").replace("\"]","");
//                String[] as = temp.split(",");
//                String lati = as[0];
//                String longti = as[1];
//                System.out.println("纬度"+lati);
//                System.out.println("经度"+longti);
//                Poidbitem poidbitem = new Poidbitem();
//                Poidbitem2 poidbitem2 = new Poidbitem2();
//
//                poidbitem.setId(counter);
//                poidbitem.setName(job.get("poi_name").toString().replace("[\"","").replace("\"]",""));
//                poidbitem.setTime_cost((Float.parseFloat(job.get("poi_playtime").toString().replace("[\"","").replace("\"]",""))));
//                poidbitem.setMoney_cost((Float.parseFloat(job.get("poi_ticket_cost").toString().replace("[\"","").replace("\"]",""))));
//                poidbitem.setLantitude(lati);
//                poidbitem.setLongtitude(longti);
//                poidbitem.save();
//
//
//                poidbitem2.setId(counter);
//                poidbitem2.setName(job.get("poi_name").toString().replace("[\"","").replace("\"]",""));
//                poidbitem2.setTime_cost((Float.parseFloat(job.get("poi_playtime").toString().replace("[\"","").replace("\"]",""))));
//                poidbitem2.setMoney_cost((Float.parseFloat(job.get("poi_ticket_cost").toString().replace("[\"","").replace("\"]",""))));
//                poidbitem2.setLantitude(lati);
//                poidbitem2.setLongtitude(longti);
//                poidbitem2.save();
//
//                counter+=1;
//            }
        calculateWayTime();




        }


        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int rcode) {
            System.out.println("第"+counter+"次回调");
            if (busRouteResult != null && busRouteResult.getPaths() != null) {
                if (busRouteResult.getPaths().size() > 0) {
                    counter += 1;
                    waytime = (double) busRouteResult.getPaths().get(0).getDuration();
                    waycost = (double) busRouteResult.getPaths().get(0).getCost();
                    waytime -= (busRouteResult.getPaths().get(0).getSteps().get(0).getWalk().getDuration())*1.05;

                    for(int i=30;i<=poiItemResult.size()-50;i++) //20-29
                    {
                        for(int j=0;j<=poiItemResult.size()-1;j++)
                    {

                        if (busRouteResult.getBusQuery().getFromAndTo().getFrom() == poiItemResult.get(i).getLatlonp() && busRouteResult.getBusQuery().getFromAndTo().getTo() == poiItemResult.get(j).getLatlonp())

                        {
                            System.out.println("本次回调从" + poiItemResult.get(i).getName() + "到" + poiItemResult.get(j).getName());
                            System.out.println(busRouteResult.getPaths().get(0).getSteps().get(0).getWalk().getDuration());
                            System.out.println("回调次数" + counter + "路线时间" + waytime);
                            System.out.println("回调次数" + counter + "路线花费" + waycost);

                            routedbitem = new Routedbitem();
                            routedbitem.setWaytime((float) waytime);
                            routedbitem.setWaycost((int) waycost);
                            routedbitem.save();

                            List<Poidbitem> Poidbitems = DataSupport.findAll(Poidbitem.class);
                            List<Poidbitem2> Poidbitems2 = DataSupport.findAll(Poidbitem2.class);
                            Poidbitem test = Poidbitems.get(i);
                            Poidbitem2 test2 = Poidbitems2.get(j);

                            System.out.println(i);
                            System.out.println(Poidbitems.get(i).getName());
                            System.out.println(j);
                            System.out.println(Poidbitems2.get(j).getName());
                            test.getRoutedbitem1().add(routedbitem);
                            test2.getRoutedbitem1().add(routedbitem);
                            test.save();
                            test2.save();
                        }

                        }
                    }

                } else if (busRouteResult != null && busRouteResult.getPaths() == null) {
                    Toast.makeText(DataInitActivity.this, "无路线", Toast.LENGTH_SHORT).show();

                } else {
                    counter += 1;


                }

            }

            waytime=0;
            waycost=0;
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
