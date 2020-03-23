package com.demo.parking.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.demo.parking.R;
import com.demo.parking.db.Collection;

import java.util.List;

/**
 * Created by White paper on 2020/3/18
 * Describe :
 */
public class ColletionAdapter extends ArrayAdapter<Collection> {

    private List<Collection> dataList;
    private ArrayAdapter<Collection> arrayAdapter;

    public ColletionAdapter(@NonNull Context context, List<Collection> list) {
        super(context, 0, list);
        arrayAdapter = this;
        dataList = list;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Nullable
    @Override
    public Collection getItem(int position) {
        return dataList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Collection collection = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_c, parent, false);
        ImageView pic = view.findViewById(R.id.iv_pic);
        TextView name = view.findViewById(R.id.tv_name);
        TextView desc = view.findViewById(R.id.tv_desc);
        ImageView delete = view.findViewById(R.id.iv_delete);
        pic.setImageResource(collection.getCarPark().getPic());

        name.setText(collection.getCarPark().getName());
        desc.setText(collection.getCarPark().getDesc());
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collection.delete();
                dataList.remove(collection);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        return view;

    }
}
