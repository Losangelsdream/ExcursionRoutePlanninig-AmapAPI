package com.example.admin.activitytest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import java.util.List;
import java.util.Random;


import overlay.AMapUtil;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener,PoiSearch.OnPoiSearchListener,GeocodeSearch.OnGeocodeSearchListener,RouteSearch.OnRouteSearchListener {
    private RangeSeekBar timeSeekBar = null;
    private int counter = 0;
    private RangeSeekBar financialSeekBar = null;
    private Button planButton = null;
    private ImageView Change_Image = null;
    private CheckBox checkbox1 = null;
    private CheckBox checkbox2 = null;
    private CheckBox checkbox3 = null;
    private CheckBox checkbox4 = null;
    private RouteSearch routeSearch;
    private TextView StartingPoint_view = null;
    private TextView EndPoint_view = null;
    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    private String Location;
    private LatLonPoint startllp;
    private DialogInterface.OnCancelListener cancelListener;
    private ProgressDialog pd4;

    private LatLonPoint endllp;
    private String mCurrentCityName = "北京";
    private int waytime;
    private int waycost;
    private final int ROUTE_TYPE_BUS = 1;


    public  JSONObject jo = new JSONObject();


    String[] view= new String[]{"110202"};// 110202国家级景点
    String[] eatery = new String[]{"050900"}; //050900甜品店
    String[] history = new String[] {"140100"};//140100博物馆
    String[] mall = new String[] {"060101"};//060101购物中心

    private PoiResult poiresult; //POI-api结果返回
    private int currentPage = 0;//当前页面
    private PoiSearch.Query query; //POI-api查询条件类
    private ArrayList<PoiItem> poiItems = new ArrayList<PoiItem>(); // POI-api数据
    private PoiSearch poiSearch; //POI-api接口类
    private ArrayList<MyPoi> poiItemResult;
    public int[][] poi_way_Tcost ;
    public int[][] poi_way_Mcost ;
    public float[] results_time;
    public float[] results_money;



    private GeocodeSearch geocoderSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice);
        InitView();

        Intent intent = getIntent();
        Location = intent.getStringExtra("address");
        StartingPoint_view.setText(Location);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        //do something
    }

    /**
     * 定义界面视图的显现初始化
     */
    private void InitView()
    {
        Change_Image = (ImageView)findViewById(R.id.map_route_exchange);
        planButton = (Button) findViewById(R.id.buttonSubscribe);
        checkbox1 = (CheckBox) findViewById(R.id.hobby_view);
        checkbox2 = (CheckBox) findViewById(R.id.hobby_history);
        checkbox3 = (CheckBox) findViewById(R.id.hobby_mall);
        checkbox4 = (CheckBox) findViewById(R.id.hobby_eatery);


        checkBoxList.add(checkbox1);
        checkBoxList.add(checkbox2);
        checkBoxList.add(checkbox3);
        checkBoxList.add(checkbox4);

        StartingPoint_view = findViewById(R.id.start_address);
        EndPoint_view = findViewById(R.id.end_address);

        timeSeekBar = (RangeSeekBar) findViewById(R.id.TimeBar);
        financialSeekBar = (RangeSeekBar) findViewById(R.id.financialBar);

        planButton.setOnClickListener(this);
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        Change_Image.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startaddress_temp = StartingPoint_view.getText().toString();
                String endaddress_temp = EndPoint_view.getText().toString();
                StartingPoint_view.setText(endaddress_temp);
                EndPoint_view.setText(startaddress_temp);
            }
        });

    }

    /**
     * 规划按钮点击函数
     * @param view
     */
    @Override
    public void onClick(View view) {

        geoSearch();



    }

