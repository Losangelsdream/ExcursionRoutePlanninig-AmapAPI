package com.example.admin.activitytest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.ae.dice.InitConfig;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import overlay.BusRouteOverlay;

public class Test extends AppCompatActivity implements RouteSearch.OnRouteSearchListener{
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private LatLonPoint start = new LatLonPoint(39.961554,116.358103);
    private String Start_name = "北京邮电大学";
    private String End_name = "罗方花园";
    private LatLonPoint end = new LatLonPoint(39.641853,116.575307);
    private RouteSearch routeSearch;
    private BusRouteOverlay busrouteOverlay;
    private final int ROUTE_TYPE_BUS = 1;
    private String mCurrentCityName = "北京";

    private String Trans_Mode;
    private String Bus_Start ;
    private String Bus_Des ;
    private String Bus_Direction ;
    private String Bus_Number;
    private Float Bus_duration;
    private Float Walk_distance ;
    private Long Walk_time;
    private ListView listview;
    private Detail detail;
    private DetailAdapter adapter;
    private List<Detail> detail_list = new ArrayList<>();
    public static Pattern pattern1 = Pattern.compile("(?<=\\()(.+?)(?=\\))");
    final private int Bus_Mode=1;
    final private int Walk_Mode=0;
    final private int Start_Mode=2;
    final private int End_Mode=3;
    final private int play_Mode=4;
    private Context context;
    private int counter_recall=0;

    private List<Double> poiLatitude = new ArrayList<Double>();
    private List<Double> poiLongtitude = new ArrayList<Double>();
    private List<String> ScenicSpot = new ArrayList<String>();
    private int totalcost;
    private double totaltime;

    private RouteModel routemodel;
    private List<LatLonPoint> wayPoints = new ArrayList<LatLonPoint>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mapView = (MapView) findViewById(R.id.mapshow3);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        Init();
        detail = new Detail(Start_Mode,routemodel.getScenicSpot().get(0));
        detail_list.add(detail);


