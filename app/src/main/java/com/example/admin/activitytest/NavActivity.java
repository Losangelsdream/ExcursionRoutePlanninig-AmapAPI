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

    public static LatLonPoint Point;
    private LatLonPoint wayPoint1;
    private LatLonPoint wayPoint2;
    private LatLonPoint endPoint;

    public ArrayList<LatLonPoint>  wayPoints = new ArrayList<LatLonPoint>();

    private RouteOverLay mRouteOverLay;
    private AMapNavi aMapNavi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testnav);
        mapView = (MapView) findViewById(R.id.mapshow);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        Intent intent = getIntent();
        routemodel = (RouteModel)intent.getSerializableExtra("route");


        dataLoad();



    }

    private void dataLoad() {
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


}
