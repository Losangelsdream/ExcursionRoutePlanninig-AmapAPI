package com.example.admin.activitytest;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;




import java.util.ArrayList;
import java.util.List;

import overlay.AMapUtil;

public class PoiSearchActivity extends Activity implements OnPoiSearchListener {

    private LatLonPoint lp = new LatLonPoint(39.961580,116.357770);// 北京邮电大学
    String[] category= new String[]{"110202"};

    private PoiResult poiresult; //POI-api结果返回
    private int currentPage = 0;//当前页面
    private PoiSearch.Query query; //POI-api查询条件类
    private ArrayList<PoiItem> poiItems = new ArrayList<PoiItem>(); // POI-api数据
    private PoiSearch poiSearch; //POI-api接口类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);
        Intent intent=getIntent();
        String choice = intent.getStringExtra("choice");
        JSONObject json = JSONObject.parseObject(choice);
        doSearchQuery();

    }




    protected void doSearchQuery() {
        currentPage = 0;
        for (int a=0; a<category.length;a++)
        {
            query = new PoiSearch.Query("", category[a], "010");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            query.setPageSize(200);// 设置每页最多返回多少条poiitem
            query.setPageNum(currentPage);// 设置查第一页
            System.out.println("bbbbbb");
            if (lp != null) {
                poiSearch = new PoiSearch(this, query);
                poiSearch.setOnPoiSearchListener(this);
                poiSearch.setBound(new PoiSearch.SearchBound(lp, 20000, true));//
                // 设置搜索区域为以lp点为圆心，其周围3000米范围
                poiSearch.searchPOIAsyn();// 异步搜索
            }
        }

    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int rcode) {
       poiItems = poiResult.getPois();
       System.out.println(poiItems.size());
       for(int i=0;i<=poiItems.size()-1;i++)
       {
           if(poiItems.get(i).getEnter()!=null)
           {
           System.out.println("经纬度"+poiItems.get(i).getLatLonPoint()+"信息："+poiItems.get(i).getTitle()+"ID"+poiItems.get(i).getPoiId());
           LatLng latlng= AMapUtil.convertToLatLng(poiItems.get(i).getEnter());
           LatLng latlng2 = AMapUtil.convertToLatLng(poiItems.get(i).getLatLonPoint());
           float distance = AMapUtils.calculateLineDistance(latlng,latlng2);
           System.out.println(distance);
           float area = (float) (0.5*distance*distance);
           System.out.println(area);
               System.out.println("-----------------------------");
           }

       }

//        System.out.println(poiItems.get(5).getEnter());

//        final Marker marker = aMap.addMarker(new MarkerOptions().position(latlng));

//        final Marker marker2 = aMap.addMarker(new MarkerOptions().position(latlng2));






    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
