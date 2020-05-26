package com.demo.parking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class AddressCheckActivity extends AppCompatActivity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private TextView back;
    private TextView done;
    private Intent intent = null;
    GeoCoder mCoder = GeoCoder.newInstance();

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
                return;
            } else {
                //详细地址
                String address = reverseGeoCodeResult.getAddress();
                Log.e("ADDRESS", address);
                intent = new Intent();
                intent.putExtra("address", address);
                intent.putExtra("latitude", reverseGeoCodeResult.getLocation().latitude);
                intent.putExtra("longitude", reverseGeoCodeResult.getLocation().longitude);
                Toast.makeText(AddressCheckActivity.this, "地址 : " + address, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_check);

        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView);
        back = findViewById(R.id.tv_back);
        done = findViewById(R.id.tv_done);
        //
        mCoder.setOnGetGeoCodeResultListener(listener);

        mBaiduMap = mMapView.getMap();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent == null) {
                    Toast.makeText(AddressCheckActivity.this, "未选择地址", Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        navigateTo();
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 单击地图
             */
            @Override
            public void onMapClick(LatLng point) {
                mBaiduMap.clear();
                BitmapDescriptor mbitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
                MarkerOptions ooA = new MarkerOptions().position(point).icon(mbitmap);
                mBaiduMap.addOverlay(ooA);
                mCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(point)
                        // POI召回半径，允许设置区间为0-1000米，超过1000米按1000米召回。默认值为1000
                        .radius(0));
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }

        });
    }

    private void navigateTo() {
        LatLng ll = new LatLng(30.499890, 104.088196);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(16);
        mBaiduMap.animateMapStatus(update);
    }
}
