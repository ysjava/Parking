package com.demo.parking.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.parking.R;
import com.demo.parking.db.CarPark;

import org.litepal.LitePal;

import java.util.List;

/**
 * Created by White paper on 2020/5/25
 * Describe :
 */
public class CarParkAdapter extends RecyclerView.Adapter<CarParkAdapter.ViewHolder> {
    private List<CarPark> carParks;
    private AdminActivity activity;

    public CarParkAdapter(List<CarPark> carParks, AdminActivity activity) {
        this.carParks = carParks;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_park, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (TextUtils.isEmpty(carParks.get(position).getPicStr())) {
            holder.pic.setImageResource(carParks.get(position).getPic());
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(carParks.get(position).getPicStr());
            holder.pic.setImageBitmap(bitmap);
        }

        holder.name.setText(carParks.get(position).getName());
        holder.desc.setText(carParks.get(position).getDesc());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
                carParks.get(position).delete();
                carParks.remove(carParks.get(position));
                //刷新
                notifyDataSetChanged();
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转至更新界面
                Intent intent = new Intent(activity, UpdateCarParkActivity.class);
                intent.putExtra("id", carParks.get(position).getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carParks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name;
        TextView desc;
        Button del;
        Button update;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.iv_pic);
            name = itemView.findViewById(R.id.tv_name);
            desc = itemView.findViewById(R.id.tv_desc);
            del = itemView.findViewById(R.id.btn_delete);
            update = itemView.findViewById(R.id.btn_update);
        }
    }
}
