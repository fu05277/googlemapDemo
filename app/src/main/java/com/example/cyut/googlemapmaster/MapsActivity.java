package com.example.cyut.googlemapmaster;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cyut.googlemapmaster.Conn.MyCallBack;
import com.example.cyut.googlemapmaster.Conn.MyConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Map介面
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //region 宣告
    private GoogleMap mMap;
    private Context context = this;
    private CyutLocationManager cyutLocationManager;
    private TelephonyManager telephonyManager;
    private ArrayList<CyutMark> arrayList;
    //endregion



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        onInit();
    }

    //region Map載入完成
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(24.0644508,120.717934);
        mMap.setBuildingsEnabled(false);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,6));
        EnableMyLocation();
    }
    //endregion

    //region onResume
    @Override
    protected void onResume() {
        super.onResume();

        //region 開始更新自己位置
        cyutLocationManager.start();
        //endregion
    }
    //endregion

    //region onPause
    @Override
    protected void onPause() {
        super.onPause();
        //region 停止更新自己位置
        cyutLocationManager.stop();
        //endregion
    }
    //endregion

    //region 初始化
    private void onInit(){
        arrayList=new ArrayList<>();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        cyutLocationManager = new CyutLocationManager(this) {
            @Override
            public void locationUpdate(Location location) {
                super.locationUpdate(location);
                MyConnection.OnLatLngUpdate(context,getIMEI(),getName(),location,new MyCallBack(){
                    @Override
                    public void onSuccess(JSONObject response) {
                        getDBdata(response);
                    }
                });
            }
        };
        cyutLocationManager.setUpdateInterval(10);
    }
    //endregion

    //region 開啟回到自己位置
    private void EnableMyLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
    //endregion

    //region 取得IMEI
    private String getIMEI() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        }
        return telephonyManager.getDeviceId();
    }
    //endregion

    //region 取得姓名
    private String getName(){
        Bundle bundle = getIntent().getExtras();
        return bundle.getString("name");
    }
    //endregion

    //region 取得資料庫資料
    private void getDBdata(JSONObject response){
        try {
            JSONArray jsonArray = response.getJSONArray("data");
            CyutMark cyutMark;
            for(int i = 0; i<jsonArray.length();i++){
                cyutMark=new CyutMark();
                cyutMark.IMEI=jsonArray.getJSONObject(i).getString("IMEI");
                cyutMark.name=jsonArray.getJSONObject(i).getString("name");
                cyutMark.Lat=jsonArray.getJSONObject(i).getString("Lat");
                cyutMark.Lng=jsonArray.getJSONObject(i).getString("Lng");
                updateList(cyutMark);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region 更新列表
    private void updateList(CyutMark cyutMark){
        for (CyutMark item: arrayList) {
            if(item.IMEI.equals(cyutMark.IMEI)){
                if(item.name.equals(cyutMark.name)){
                    item.Lat=cyutMark.Lat;
                    item.Lng=cyutMark.Lng;
                    item.marker.setPosition(new LatLng(Double.parseDouble(cyutMark.Lat),Double.parseDouble(cyutMark.Lng)));
                    return;
                }
            }
        }
        cyutMark.marker=setMarker(cyutMark);
        arrayList.add(cyutMark);

    }
    //endregion

    //region 設定Maker
    private Marker setMarker(CyutMark cyutMark){
        LatLng sydney = new LatLng(Double.parseDouble(cyutMark.Lat),Double.parseDouble(cyutMark.Lng));
        return mMap.addMarker(new MarkerOptions().position(sydney).title(cyutMark.name));
    }
    //endregion

}
