package com.example.mrapat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.mrapat.MyLibraryes.AbsensiManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class ScannAbsensiActivity extends AppCompatActivity {

    public static final String MY_SCANN_ABSEN_KEY = "MY_SCANN_ABSEN_KEY" ;
    private IntentIntegrator qrScan ;
    private TextView waktuAbsen, ketAbsen, namaRapat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scann_absensi);
        namaRapat = findViewById(R.id.crl_namaRaker);
        waktuAbsen = findViewById(R.id.asa_waktuAbsen);
        ketAbsen = findViewById(R.id.asa_ketAbsen);

        qrScan = new IntentIntegrator(this);
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                try {
                    JSONObject dataQrCode = new JSONObject(result.getContents());
                    String id_raker_qrcode = dataQrCode.getString("raker");
                    String id_raker = getRaker().getString("id") ; //ERROR
                    if(id_raker_qrcode != null && id_raker != null){
                        this.absenHandler(id_raker_qrcode, id_raker);
                        Toast.makeText(getApplicationContext(), "ABSEN BERHASIL", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "TIDAK ADA KODE YANG SESUAI", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "KAMU TELAH KELUAR DARI QR CODE", Toast.LENGTH_LONG).show();
                Intent in = new Intent(getApplicationContext(), ListRakerActivity.class);
                startActivity(in);
                finish();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void absenHandler(String id_raker_qrcode, String id_raker) {
        AbsensiManager.ambilAbsen(getApplicationContext(), new String[]{id_raker, id_raker_qrcode}, new AbsensiManager.Absensi() {
            @Override
            public void myResponse(String data) throws JSONException {
                String nAbsen = new JSONObject(data).getJSONObject("data").getJSONObject("raker").getString("nama_raker");
                String wAbsen = new JSONObject(data).getJSONObject("data").getJSONObject("peserta").getString("tanggal_jam_absensi");
                String kAbsen = new JSONObject(data).getJSONObject("data").getJSONObject("peserta").getString("keterangan_absensi");
                String msgAbsen = new JSONObject(data).getJSONArray("msg").getString(0);
                namaRapat.setText(nAbsen);
                waktuAbsen.setText(wAbsen);
                ketAbsen.setText(kAbsen);
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//                Toast.makeText(getApplicationContext(), msgAbsen.toUpperCase(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void myError(VolleyError err, String msg) {

            }
        });
    }


    private JSONObject getRaker(){
        JSONObject raker = null ;
        try {
            raker = new JSONObject(getIntent().getSerializableExtra(MY_SCANN_ABSEN_KEY).toString());
//            Toast.makeText(getApplicationContext(), raker.toString(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  raker ;
    }
}
