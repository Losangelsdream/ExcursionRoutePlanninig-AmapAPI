package com.example.admin.activitytest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import overlay.BusRouteOverlay;

public class Test extends AppCompatActivity implements RouteSearch.OnRouteSearchListener{
    private AMap aMap;
    private MapView mapView;
    private Context mContext;
    private LatLonPoint start = new LatLonPoint(39.961554,116.358103);
    private LatLonPoint end = new LatLonPoint(39.941853,116.385307);
    private RouteSearch routeSearch;
    private BusRouteOverlay busrouteOverlay;
    private final int ROUTE_TYPE_BUS = 1;
    private String mCurrentCityName = "北京";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mapView = (MapView) findViewById(R.id.mapshow);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start,end);
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, ROUTE_TYPE_BUS, mCurrentCityName, 0);
        routeSearch.calculateBusRouteAsyn(query);
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
                BusPath busPath = result.getPaths().get(1);
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
