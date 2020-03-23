package com.demo.parking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.demo.parking.db.Appointment;
import com.demo.parking.db.CarPark;
import com.demo.parking.db.Parking;
import com.demo.parking.db.User;
import com.demo.parking.ui.CollectActivity;
import com.demo.parking.ui.ShareActivity;
import com.demo.parking.ui.login.LoginActivity;
import com.demo.parking.ui.login.RegisterActivity;
import com.demo.parking.ui.user.PersonaInfoActivity;
import com.demo.parking.ui.user.UpdatePawActivity;

import org.litepal.LitePal;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private List<CarPark> mCarParkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Account.load(getApplicationContext());
        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView);
        TextView tvTest = findViewById(R.id.tv_options);
        TextView tvList = findViewById(R.id.tv_list);

        initLocalData();

        tvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.e("YUN", "创建数据 : " + carPark.toString());
                final String[] items = new String[]{"分享车位", "分享我的位置", "我的收藏", "个人信息", "修改密码", "注销登陆", "退出"};
                AlertDialog alertDialog3 = new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(items, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "点击位置:" + i, Toast.LENGTH_SHORT).show();
                                switch (i) {
                                    case 0:
                                        startActivity(new Intent(MainActivity.this, ShareActivity.class));
                                        break;
                                    case 1:
                                        startActivity(new Intent().setAction("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER"));
                                        break;
                                    case 2:
                                        startActivity(new Intent(MainActivity.this, CollectActivity.class));
                                        break;
                                    case 3:
                                        startActivity(new Intent(MainActivity.this, PersonaInfoActivity.class));
                                        break;
                                    case 4:
                                        startActivity(new Intent(MainActivity.this, UpdatePawActivity.class));
                                        break;
                                    case 5:
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        intent.putExtra("load", "1");
                                        startActivity(intent);
                                        break;
                                    case 6:
                                        System.exit(1);
                                        break;

                                }
                            }
                        })
                        .create();

                WindowManager.LayoutParams wlp = alertDialog3.getWindow().getAttributes();
                wlp.gravity = Gravity.TOP | Gravity.LEFT;
                wlp.x = 50;
                wlp.y = 50;
                alertDialog3.show();

                alertDialog3.getWindow().setLayout(540, 1020);

            }
        });

        mBaiduMap = mMapView.getMap();

        navigateTo();

        initData();

        initOverlay();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("YSH", "地址 :经度" + marker.getPosition().longitude + " , 纬度 :" + marker.getPosition().latitude);
                Intent intent = new Intent(MainActivity.this, CarParkActivity.class);
                intent.putExtra("id", marker.getExtraInfo());
                startActivity(intent);
                return true;
            }
        });
    }

    //初始化本地数据  车场,车位,等信息
    private void initLocalData() {
        //本地有数据就不生成了
        List<CarPark> all = LitePal.findAll(CarPark.class);
        if (all.size() != 0)
            return;

        CarPark carPark1 = new CarPark();
        carPark1.setAddress("剑南大道流浪纪1001号");
        carPark1.setId("cp_01");
        carPark1.setName("动物园");
        carPark1.setDesc("我是描述信息:剑南大道流浪纪1001号");
        carPark1.setPic(R.mipmap.carpark1);
        carPark1.setLatitude(30.502395);
        carPark1.setLongitude(104.089893);
        carPark1.save();

        CarPark carPark2 = new CarPark();
        carPark2.setAddress("手起刀落杀琪玛1002号");
        carPark2.setId("cp_02");
        carPark2.setName("甜品镇");
        carPark2.setDesc("我是描述信息:手起刀落杀琪玛1002号");
        carPark2.setPic(R.mipmap.carpark2);
        carPark2.setLatitude(30.507638);
        carPark2.setLongitude(104.0879980);
        carPark2.save();

        CarPark carPark3 = new CarPark();
        carPark3.setAddress("蜻蜓点水之夜1003号");
        carPark3.setId("cp_03");
        carPark3.setName("赵熊托停车场");
        carPark3.setDesc("我是描述信息:蜻蜓点水之夜1003号");
        carPark3.setPic(R.mipmap.carpark3);
        carPark3.setLatitude(30.489979);
        carPark3.setLongitude(104.084144);
        carPark3.save();

        Parking parking1 = new Parking();
        parking1.setPrice(8.23);
        CarPark carPark = LitePal.where("cid = ?", "cp_01").findFirst(CarPark.class);
        parking1.setCarPark(carPark);
        parking1.setNumber("001");
        parking1.save();

        Parking parking2 = new Parking();
        parking2.setPrice(8.23);
        parking2.setCarPark(carPark);
        parking2.setNumber("002");
        parking2.save();

    }

    private void navigateTo() {
        LatLng ll = new LatLng(30.499890, 104.088196);
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        mBaiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(16);
        mBaiduMap.animateMapStatus(update);
    }

    private void initData() {
        //拿到所有停车场
        mCarParkList = LitePal.findAll(CarPark.class, true);
    }

    private void initOverlay() {

        if (mCarParkList == null || mCarParkList.size() <= 0)
            return;
        // 把查到的停车场标记到地图上
        for (CarPark carPark : mCarParkList) {
            LatLng ll = new LatLng(carPark.getLatitude(), carPark.getLongitude());
            OverlayOptions oo = new MarkerOptions().position(ll).icon(getBd(carPark));
            Bundle bundle = new Bundle();
            bundle.putString("id", carPark.getId());
            mBaiduMap.addOverlay(oo).setExtraInfo(bundle);
        }

    }

    //根据是否占用该车场的车位返回不同的图标
    private BitmapDescriptor getBd(CarPark carPark) {
        boolean temp = false;
        for (Parking parking : carPark.getParkings()) {
            //判断这个这位是否被预约  是就返回预约人,否则 返回null
            Parking cp = LitePal.find(Parking.class, parking.getId(), true);

            Appointment appointment = cp.getAppointment();
            if (appointment == null)
                continue;
            Appointment a = LitePal.find(Appointment.class, appointment.getId(), true);
            User user = a.getUser();
            //不为空代表已经被预约了
            if (user != null) {
                //并且预约人是登陆账户本人
                if (user.getName().equals(Account.getUser().getName())) {
                    temp = true;
                    break;
                }
                //不是本人就遍历下一个
                continue;
            }

            //自己是否在这个停车场停车
            if (Account.getUser() == cp.getUser()) {
                temp = true;
                break;
            }
        }

        if (temp)
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);

        return BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initOverlay();
    }
}
