package com.example.admin.activitytest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;

import java.util.ArrayList;
import java.util.List;

public class PoiSearchActivity extends Activity implements OnPoiSearchListener {
    private MapView mapview; //地图显示
    private AMap aMap;  //地图空间
    private LatLonPoint lp = new LatLonPoint(39.961580,116.357770);// 北京邮电大学

    private PoiResult poiresult; //POI-api结果返回
    private int currentPage = 0;//当前页面
    private PoiSearch.Query query; //POI-api查询条件类
    private ArrayList<PoiItem> poiItems = new ArrayList<PoiItem>(); // POI-api数据
    private PoiSearch poiSearch; //POI-api接口类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
        //地图显示
        mapview = (MapView) findViewById(R.id.mapView);
        mapview.onCreate(savedInstanceState);
        init();
        doSearchQuery();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapview.getMap();
        }
    }

    protected void doSearchQuery() {
        currentPage = 0;
        query = new PoiSearch.Query("", "290", "010");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        if (lp != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.setBound(new PoiSearch.SearchBound(lp, 3000, true));//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
       poiresult = poiResult;
       poiItems = poiresult.getPois();
       for (int i=0; i <=poiItems.size()-1;i++)
       {

       }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
