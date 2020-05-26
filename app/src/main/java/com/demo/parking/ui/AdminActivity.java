package com.demo.parking.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.demo.parking.R;
import com.demo.parking.db.CarPark;

import org.litepal.LitePal;

import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recycler;
    private CarParkAdapter adapter;
    private List<CarPark> carParks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView tvBack = findViewById(R.id.tv_back);
        TextView addCarPark = findViewById(R.id.tv_add_car_park);
        recycler = findViewById(R.id.recycler);
        carParks = LitePal.findAll(CarPark.class);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter = new CarParkAdapter(carParks, this));


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addCarPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至添加界面
                startActivity(new Intent(AdminActivity.this, AddCarParkActivity.class));
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        carParks = LitePal.findAll(CarPark.class);
        recycler.setAdapter(adapter = new CarParkAdapter(carParks, AdminActivity.this));
    }
}
