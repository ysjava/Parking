package com.demo.parking.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.demo.parking.Account;
import com.demo.parking.R;
import com.demo.parking.db.Collection;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CollectActivity extends AppCompatActivity {
    private List<Collection> collections = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        TextView back = findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        List<Collection> all = LitePal.findAll(Collection.class, true);
        for (Collection c : all) {
            if (c.getUser().getId() == Account.getUser().getId())
                collections.add(c);
        }

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new ColletionAdapter(this, collections));

    }
}
