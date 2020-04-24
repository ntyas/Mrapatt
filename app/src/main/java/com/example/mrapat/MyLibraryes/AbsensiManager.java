package com.example.mrapat.MyLibraryes;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AbsensiManager {

    public interface Absensi{
        public void myResponse(String data) throws JSONException;
        public void myError(VolleyError err, String msg);
    }

//    private static String base_url = "http://192.168.2.101:8000/api/absensi";
    private static String base_url = "http://mrapat-lite.herokuapp.com/public/api/absensi";

    public static void login(final Context context, final String nip, final Absensi absensi){
        new RequestURL(context, new RequestURL.MyRequest() {
            @Override
            public int getMethod() {
                return Request.Method.POST;
            } //Method yang digunakan POST

            @Override
            public String getUrl() {
                return base_url+"/login";
            } //Url yang digunakan

            @Override
            public Map<String, String> param(Map<String, String> data) {
                //data yang diperlukan untuk pengelolaan vollay
                //yaitu phone key dan nip
                data.put("phone_key", deviceId(context) );
                if(nip != null){
                    data.put("nip", nip);
                }
                return data;
            }

            @Override
            public void response(Object response) {
                //respon pada volley
                try {
                    absensi.myResponse(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(VolleyError error, String dataMsgError) throws JSONException {
                String err = new JSONObject(dataMsgError).getJSONArray("err").getString(0).toString();
                String message = new JSONObject(dataMsgError).getJSONArray("msg").getString(0).toString();
                Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                absensi.myError(error, dataMsgError);
            }
        });
    }

    public static void ambilAbsen(final Context context, final String[] raker ,final Absensi absensi){
        new RequestURL(context, new RequestURL.MyRequest() {
            @Override
            public int getMethod() {
                return Request.Method.POST;
            }

            @Override
            public String getUrl() {
                return base_url;
            }

            @Override
            public Map<String, String> param(Map<String, String> data) {
                data.put("phone_key", deviceId(context) );
                data.put("raker", raker[0]);
                data.put("raker_qrcode", raker[1]);
                return data;
            }

            @Override
            public void response(Object response) {
//                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                try {
                    absensi.myResponse(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void err(VolleyError error, String dataMsgError) throws JSONException {
                String err = new JSONObject(dataMsgError).getJSONArray("err").getString(0).toString();
                String message = new JSONObject(dataMsgError).getJSONArray("msg").getString(0).toString();
                Toast.makeText(context, err, Toast.LENGTH_LONG).show();
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                absensi.myError(error, dataMsgError);
            }
        });
    }

    public static String deviceId(Context context){
        //deviceId yaitu sintax untuk pengambilan id android pada hp
        String deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return  deviceId ;
    }


}
