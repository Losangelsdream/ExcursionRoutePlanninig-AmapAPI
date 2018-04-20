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


import GA.Preparer;
import GA.World;
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
    private double waytime;
    private double waycost;
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
    private ArrayList<RouteModel> poiroute = new ArrayList<RouteModel>();

    public double[][] poi_way_Tcost ;
    public double[][] poi_way_Mcost ;
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
                            query.setPageSize(14);// 设置每页最多返回多少条poiitem
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
                if(cost_time<=1800)
                {
                    cost_time=cost_time+((cost_time+2000)*0.95);
                }//魔改的时间！！！！！
                Random rand = new Random();
                int random=rand.nextInt(228) + 60;
                MyPoi wayPoi = new MyPoi(poiItems.get(i).getTitle(),cost_time,random,poiItems.get(i).getLatLonPoint());
                poiItemResult.add(wayPoi);
            }
        }


        poi_way_Tcost = new double[poiItemResult.size()][poiItemResult.size()];
        poi_way_Mcost = new double[poiItemResult.size()][poiItemResult.size()];
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





    private  void arrangementSelect(ArrayList<Integer> dataList, ArrayList<Integer>tmpResultList, int resultIndex ,ArrayList<Integer> resultList,double resultTime) {
        // 递归选择下一个
        if(tmpResultList.size()==dataList.size())
        {
            double totaltime=0;
            for (int t=0;t< tmpResultList.size()-1;t++)
            {
                totaltime += poiItemResult.get(tmpResultList.get(t)).getTime_cost()+poi_way_Tcost[tmpResultList.get(t)][tmpResultList.get(t+1)];
            }
            totaltime+=poi_way_Tcost[tmpResultList.get(0)][0];
            totaltime+=poi_way_Tcost[tmpResultList.get(tmpResultList.size()-1)][1];
            if(resultTime>totaltime)
            {
                resultTime=totaltime;
                resultList.clear();
                for(int i=0;i<tmpResultList.size();i++)
                {
                    resultList.add(tmpResultList.get(i));
                }
            }
        }
        else{
        for (int i = 0; i < dataList.size(); i++) {
            // 判断待选项是否存在于排列结果中
            boolean exists = false;
            for (int j = 0; j < resultIndex; j++) {
                if (dataList.get(i).equals(tmpResultList.get(j))) {
                    exists = true;
                    break;
                }
            }
            if (!exists) { // 排列结果不存在该项，才可选择
                tmpResultList.add(dataList.get(i));
                arrangementSelect(dataList, tmpResultList, resultIndex + 1,resultList,resultTime);
                tmpResultList.remove(tmpResultList.size()-1);
            }
        }
        }

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
                if (geocodeResult.getGeocodeQuery().getLocationName().equals(EndPoint_view.getText().toString())&&geocodeResult.getGeocodeQuery().getLocationName().equals(StartingPoint_view.getText().toString()))
                {
                    startllp = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                    endllp = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                }
                else if(geocodeResult.getGeocodeQuery().getLocationName().equals(EndPoint_view.getText().toString()))
                {
                    endllp = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                }else if(geocodeResult.getGeocodeQuery().getLocationName().equals(StartingPoint_view.getText().toString()))
                {
                    startllp = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                }else if(!(geocodeResult.getGeocodeQuery().getLocationName().equals(StartingPoint_view)||geocodeResult.getGeocodeQuery().getLocationName().equals(EndPoint_view)))
                {
                    Toast.makeText(getApplicationContext(), "无法检测到起始点，请重新输入", Toast.LENGTH_SHORT).show();
                }
                System.out.println("起点"+startllp);
                System.out.println("终点"+endllp);
                if(startllp!=null&&endllp!=null)
                {
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
                }
            } else if(geocodeResult==null) {
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
                waytime = (double) busRouteResult.getPaths().get(0).getDuration();
                waycost = (double) busRouteResult.getPaths().get(0).getCost();
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
//            System.out.println("POI点数" + poiItemResult.size());
            if (counter == poiItemResult.size()*poiItemResult.size()-3) {
                for (int i = 0; i <= poiItemResult.size() - 1; i++) {

                    for (int j = 0; j <= poiItemResult.size() - 1; j++) {
                        if(i!=j&&poi_way_Tcost[i][j]==0)
                        {
                            poi_way_Tcost[i][j]=600;
                        }

                    }
                }
                double confine_time = (double) ((results_time[0]+results_time[1])/2*60*60);
                double confine_money = (double) (results_money[0]+results_money[1]/2);
                double tL [] = new double[poiItemResult.size()];
                double mL [] = new double[poiItemResult.size()];
                for (int temp=0;temp<=poiItemResult.size()-1;temp++)
                {
                    tL[temp] = poiItemResult.get(temp).getTime_cost();
                    mL[temp] = poiItemResult.get(temp).getMoney_cost();
                }

                for(int temp2=0;temp2<=poiItemResult.size()-1;temp2++)
                {
                    for(int temp3=0;temp3<=poiItemResult.size()-1;temp3++)
                    {
                        System.out.println("第" + temp2 + "行第" + temp3 + "列的时间是" + poi_way_Tcost[temp2][temp3]);
                        System.out.println("第" + temp2 + "行第" + temp3 + "列的花费是" + poi_way_Mcost[temp2][temp3]);
                        System.out.println("-------");
                    }
                }

                for(int temp4=0;temp4<=poiItemResult.size()-1;temp4++)
                {
                    System.out.println("地址："+poiItemResult.get(temp4).getName());
                    System.out.println("第"+temp4+"个景点时间花费为"+tL[temp4]);
                    System.out.println("第"+temp4+"个景点金钱花费为"+mL[temp4]);
                }
                System.out.println("总时间"+confine_time);





                Preparer preparer = new Preparer(poi_way_Tcost,poi_way_Mcost,tL,mL,confine_time,confine_money,poiItemResult.size());
                World world=new World(preparer);
                world.initSetting();
                world.initPopulation();

                ArrayList<String> poiname = new ArrayList<String>();
                ArrayList<Integer> poiID = new ArrayList<Integer>();
                ArrayList<Double> poiLatitude = new ArrayList<Double>();
                ArrayList<Double> poiLongtitude = new ArrayList<Double>();

                while(world.goNext())world.Evolution();
                for(int i=0;i<world.king.staff.size();i++)
                {
                    double totaltime=0;
                    int totalcost=0;
                    System.out.println("路线"+i);
                    for(int j=0;j<world.king.staff.get(i).path.size();j++)
                    {
                        poiID.add(world.king.staff.get(i).path.get(j));
                        poiname.add(poiItemResult.get(world.king.staff.get(i).path.get(j)).getName());
                        poiLatitude.add(poiItemResult.get(world.king.staff.get(i).path.get(j)).getLatlonp().getLatitude());//纬度
                        poiLongtitude.add(poiItemResult.get(world.king.staff.get(i).path.get(j)).getLatlonp().getLongitude());//经度
                        System.out.println(poiID+"  ");
                        System.out.println(poiname);
                    }


                    poiID.remove(0);
                    poiID.remove(poiID.size()-1);
                    ArrayList<Integer> tmpResultList=new ArrayList<Integer>();
                    ArrayList<Integer> resultList=new ArrayList<Integer>();
                    arrangementSelect(poiID,tmpResultList,0,resultList,999999);

                    resultList.add(1);
                    resultList.add(0,0);
                    System.out.println(resultList+"  ");
                    for (int t=0;t< resultList.size()-1;t++)
                    {
                        totaltime += poiItemResult.get(resultList.get(t)).getTime_cost()+poi_way_Tcost[resultList.get(t)][resultList.get(t+1)];
                        totalcost += (int) (poiItemResult.get(resultList.get(t)).getMoney_cost()+poi_way_Mcost[resultList.get(t)][resultList.get(t+1)]);
                    }
                    System.out.println("总时间花费:"+totaltime);
                    System.out.println("总金钱花费:"+totalcost);

                    poiname.clear();
                    poiLatitude.clear();
                    poiLongtitude.clear();

                    for(int j=0;j<resultList.size();j++)
                    {
                        poiname.add(poiItemResult.get(resultList.get(j)).getName());
                        poiLatitude.add(poiItemResult.get(resultList.get(j)).getLatlonp().getLatitude());//纬度
                        poiLongtitude.add(poiItemResult.get(resultList.get(j)).getLatlonp().getLongitude());//经度
                        System.out.println(resultList+"  ");
                        System.out.println(poiname);
                    }



                    RouteModel RecommendRoute = new RouteModel((i+1),poiLatitude,poiLongtitude,totaltime,totalcost,poiname);
                    poiroute.add(RecommendRoute);

                    poiname = new ArrayList<String>();
                    poiID = new ArrayList<Integer>();
                    poiLatitude = new ArrayList<Double>();
                    poiLongtitude = new ArrayList<Double>();
                    System.out.println();
                }

                Intent intent = new Intent(SecondActivity.this,Route_plan_show_Activity.class);
                intent.putExtra("startpoint",StartingPoint_view.getText().toString());
                intent.putExtra("destination",EndPoint_view.getText().toString());
                intent.putExtra("routepoi",poiroute);
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

