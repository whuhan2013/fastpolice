package com.zj.fastpolice;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.zj.base.BaseActivity;
import com.zj.utils.ThemeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ReportActivity extends BaseActivity implements View.OnClickListener {

    private MapView mapview;
    private BaiduMap bdMap;

    private LocationClient locationClient;
    private BDLocationListener locationListener;
    private BDNotifyListener notifyListener;

    private double longitude;// 精度
    private double latitude;// 维度
    private float radius;// 定位精度半径，单位是米
    private String addrStr;// 反地理编码
    private String province;// 省份信息
    private String city;// 城市信息
    private String district;// 区县信息
    private float direction;// 手机方向信息

    private int locType;

    // 定位按钮
    private Button locateBtn;
    // 定位模式 （普通-跟随-罗盘）
    private MyLocationConfiguration.LocationMode currentMode;
    // 定位图标描述
    private BitmapDescriptor currentMarker = null;
    // 记录是否第一次定位
    private boolean isFirstLoc = true;

    //振动器设备
    private Vibrator mVibrator;

    @Bind(R.id.report_icon)ImageView reporticon;

    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case 0:
                    Toast.makeText(ReportActivity.this,addrStr,Toast.LENGTH_LONG).show();
                    Log.i("location", "heree" + addrStr);

                    new MaterialDialog.Builder(ReportActivity.this)
                            .title("定位")
                            .icon(new IconicsDrawable(ReportActivity.this)
                                    .color(ThemeUtils.getThemeColor(ReportActivity.this, R.attr.colorPrimary))
                                    .icon(MaterialDesignIconic.Icon.gmi_my_location)
                                    .sizeDp(20))
                            .content("您当前位于"+addrStr+"，与报案地址是否一致？")
                            .positiveText("是").onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            Intent intent=new Intent(ReportActivity.this,SendPoliceActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putParcelable("location",new LatLng(latitude,longitude));
                            bundle.putString("address", addrStr);
                            intent.putExtra("mybundle",bundle);
                            startActivity(intent);
                            //Toast.makeText(ReportActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        }
                    }).negativeText("否").onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {
                            //Toast.makeText(ReportActivity.this, "failed", Toast.LENGTH_SHORT).show();


                        }
                    })
                            .show();



                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        mapview = (MapView) findViewById(R.id.bd_mapview);
        bdMap = mapview.getMap();
        //locateBtn = (Button) findViewById(R.id.locate_btn);
        //locateBtn.setOnClickListener(this);
        currentMode = MyLocationConfiguration.LocationMode.NORMAL;
        //locateBtn.setText("定位");
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        reporticon.setImageDrawable(new IconicsDrawable(this).color(Color.WHITE).icon(MaterialDesignIconic.Icon.gmi_my_location).sizeDp(20));
        init();

        //initGEO();

        bdMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Toast.makeText(getApplication(),latLng.longitude+","+latLng.latitude,Toast.LENGTH_SHORT).show();
                initGEO(latLng);
            }

            @Override
            public boolean onMapPoiClick(final MapPoi mapPoi) {
                new MaterialDialog.Builder(ReportActivity.this)
                        .title("定位")
                        .icon(new IconicsDrawable(ReportActivity.this)
                                .color(ThemeUtils.getThemeColor(ReportActivity.this, R.attr.colorPrimary))
                                .icon(MaterialDesignIconic.Icon.gmi_my_location)
                                .sizeDp(20))
                        .content("您选择的是:"+mapPoi.getName()+"，与报案地址是否一致？")
                        .positiveText("是").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Intent intent=new Intent(ReportActivity.this,SendPoliceActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putParcelable("location",mapPoi.getPosition());
                        bundle.putString("address", mapPoi.getName());
                        intent.putExtra("mybundle",bundle);
                        startActivity(intent);
                        //Toast.makeText(ReportActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                    }
                }).negativeText("否").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        //Toast.makeText(ReportActivity.this, "failed", Toast.LENGTH_SHORT).show();


                    }
                })
                        .show();
                //Toast.makeText(getApplication(),mapPoi.getName(),Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    public void initGEO(LatLng latLng)
    {
        GeoCoder mSearch=GeoCoder.newInstance();

        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                }
                //获取地理编码结果

            }

            @Override
            public void onGetReverseGeoCodeResult(final ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                }
                //获取反向地理编码结果
                new MaterialDialog.Builder(ReportActivity.this)
                        .title("定位")
                        .icon(new IconicsDrawable(ReportActivity.this)
                                .color(ThemeUtils.getThemeColor(ReportActivity.this, R.attr.colorPrimary))
                                .icon(MaterialDesignIconic.Icon.gmi_my_location)
                                .sizeDp(20))
                        .content("您选择的是"+result.getAddress()+"，与报案地址是否一致？")
                        .positiveText("是").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Intent intent=new Intent(ReportActivity.this,SendPoliceActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putParcelable("location",result.getLocation());
                        bundle.putString("address", result.getAddress());
                        intent.putExtra("mybundle",bundle);
                        startActivity(intent);
                        //Toast.makeText(ReportActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                    }
                }).negativeText("否").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        //Toast.makeText(ReportActivity.this, "failed", Toast.LENGTH_SHORT).show();


                    }
                })
                        .show();
                //Toast.makeText(getApplication(),result.getAddress(),Toast.LENGTH_SHORT).show();
            }
        };
        ReverseGeoCodeOption reverseGeoCodeOption=new ReverseGeoCodeOption();

        mSearch.setOnGetGeoCodeResultListener(listener);
        mSearch.reverseGeoCode(reverseGeoCodeOption.location(latLng));
    }



    /**
     *
     */
    private void init() {
        bdMap.setMyLocationEnabled(true);
        // 1. 初始化LocationClient类
        locationClient = new LocationClient(getApplicationContext());
        // 2. 声明LocationListener类
        locationListener = new MyLocationListener();
        // 3. 注册监听函数
        locationClient.registerLocationListener(locationListener);
        // 4. 设置参数
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        locOption.setCoorType("bd09ll");// 设置定位结果类型
        locOption.setScanSpan(2000);// 设置发起定位请求的间隔时间,ms
        locOption.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        locOption.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向

        locationClient.setLocOption(locOption);
        // 5. 注册位置提醒监听事件
        notifyListener = new MyNotifyListener();
        notifyListener.SetNotifyLocation(longitude, latitude, 3000, "bd09ll");//精度，维度，范围，坐标类型
        locationClient.registerNotify(notifyListener);

        // 6. 开启/关闭 定位SDK
        locationClient.start();
        // locationClient.stop();
        // 发起定位，异步获取当前位置，因为是异步的，所以立即返回，不会引起阻塞
        // 定位的结果在ReceiveListener的方法onReceive方法的参数中返回。
        // 当定位SDK从定位依据判定，位置和上一次没发生变化，而且上一次定位结果可用时，则不会发生网络请求，而是返回上一次的定位结果。
        // 返回值，0：正常发起了定位 1：service没有启动 2：没有监听函数
        // 6：两次请求时间太短（前后两次请求定位时间间隔不能小于1000ms）
		/*
		 * if (locationClient != null && locationClient.isStarted()) {
		 * requestResult = locationClient.requestLocation(); } else {
		 * Log.d("LocSDK5", "locClient is null or not started"); }
		 */

    }

    /**
     *
     * @author ys
     *
     */
    class MyLocationListener implements BDLocationListener {
        // 异步返回的定位结果
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            locType = location.getLocType();
            //Toast.makeText(ReportActivity.this, "当前定位的返回值是：" + locType, Toast.LENGTH_SHORT).show();

            longitude = location.getLongitude();
            latitude = location.getLatitude();
            if (location.hasRadius()) {// 判断是否有定位精度半径
                radius = location.getRadius();
            }
            if (locType == BDLocation.TypeGpsLocation) {//
                Toast.makeText(
                        ReportActivity.this,
                        "当前速度是：" + location.getSpeed() + "~~定位使用卫星数量："
                                + location.getSatelliteNumber(),
                        Toast.LENGTH_SHORT).show();
            } else if (locType == BDLocation.TypeNetWorkLocation) {
                addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
//                Toast.makeText(ReportActivity.this, addrStr,
//                        Toast.LENGTH_SHORT).show();
            }
            direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
            province = location.getProvince();// 省份
            city = location.getCity();// 城市
            district = location.getDistrict();// 区县
//            Toast.makeText(ReportActivity.this,
//                    province + "~" + city + "~" + district, Toast.LENGTH_SHORT)
//                    .show();
            if (isFirstLoc)
            {
                Message message=new Message();
                message.what=0;
                Log.i("location","isFirst"+addrStr);
                handler.sendMessageDelayed(message,2000);

                // 构造定位数据
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(radius)//
                        .direction(direction)// 方向
                        .latitude(latitude)//
                        .longitude(longitude)//
                        .build();
                // 设置定位数据
                bdMap.setMyLocationData(locData);
                LatLng ll = new LatLng(latitude, longitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
                bdMap.animateMapStatus(msu);


            }

            isFirstLoc=false;


        }
    }

    /**
     * 位置提醒监听器
     * @author ys
     *
     */
    class MyNotifyListener extends BDNotifyListener {
        @Override
        public void onNotify(BDLocation bdLocation, float distance) {
            super.onNotify(bdLocation, distance);
            mVibrator.vibrate(1000);//振动提醒已到设定位置附近
            Toast.makeText(ReportActivity.this, "震动提醒", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.locate_btn:// 定位

//                new MaterialDialog.Builder(getApplication())
//                        .title("地址")
//                        .content("您当前位于"+addrStr)
//                        .positiveText("是")
//                        .negativeText("否").onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        Toast.makeText(ReportActivity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
//                    }
//                }).onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        Toast.makeText(getApplication(),"failed",Toast.LENGTH_SHORT).show();
//                    }
//                }).show();


//                new MaterialDialog.Builder(this)
//                        .title("定位")
//                        .icon(new IconicsDrawable(this)
//                                .color(ThemeUtils.getThemeColor(this, R.attr.colorPrimary))
//                                .icon(MaterialDesignIconic.Icon.gmi_my_location)
//                                .sizeDp(20))
//                        .content("您当前位于"+addrStr+"，与报案地址是否一致？")
//                        .positiveText("是").onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        Toast.makeText(ReportActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
//                    }
//                }).negativeText("否").onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(MaterialDialog dialog, DialogAction which) {
//                        Toast.makeText(ReportActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
//
//
//                    }
//                })
//                        .show();

                //break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
        locationClient.unRegisterLocationListener(locationListener);
        //取消位置提醒
        locationClient.removeNotifyEvent(notifyListener);
        locationClient.stop();
    }
}
