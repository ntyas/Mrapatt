package com.example.mrapat.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mrapat.R;
import com.example.mrapat.ScannAbsensiActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RakerAdapter extends RecyclerView.Adapter<RakerAdapter.MyHolder> {

    JSONArray rakerArray ;

    public RakerAdapter(JSONArray rakerArray){
        this.rakerArray = rakerArray ;
    }

    @NonNull
    @Override
    public RakerAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_raker_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RakerAdapter.MyHolder holder, int position) {
        try {
            final JSONObject result = rakerArray.getJSONObject(position);
            String namaRaker = result.getString("nama_raker") ;
            String waktuRaker = result.getString("tanggal_jam_masuk_raker")+" s/d "+result.getString("tanggal_jam_keluar_raker");
            holder.namaRaker.setText(namaRaker);
            holder.waktuRaker.setText(waktuRaker);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(v.getContext(), ScannAbsensiActivity.class);
                    in.putExtra(ScannAbsensiActivity.MY_SCANN_ABSEN_KEY, result.toString());
                    ((Activity) v.getContext()).startActivity(in);
                }
            });
            Boolean value= result.getBoolean("status_absensi");
            if (value){

                holder.statusRapat.setText("Telah Absen ");
                holder.statusRapat.setTextColor(ColorStateList.valueOf(Color.GREEN));

            }else{
                holder.statusRapat.setText("Belum Absen ");
                holder.statusRapat.setTextColor(ColorStateList.valueOf(Color.RED));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rakerArray.length();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView namaRaker, waktuRaker, statusRapat;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            namaRaker = itemView.findViewById(R.id.crl_namaRaker);
            waktuRaker = itemView.findViewById(R.id.crl_waktuRaker);
            statusRapat = itemView.findViewById(R.id.status);

        }
    }
}
