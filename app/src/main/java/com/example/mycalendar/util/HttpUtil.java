package com.example.mycalendar.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by 逍遥依尘 on 2018/5/28.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, String json ,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, json);
        //RequestBody requestBody = new FormBody.Builder().add("username","admin").build();
        Request request = new Request.Builder().url(address).post(requestBody).build();
        Log.i("HttpUtil",json);
        Log.i("HttpUtil",requestBody.toString());
        Log.i("HttpUtil",""+requestBody.contentType());
        client.newCall(request).enqueue(callback);
    }

}
