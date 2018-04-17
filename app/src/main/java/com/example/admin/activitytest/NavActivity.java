package com.example.admin.activitytest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;

import java.util.ArrayList;

public class NavActivity extends AppCompatActivity implements OnGeocodeSearchListener,OnRouteSearchListener{
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

    public static LatLonPoint Point;
    private LatLonPoint wayPoint1;
    private LatLonPoint wayPoint2;
    private LatLonPoint endPoint;

    public static ArrayList<LatLonPoint>  wayPoints = new ArrayList<LatLonPoint>();

    private RouteOverLay mRouteOverLay;
    private AMapNavi aMapNavi;

    //private NaviLatLng startPoint = new NaviLatLng(39.961580,116.357770);
    //private NaviLatLng endPoint = new NaviLatLng(39.904556,116.427231);
    //private NaviLatLng wayPoint1 = new NaviLatLng(39.9261934962,116.3895034790);
    //private NaviLatLng wayPoint2 = new NaviLatLng(39.9081561294,116.3980865479);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testnav);
        mapView = (MapView) findViewById(R.id.mapshow);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        Intent intent = getIntent();
        ArrayList<String> route_poi = intent.getStringArrayListExtra("route_poi");
        poi_length = route_poi.size();
        dataLoad(route_poi);



    }

    private void dataLoad(ArrayList<String> route_poi) {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
       for(int s=0; s<=route_poi.size()-1;s++)
       {
           GeocodeQuery query = new GeocodeQuery(route_poi.get(s), "010");
           geocoderSearch.getFromLocationNameAsyn(query);
           try {
               Thread.sleep(300);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }


//       startPoint = new LatLonPoint(39.961580,116.357770);//北京邮电大学
//       wayPoint1 = new LatLonPoint(39.926193,116.389503);//北海公园
//       wayPoint2 = new LatLonPoint(39.908156,116.398086);//天安门
//       endPoint = new LatLonPoint(39.904556,116.427231);//北京站

    }

    private void searchRouteResult(int routeType, int mode) {
        if (wayPoints.get(0) == null) {
            Toast.makeText(NavActivity.this, "正在定位中，稍后再试...", Toast.LENGTH_SHORT).show();
        }
        if (wayPoints.get(wayPoints.size() - 1) == null) {
            Toast.makeText(NavActivity.this, "无终点", Toast.LENGTH_SHORT).show();
        }

            final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(wayPoints.get(i),wayPoints.get(i+1));
            System.out.println(wayPoints.get(i));
            System.out.println(wayPoints.get(i+1));

            if (routeType == ROUTE_TYPE_BUS) {
                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, mode, mCurrentCityName, 0);
             routeSearch.calculateBusRouteAsyn(query);
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

                  busrouteResult = result;
                  BusPath busPath = result.getPaths().get(1);
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
                //BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
               // mBusResultList.setAdapter(mBusResultListAdapter);
            }else if (result != null && result.getPaths() == null) {
                Toast.makeText(NavActivity.this,"无路线",Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(NavActivity.this,"无路线",Toast.LENGTH_SHORT).show();
            }
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


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        Point = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
        wayPoints.add(Point);
        if(wayPoints.size()==poi_length) {
            for (i = 0; i < wayPoints.size() - 1; i++) {
                routeSearch = new RouteSearch(this);
                routeSearch.setRouteSearchListener(this);
                if (wayPoints.get(0) == null) {
                    Toast.makeText(NavActivity.this, "正在定位中，稍后再试...", Toast.LENGTH_SHORT).show();
                }
                if (wayPoints.get(wayPoints.size() - 1) == null) {
                    Toast.makeText(NavActivity.this, "无终点", Toast.LENGTH_SHORT).show();
                }

                final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(wayPoints.get(i),wayPoints.get(i+1));
                RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, ROUTE_TYPE_BUS, mCurrentCityName, 0);
                routeSearch.calculateBusRouteAsyn(query);
                }
            }
        }

}
