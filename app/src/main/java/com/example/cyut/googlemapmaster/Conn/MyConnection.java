package com.example.cyut.googlemapmaster.Conn;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.example.cyut.googlemapmaster.Conn.BaseAsyncHttpClient;
import com.example.cyut.googlemapmaster.Conn.MyCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by CYUT on 2018/6/7.
 */

public class MyConnection extends BaseAsyncHttpClient {

    //網址
    private static String SIGNATURE_UPLOAD ="http://fleo.tk/updateLocation.php";
    private static String TAG ="MainConnect";
    //自定義方法
    public static void OnLatLngUpdate(final Context context,String IMEI,String name, Location location, final MyCallBack callback) { //登入
        Log.d(TAG, "OnLatLngUpdate");
        try {
            //等待圖示
            final ProgressDialog mLoginDialog = new ProgressDialog(context);

            //要傳的資料
            RequestParams params = new RequestParams();
            params.put("Lat",location.getLatitude());
            params.put("Lng",location.getLongitude());
            params.put("IMEI",IMEI);
            params.put("name",name);
            //post語法
            mClient.post(context, SIGNATURE_UPLOAD, params, new JsonHttpResponseHandler() {
                //當成功連線的語法
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d(TAG, "OnLatLngUpdate - onSuccess");
                    super.onSuccess(statusCode, headers, response);
                    mLoginDialog.cancel();
                    boolean status = false;
                    callback.onSuccess(response);
                    Log.e("ziona",response.toString());
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.d(TAG, "OnLatLngUpdate - onFailure");
                    try {
                    } catch (Exception e) {
                        Log.d(TAG, "OnLatLngUpdate - onFailure: " + e.getMessage());
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d(TAG, "OnLatLngUpdate - onFailure");
                    try {
                        mLoginDialog.dismiss();
                    } catch (Exception e) {
                        Log.d(TAG, "OnLatLngUpdate - onFailure: " + e.getMessage());
                    }

                }
            });
        } catch (Exception e) {
            Log.d(TAG, "OnLatLngUpdate: " + e.getMessage());
        }
    }

}
