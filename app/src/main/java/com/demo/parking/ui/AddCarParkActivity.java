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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.parking.AddressCheckActivity;
import com.demo.parking.R;
import com.demo.parking.db.CarPark;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.Random;

public class AddCarParkActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView cancel;
    private TextView pic;
    private TextView picSelect;
    private EditText desc;
    private TextView address;
    private EditText name;
    private Button commit;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_park);
        pic = findViewById(R.id.tv_pic);
        picSelect = findViewById(R.id.tv_pic_select);
        desc = findViewById(R.id.et_desc);
        address = findViewById(R.id.tv_address);
        name = findViewById(R.id.et_name);
        commit = findViewById(R.id.btn_commit);
        cancel = findViewById(R.id.tv_back);

        cancel.setOnClickListener(this);
        picSelect.setOnClickListener(this);
        commit.setOnClickListener(this);
        address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pic_select:
                //图片选择
                if (ContextCompat.checkSelfPermission(AddCarParkActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddCarParkActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_commit:
                commitInfo();
                break;
            case R.id.tv_address:
                startActivityForResult(new Intent(AddCarParkActivity.this, AddressCheckActivity.class), 3);
                break;
        }
    }

    //提交信息
    private void commitInfo() {
        if (TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(address.getText().toString()) ||
                TextUtils.isEmpty(desc.getText().toString()) || TextUtils.isEmpty(pic.getText().toString())) {
            Toast.makeText(this, "信息不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (latitude == 0 || longitude == 0) {
            Toast.makeText(this, "地址选择出错,请重新选择地址!", Toast.LENGTH_SHORT).show();
            return;
        }

        CarPark carPark = new CarPark();
        carPark.setAddress(address.getText().toString());
        carPark.setDesc(desc.getText().toString());
        carPark.setName(name.getText().toString());
        carPark.setId(getRandomString(8));
        carPark.setPicStr(pic.getText().toString());

        carPark.setLatitude(latitude);
        carPark.setLongitude(longitude);

        carPark.save();
        finish();
    }

    //length用户要求产生字符串的长度
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
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

//    private void displayImage(String imagePath) {
//        if (imagePath != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            img.setImageBitmap(bitmap);
//        } else {
//            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
//        }
//    }

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
}
