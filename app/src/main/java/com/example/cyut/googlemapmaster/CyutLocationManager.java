package com.example.cyut.googlemapmaster;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * Created by CYUT on 2018/6/20.
 * 位置更新監聽器
 */

public class CyutLocationManager {

    //region 宣告
    private int UPDATE_INTERVAL_IN_SECONDS = 10;
    private  int MILLISECONDS_PER_SECOND = 1000;
    private  long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    private  int FASTEST_INTERVAL_IN_SECONDS = 10;
    private  long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private Context context;
    //endregion


    //region 建構子
    CyutLocationManager(Context context){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    locationUpdate(location);
                   // Log.e("cyut", String.valueOf(location.toString()));
                }
            }
        };
        this.context=context;
    }
    //endregion

    //region 位置更新監聽器方法
    public void locationUpdate(Location location){
    }
    //endregion

    //region 開始偵測
    public void start() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);

    }
    //endregion

    //region 停止偵測
    public void stop(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }
    //endregion

    //region 設定間格時間
    public void setUpdateInterval(int i){
        UPDATE_INTERVAL_IN_SECONDS=i;
    }

    public void setFastestInterval(int i){
        FASTEST_INTERVAL_IN_SECONDS=i;
    }
    //endregion



}