//            Intent intent2 = new Intent(SecondActivity.this,PoiSearchActivity.class);
//            intent2.putExtra("choice", jo.toString());
//            startActivity(intent2);



    private void geoSearch() {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        GeocodeQuery query = new GeocodeQuery(StartingPoint_view.getText().toString(), "010");
        geocoderSearch.getFromLocationNameAsyn(query);
        GeocodeQuery query2 = new GeocodeQuery(EndPoint_view.getText().toString(), "010");
        geocoderSearch.getFromLocationNameAsyn(query2);
    }

    private void doPoiSearchQuery() {
        currentPage = 0;
        if(checkbox1.isChecked())
        {
                        for (int a=0; a<view.length;a++)
                        {
                            query = new PoiSearch.Query("", view[a], "010");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                            query.setPageSize(3);// 设置每页最多返回多少条poiitem
                            query.setPageNum(currentPage);// 设置查第一页
                            if (startllp != null) {
                                poiSearch = new PoiSearch(this, query);
                                poiSearch.setOnPoiSearchListener(this);
                                poiSearch.setBound(new PoiSearch.SearchBound(startllp, 15000, true));//
                                // 设置搜索区域为以startllp点为圆心，其周围15000米范围
                                poiSearch.searchPOIAsyn();// 异步搜索
                            }
                        }
        }
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
        poiItems = poiResult.getPois();
        MyPoi startPoi = new MyPoi(StartingPoint_view.getText().toString(),0,0,startllp);
        MyPoi endPoi = new MyPoi(EndPoint_view.getText().toString(),0,0,endllp);
        poiItemResult = new ArrayList<MyPoi>();
        counter=0;
        poiItemResult.add(startPoi);
        poiItemResult.add(endPoi);
        for(int i=0;i<=poiItems.size()-1;i++)
        {
            if(poiItems.get(i).getEnter()!=null)
            {
                System.out.println("经纬度"+poiItems.get(i).getLatLonPoint()+"信息："+poiItems.get(i).getTitle()+"ID"+poiItems.get(i).getPoiId());
                LatLng latlngFrom= AMapUtil.convertToLatLng(poiItems.get(i).getEnter());
                LatLng latlngTo = AMapUtil.convertToLatLng(poiItems.get(i).getLatLonPoint());
                double distance = AMapUtils.calculateLineDistance(latlngFrom,latlngTo);
                double perimeter = 2*Math.PI*distance;
                double cost_time = Math.floor(perimeter/70)*60; //单位秒
                if(cost_time<=1000)
                {
                    cost_time+=600;
                }//魔改的时间！！！！！
                Random rand = new Random();
                int random=rand.nextInt(228) + 60;
                MyPoi wayPoi = new MyPoi(poiItems.get(i).getTitle(),cost_time,random,poiItems.get(i).getLatLonPoint());
                poiItemResult.add(wayPoi);
            }
        }


        poi_way_Tcost = new int[poiItemResult.size()][poiItemResult.size()];
        poi_way_Mcost = new int[poiItemResult.size()][poiItemResult.size()];
        calculateWayTime();

    }

    private void calculateWayTime() {



        for(int i=0;i<=poiItemResult.size()-1;i++)
        {
            for(int j=0;j<=poiItemResult.size()-1;j++)
            {

                final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(poiItemResult.get(i).getLatlonp(),poiItemResult.get(j).getLatlonp());
                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, ROUTE_TYPE_BUS, mCurrentCityName, 0);
                routeSearch.calculateBusRouteAsyn(query);
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rcode) {
        if (rcode==1000)
        {
            if(geocodeResult!=null)
            {
                if (geocodeResult.getGeocodeQuery().getLocationName().equals(EndPoint_view.getText().toString())) {
                    endllp = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                }
                else{
                    startllp = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                }
                results_time = timeSeekBar.getCurrentRange();
                results_money = financialSeekBar.getCurrentRange();
                StringBuffer sb = new StringBuffer();
                //遍历集合中的checkBox,判断是否选择，获取选中的文本
                for (CheckBox checkbox : checkBoxList) {
                    if (checkbox.isChecked()) {
                        sb.append(checkbox.getText().toString() + " ");
                    }
                }
                if (sb != null && "".equals(sb.toString())) {
                    Toast.makeText(getApplicationContext(), "请至少选择一个", Toast.LENGTH_SHORT).show();
                }
                if((int)results_time[1]==0&&(results_time[1]-results_time[0])<1)
                {
                    Toast.makeText(getApplicationContext(), "时间的最大值或间距不得为0", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        jo.put("start_address", startllp.toString());
                        jo.put("end_address", endllp.toString());
                        jo.put("time_min", (int) results_time[0]);
                        jo.put("time_max", (int) results_time[1]);
                        jo.put("money_min", (int) results_money[0]);
                        jo.put("money_max", (int) results_money[1]);
                        jo.put("view", checkbox1.isChecked());
                        jo.put("history", checkbox2.isChecked());
                        jo.put("mall", checkbox3.isChecked());
                        jo.put("rest", checkbox4.isChecked());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), jo.toString(), Toast.LENGTH_SHORT).show();
                    doPoiSearchQuery();
                }
            }else if(geocodeResult==null) {
                Toast.makeText(SecondActivity.this,"没有搜索到对应的地址，请检查地址的正误",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(SecondActivity.this, "没有搜索到对应的地址，请检查地址的正误，错误码："+rcode, Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int rcode) {
        if (busRouteResult != null && busRouteResult.getPaths() != null) {
            if (busRouteResult.getPaths().size() > 0) {
                counter += 1;
                waytime = (int) busRouteResult.getPaths().get(0).getDuration();
                waycost = (int) busRouteResult.getPaths().get(0).getCost();
                for (int i = 0; i <=poiItemResult.size() - 1; i++) {
                    for (int j = 0; j <=poiItemResult.size() - 1; j++) {
                        if (busRouteResult.getBusQuery().getFromAndTo().getFrom() == poiItemResult.get(i).getLatlonp() && busRouteResult.getBusQuery().getFromAndTo().getTo() == poiItemResult.get(j).getLatlonp()) {
                            poi_way_Tcost[i][j] = waytime;
                            poi_way_Mcost[i][j] = waycost;

                        }
                    }
                }

            } else if (busRouteResult != null && busRouteResult.getPaths() == null) {
                Toast.makeText(SecondActivity.this, "无路线", Toast.LENGTH_SHORT).show();

            } else{
                counter += 1;

            }


            }
            System.out.println("计数器数值" + counter);
            System.out.println("POI点数" + poiItemResult.size());
            if (counter == poiItemResult.size()*poiItemResult.size()) {
                for (int i = 0; i <= poiItemResult.size() - 1; i++) {
                    System.out.println("地址："+poiItemResult.get(i).getName()+"时间花费："+poiItemResult.get(i).getTime_cost());
                    for (int j = 0; j <= poiItemResult.size() - 1; j++) {
                        if(i!=j&&poi_way_Tcost[i][j]==0)
                        {
                            poi_way_Tcost[i][j]=600;
                        }
                        System.out.println("第" + i + "行第" + j + "列的时间是" + poi_way_Tcost[i][j]);
                        System.out.println("第" + i + "行第" + j + "列的花费是" + poi_way_Mcost[i][j]);
                        System.out.println("-------");

                    }
                }
                int confine_time = (int) ((results_time[0]+results_time[1])/2);
                int confine_money = (int) (results_money[0]+results_money[1]/2);
                //GA(poi_way_Mcost,poi_way_Tcost,poiItemResult,confine_time,confine_money);
                Intent intent = new Intent(SecondActivity.this,Route_plan_show_Activity.class);
                intent.putExtra("startpoint",StartingPoint_view.getText().toString());
                intent.putExtra("destination",EndPoint_view.getText().toString());
                startActivity(intent);
                finish();
                routeSearch.setRouteSearchListener(null);

            }
            System.out.println("回调完毕");


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

