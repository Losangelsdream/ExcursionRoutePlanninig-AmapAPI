package com.example.admin.activitytest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import overlay.*;

import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavActivity extends AppCompatActivity implements OnRouteSearchListener{
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private GeocodeSearch geocoderSearch;
    private String mCurrentCityName = "北京";
    private final int ROUTE_TYPE_BUS = 1;
    private BusRouteOverlay busrouteOverlay;
    private boolean flag = false;
    private int poi_length;

    private int i = 0;


    private RouteSearch routeSearch;
    private RouteSearch routeSearch2;
    private RouteSearch routeSearch3;
    private BusRouteResult busrouteResult;
    private int counter = 0;
    private RouteModel routemodel;


    private DetailAdapter adapter;
    private List<Detail> detail_list = new ArrayList<>();

    public static LatLonPoint Point;
    private LatLonPoint wayPoint1;
    private LatLonPoint wayPoint2;
    private LatLonPoint endPoint;

    public ArrayList<LatLonPoint>  wayPoints = new ArrayList<LatLonPoint>();

    private RouteOverLay mRouteOverLay;

    final private int Bus_Mode=1;
    final private int Walk_Mode=0;
    final private int Start_Mode=2;
    final private int End_Mode=3;
    final private int play_Mode=4;
    private int counter_recall=0;


    private String Bus_Start ;
    private String Bus_Des ;
    private String Bus_Direction ;
    private String Bus_Number;
    private Float Bus_duration;
    private Float Walk_distance ;
    private Long Walk_time;
    private ListView listview;
    private Detail detail;
    public static Pattern pattern1 = Pattern.compile("(?<=\\()(.+?)(?=\\))");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testnav);
        mapView = (MapView) findViewById(R.id.mapshow2);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        Intent intent = getIntent();
        routemodel = (RouteModel)intent.getSerializableExtra("route");

        detail = new Detail(Start_Mode,routemodel.getScenicSpot().get(0));
        detail_list.add(detail);

        RoadSearch();

    }

    private void RoadSearch() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        for(int i=0;i<=routemodel.getPoiLongtitude().size()-1;i++)
        {
            LatLonPoint latlon = new LatLonPoint(routemodel.getPoiLatitude().get(i),routemodel.getPoiLongtitude().get(i));
            wayPoints.add(latlon);
        }
        for (i = 0; i < wayPoints.size() - 1; i++) {
                final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(wayPoints.get(i),wayPoints.get(i+1));
                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, ROUTE_TYPE_BUS, mCurrentCityName, 0);
                routeSearch.calculateBusRouteAsyn(query);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    private void initView() {
        if (aMap == null)
        {
            aMap = mapView.getMap();
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int i) {

        if (result != null && result.getPaths() != null){
            if(result.getPaths().size()>0)
            {
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
                  if(result.getStartPos().toString().equals(wayPoints.get(wayPoints.size()-2).toString()))
                  {
                      busrouteOverlay.removeFromMap();
                      busrouteOverlay.addToMap(false,true);
                      busrouteOverlay.zoomToSpan();
                      counter+=1;
                  }
                  else if(result.getStartPos().toString().equals(wayPoints.get(0).toString())){
                      busrouteOverlay.removeFromMap();
                      busrouteOverlay.addToMap(true,false);
                      busrouteOverlay.zoomToSpan();
                      counter+=1;
                  }
                  else
                  {
                      busrouteOverlay.removeFromMap();
                      busrouteOverlay.addToMap(false,false);
                      busrouteOverlay.zoomToSpan();
                      counter+=1;
                  }
            }else if (result != null && result.getPaths() == null) {
                Toast.makeText(NavActivity.this,"无路线",Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(NavActivity.this,"无路线",Toast.LENGTH_SHORT).show();
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
            detail_list.add(detail);//加入终点
            detail_list.remove(detail_list.size()-2);//删掉倒数第二个并不是景点的位置
            for(int t=0;t<=detail_list.size()-1;t++)
            {
                System.out.println(detail_list.get(t).to_MyString());
            }
            listview = (ListView) findViewById(R.id.listview);
            adapter = new DetailAdapter(this, detail_list);
            listview.setAdapter(adapter);


        }
        if(counter == wayPoints.size()-1)
        {
            wayPoints.clear();
            counter = 0;
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
