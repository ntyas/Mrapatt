package com.example.mrapat.MyLibraryes;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RequestURL {
    public interface MyRequest {
        public int getMethod();
        public String getUrl() ;
        public Map<String, String> param(Map<String, String> data);
        public void response(Object response) throws JSONException;
        public void err(VolleyError error, String dataMsgError) throws JSONException;
    }

    private MyRequest myRequest ;
    private Context context ;

    public RequestURL(Context context, final MyRequest myRequest){
        this.myRequest = myRequest ;
        this.context = context ;

        Volley.newRequestQueue(this.context).add(new StringRequest(
                myRequest.getMethod(),
                myRequest.getUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            myRequest.response(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        String dataMsgError = null ;
                        if (error instanceof ServerError && response != null) {
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                dataMsgError = res;
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                e1.printStackTrace();
                            }
                        }
                        try {
                            myRequest.err(error, dataMsgError);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<String, String>();
                return myRequest.param(data);
            }
        });
    }
}
