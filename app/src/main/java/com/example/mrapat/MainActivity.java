package com.example.mrapat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.mrapat.MyLibraryes.AbsensiManager;
import com.example.mrapat.MyLibraryes.RequestURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AbsensiManager.login(this, null, new AbsensiManager.Absensi() {
            @Override
            public void myResponse(String data) throws JSONException {
                String dataListRaker = new JSONObject(data).getJSONObject("data").getJSONArray("raker").toString();
                Intent in = new Intent(getApplicationContext(), ListRakerActivity.class);
                in.putExtra(ListRakerActivity.MY_LIST_RAKER_KEY, dataListRaker);
                startActivity(in);
                finish();
            }

            @Override
            public void myError(VolleyError err, String msg) {
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                finish();
            }
        });

    }
}
