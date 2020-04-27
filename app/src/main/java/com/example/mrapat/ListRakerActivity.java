package com.example.mrapat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.mrapat.Adapter.RakerAdapter;
import com.example.mrapat.MyLibraryes.AbsensiManager;
import com.example.mrapat.MyLibraryes.RequestURL;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ListRakerActivity extends AppCompatActivity {

    //JAVA UNTUK MENGATUR RECYCLERVIEW LIST RAPAT
    Context context;
    public static final String MY_LIST_RAKER_KEY = "MY_LIST_RAKER_KEY";
    RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;
    private String nama,nip;
//    private String deviceId = android.provider.Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    private String deviceId ;
    private Bundle activitySavedInstanceState ;
    Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_raker);

        deviceId = AbsensiManager.deviceId(getApplicationContext());
        activitySavedInstanceState = savedInstanceState ;

        recyclerView = findViewById(R.id.alr_recycleView);
        this.setListAdapterHandler();
        swipeRefreshLayout = findViewById(R.id.swlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorCoral, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { //mengatur listener yang akan dijalankan saat layar refresh
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() { //mengatur jeda/delay

                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }, 300);
            }
        });

        //Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        RequestURL requestURL = new RequestURL(this.getApplicationContext(), new RequestURL.MyRequest() {
            @Override
            public int getMethod() {
                return Request.Method.GET;
            }

            @Override
            public String getUrl() {
                return "http://mrapat-lite.herokuapp.com/public/api/data/profile_karyawan/"+deviceId;
            }

            @Override
            public Map<String, String> param(Map<String, String> data) {
                return null;
            }

            @Override
            public void response(Object response) throws JSONException {

                nama = new JSONObject(String.valueOf(response)).getJSONObject("data").getString("nama_pegawai");
                nip = new JSONObject(String.valueOf(response)).getJSONObject("data").getString("nip_pegawai");

                //NavigationDrawer
                headerNavigationLeft = new AccountHeader()
                        .withActivity(ListRakerActivity.this)
                        .withCompactStyle(false)
                        .withSavedInstance(activitySavedInstanceState)
                        .withHeaderBackground(R.drawable.latarr)
                        .addProfiles(
                                new ProfileDrawerItem().withIcon(String.valueOf(R.drawable.ic_perm_identity_black)).withName(nama).withEmail(nip)
                        )
                        .build();

                navigationDrawerLeft = new Drawer()
                        .withActivity(ListRakerActivity.this)
                        .withToolbar(toolbar)
                        .withDisplayBelowToolbar(false)
                        .withActionBarDrawerToggleAnimated(true)
                        .withDrawerGravity(Gravity.LEFT)
                        .withSavedInstance(activitySavedInstanceState)
                        .withAccountHeader(headerNavigationLeft)
                        .withSliderBackgroundColorRes(R.color.md_grey_200)
                        .withStatusBarColorRes(R.color.md_grey_200)
                        .build();

                navigationDrawerLeft.addItem(new SecondaryDrawerItem().withName("Contact Us for more information -STI-"));
            }

            @Override
            public void err(VolleyError error, String dataMsgError) throws JSONException {

            }
        });
}



    private void setListAdapterHandler() {
        //handeler untuk recyclerview
        RakerAdapter rakerAdapter = new RakerAdapter(this.getDataRaker()); //pemanggilan adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rakerAdapter);
    }

    private JSONArray getDataRaker() {
        JSONArray getDataRaker = null ;
        try {
            getDataRaker = new JSONArray(getIntent().getSerializableExtra(MY_LIST_RAKER_KEY).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDataRaker ;
    }
}
