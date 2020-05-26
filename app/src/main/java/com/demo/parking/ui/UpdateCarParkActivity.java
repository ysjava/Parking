package com.demo.parking.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.demo.parking.AddressCheckActivity;
import com.demo.parking.R;
import com.demo.parking.db.CarPark;
import com.demo.parking.db.Parking;

import org.litepal.LitePal;

import java.text.MessageFormat;

public class UpdateCarParkActivity extends AppCompatActivity {
    private TextView back;
    private TextView pic;
    private TextView add;
    private TextView del;
    private TextView picSelect;
    private TextView number;
    private EditText name;
    private EditText desc;
    private TextView address;
    private EditText etNumber;
    private EditText etPrice;
    private Button commit;
    private CarPark carPark;
    private LinearLayout layout;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car_park);


        String id = getIntent().getStringExtra("id");

        carPark = LitePal.where("cid = ?", id).findFirst(CarPark.class, true);
        latitude = carPark.getLatitude();
        longitude = carPark.getLongitude();

        back = findViewById(R.id.tv_back);
        pic = findViewById(R.id.tv_pic);
        add = findViewById(R.id.tv_parking_add);
        picSelect = findViewById(R.id.tv_pic_select);
        number = findViewById(R.id.tv_number);
        name = findViewById(R.id.et_name);
        desc = findViewById(R.id.et_desc);
        address = findViewById(R.id.et_address);
        commit = findViewById(R.id.btn_commit);
        layout = findViewById(R.id.lay_parking);
        etNumber = findViewById(R.id.et_number);
        etPrice = findViewById(R.id.et_price);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etNumber.getText().toString()) ||
                        TextUtils.isEmpty(etPrice.getText().toString())) {
                    Toast.makeText(UpdateCarParkActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                Parking parking = LitePal.where("parkId = ? and number = ?", carPark.getId(), etNumber.getText().toString())
                        .findFirst(Parking.class);
                if (parking != null) {
                    Toast.makeText(UpdateCarParkActivity.this, "编号以及存在,请重新输入!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //添加车位
                parking = new Parking();
                parking.setPrice(Double.parseDouble(etPrice.getText().toString()));
                parking.setCarPark(carPark);
                parking.setNumber(etNumber.getText().toString());
                parking.save();
                number.setText(MessageFormat.format("车位 : {0}个", carPark.getParkings().size()));
                View view = LayoutInflater.from(UpdateCarParkActivity.this).inflate(R.layout.cell_parking, layout, false);
                TextView number = view.findViewById(R.id.tv_number);
                TextView price = view.findViewById(R.id.tv_price);
//            TextView zan = view.findViewById(R.id.tv_zan);
                number.setText(String.format("编号 : %s", parking.getNumber()));
                price.setText(String.format("价格 : %s", String.valueOf(parking.getPrice())));
                layout.addView(view);
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //地址选择
                startActivityForResult(new Intent(UpdateCarParkActivity.this, AddressCheckActivity.class), 3);
            }
        });
        picSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //图片选择
                if (ContextCompat.checkSelfPermission(UpdateCarParkActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateCarParkActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(address.getText().toString()) ||
                        TextUtils.isEmpty(desc.getText().toString()) || TextUtils.isEmpty(pic.getText().toString())) {
                    Toast.makeText(UpdateCarParkActivity.this, "信息不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (latitude == 0 || longitude == 0) {
                    Toast.makeText(UpdateCarParkActivity.this, "地址选择出错,请重新选择地址!", Toast.LENGTH_SHORT).show();
                    return;
                }
                carPark.setAddress(address.getText().toString());
                carPark.setDesc(desc.getText().toString());
                carPark.setName(name.getText().toString());

                carPark.setPicStr(TextUtils.isEmpty(carPark.getPicStr()) ? null : pic.getText().toString());

                carPark.setLatitude(latitude);
                carPark.setLongitude(longitude);

                carPark.save();
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        show(carPark);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 2:
                if (resultCode == RESULT_OK) {
                    handleImage(data);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    //处理选择后的地址信息
                    String address = data.getStringExtra("address");
                    latitude = data.getDoubleExtra("latitude", 0);
                    longitude = data.getDoubleExtra("longitude", 0);

                    this.address.setText(address);
                }
                break;
        }
    }

    private void handleImage(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
//        displayImage(imagePath);//显示图片
        //显示选择图片地址
        pic.setText(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;

        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void show(CarPark carPark) {
        pic.setText(TextUtils.isEmpty(carPark.getPicStr()) ? String.valueOf(carPark.getPic()) : carPark.getPicStr());
        name.setText(carPark.getName());
        desc.setText(carPark.getDesc());
        address.setText(carPark.getAddress());
        number.setText(MessageFormat.format("车位 : {0}个", carPark.getParkings().size()));
        layout.removeAllViews();
        for (Parking parking : carPark.getParkings()) {
            View view = LayoutInflater.from(this).inflate(R.layout.cell_parking, layout, false);
            TextView number = view.findViewById(R.id.tv_number);
            TextView price = view.findViewById(R.id.tv_price);
//            TextView zan = view.findViewById(R.id.tv_zan);
            number.setText(String.format("编号 : %s", parking.getNumber()));
            price.setText(String.format("价格 : %s", String.valueOf(parking.getPrice())));
            layout.addView(view);
        }

    }
}