        TestSearch();

    }

    private void TestSearch() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        for(int i=0;i<=routemodel.getPoiLongtitude().size()-1;i++)
        {
            LatLonPoint latlon = new LatLonPoint(routemodel.getPoiLatitude().get(i),routemodel.getPoiLongtitude().get(i));
            wayPoints.add(latlon);
        }
        for (int i = 0; i < wayPoints.size() - 1; i++) {
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(wayPoints.get(i),wayPoints.get(i+1));
            RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, ROUTE_TYPE_BUS, mCurrentCityName, 0);
            routeSearch.calculateBusRouteAsyn(query);
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void Init() {

        poiLatitude.add(39.961580);
        poiLongtitude.add(116.357770);
        ScenicSpot.add("北京邮电大学");
        poiLatitude.add(39.942900);
        poiLongtitude.add(116.384510000);
        ScenicSpot.add("什刹海");
        poiLatitude.add(39.908860);
        poiLongtitude.add(116.397390);
        ScenicSpot.add("天安门");
        poiLatitude.add(39.964860);
        poiLongtitude.add(116.357090);
        ScenicSpot.add("北京邮电大学北门");
        totalcost=357;
        totaltime= 14730.0;
        routemodel = new RouteModel(1,poiLatitude,poiLongtitude,totaltime,totalcost,ScenicSpot);
    }

    private void initView() {
        if (aMap == null)
        {
            aMap = mapView.getMap();
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int i) {

        if (result != null && result.getPaths() != null) {
            if (result.getPaths().size() > 0) {
                BusPath busPath = result.getPaths().get(0);
                List<BusStep> busStep = busPath.getSteps();
                for (int t = 0; t <= busStep.size() - 1; t++) {
                    if (busStep.get(t).getBusLines() != null && busStep.get(t).getWalk() != null)//该段路径既有公交又需要步行
                    {
                        System.out.println(busStep.get(t).getBusLines());
                        System.out.println(busStep.get(t).getWalk());
                        System.out.println("-----");
                        Walk_distance = busStep.get(t).getWalk().getDistance();
                        Walk_time = busStep.get(t).getWalk().getDuration();
                        detail = new Detail(Walk_Mode,Walk_distance,Walk_time);
                        detail_list.add(detail);
                        if(busStep.get(t).getBusLines().size()!=0) {
                            Bus_Start = busStep.get(t).getBusLines().get(0).getDepartureBusStation().getBusStationName();
                            Bus_Des = busStep.get(t).getBusLines().get(0).getArrivalBusStation().getBusStationName();
                            Bus_duration = busStep.get(t).getBusLines().get(0).getDuration();
                            Bus_Number = busStep.get(t).getBusLines().get(0).getBusLineName().replaceAll("\\(.*?\\)", "");
                            Matcher m = pattern1.matcher(busStep.get(t).getBusLines().get(0).getBusLineName());
                            while (m.find()) {
                                Bus_Direction = (m.group());
                            }
                            detail = new Detail(Bus_Mode,Bus_Start,Bus_Des,Bus_Direction,Bus_Number,Bus_duration);
                            detail_list.add(detail);
                        }


                    }
                    else if(busStep.get(t).getBusLines() != null && busStep.get(t).getWalk()==null) //该段路仅有公交无需步行(可能是地铁换乘)
                    {
                        System.out.println(busStep.get(t).getBusLines());
                        System.out.println("-----");
                        Bus_Start = (busStep.get(t).getBusLines().get(0).getDepartureBusStation().getBusStationName());
                        Bus_Des = (busStep.get(t).getBusLines().get(0).getArrivalBusStation().getBusStationName());
                        Bus_duration = (busStep.get(t).getBusLines().get(0).getDuration());
                        Bus_Number = busStep.get(t).getBusLines().get(0).getBusLineName().replaceAll("\\(.*?\\)", "");
                        Matcher m = pattern1.matcher(busStep.get(t).getBusLines().get(0).getBusLineName());
                        while (m.find()) {
                            Bus_Direction = (m.group());
                        }
                        detail = new Detail(Bus_Mode,Bus_Start,Bus_Des,Bus_Direction,Bus_Number,Bus_duration);
                        detail_list.add(detail);
                    }
                    else if(busStep.get(t).getBusLines()==null && busStep.get(t).getWalk()!=null) //该路段仅有步行没有交通（一般是走到终点)
                    {
                        System.out.println(busStep.get(t).getWalk());
                        System.out.println("-----");
                        Walk_distance = busStep.get(t).getWalk().getDistance();
                        Walk_time = busStep.get(t).getWalk().getDuration();
                        detail = new Detail(Walk_Mode,Walk_distance,Walk_time);
                        detail_list.add(detail);
                    }
                    else {
                        System.out.println("===========");
                    }
            }
                busrouteOverlay = new BusRouteOverlay(mContext,aMap,busPath,result.getStartPos(),result.getTargetPos());
                busrouteOverlay.removeFromMap();
                busrouteOverlay.addToMap(true,true);
                busrouteOverlay.zoomToSpan();
            }else if (result != null && result.getPaths() == null) {
                Toast.makeText(Test.this,"无路线",Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(Test.this,"无路线",Toast.LENGTH_SHORT).show();
            }
        }
        counter_recall=counter_recall+1;

        detail = new Detail(play_Mode,routemodel.getScenicSpot().get(counter_recall));
        detail_list.add(detail);

        if(counter_recall==(routemodel.getPoiLatitude().size()-1))
        {
            System.out.println("判断中计数器数值为"+counter_recall);
            System.out.println("判断中列表长度为"+(routemodel.getPoiLatitude().size()-1));
            counter_recall =0;
            detail = new Detail(End_Mode,routemodel.getScenicSpot().get(routemodel.getScenicSpot().size()-1));
            detail_list.add(detail);
            for(int t=0;t<=detail_list.size()-1;t++)
            {
                System.out.println(detail_list.get(t).to_MyString());
            }
            listview = (ListView) findViewById(R.id.listview);
            adapter = new DetailAdapter(this, detail_list);
            listview.setAdapter(adapter);


        }
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
