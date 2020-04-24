package com.example.mrapat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.mrapat.MyLibraryes.AbsensiManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText nip ;
    Button btnSimpan ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nip = findViewById(R.id.al_nip);
        btnSimpan = findViewById(R.id.al_btnSimpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nipPegawai = nip.getText().toString() ;
                AbsensiManager.login(getApplicationContext(), nipPegawai, new AbsensiManager.Absensi() {

                    @Override
                    public void myResponse(String data) throws JSONException {
                        String dataListRaker = new JSONObject(data).getJSONObject("data").getJSONArray("raker").toString();
                        String msg = new JSONObject(data).getJSONArray("msg").getString(0).toUpperCase();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent in = new Intent(getApplicationContext(), ListRakerActivity.class);
                        in.putExtra(ListRakerActivity.MY_LIST_RAKER_KEY, dataListRaker);
                        startActivity(in);
                        finish();
                    }

                    @Override
                    public void myError(VolleyError err, String msg) {
                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(in);
                        finish();
                    }
                });
            }
        });
    }
}
