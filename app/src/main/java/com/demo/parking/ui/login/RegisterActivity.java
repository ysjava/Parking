package com.demo.parking.ui.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.parking.CarParkActivity;
import com.demo.parking.R;
import com.demo.parking.db.Parking;
import com.demo.parking.db.User;

import org.litepal.LitePal;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPaw;
    private EditText etConfirm;
    private EditText etPhone;
    private EditText etBirthday;

    private Button sexBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etPaw = findViewById(R.id.et_paw);
        etConfirm = findViewById(R.id.et_confirm);
        etPhone = findViewById(R.id.et_phone);
        etBirthday = findViewById(R.id.et_birthday);

        sexBtn = findViewById(R.id.btn_sex);
        Button submitBtn = findViewById(R.id.btn_submit);
        Button cancelBtn = findViewById(R.id.btn_cancel);

        sexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"男", "女"};
                AlertDialog alertDialog3 = new AlertDialog.Builder(RegisterActivity.this)
                        .setTitle("选择男女")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(items, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sexBtn.setText(items[i]);
                            }
                        })
                        .create();
                alertDialog3.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check()) {
                    Toast.makeText(RegisterActivity.this, "必填项未填或者重复密码输入错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                //注册
                User user = new User();
                user.setName(etName.getText().toString());
                user.setPassword(etPaw.getText().toString());
                user.setPhone(etPhone.getText().toString());
                user.setBirthday(etBirthday.getText().toString());
                user.setGender(sexBtn.getText().toString());
                user.save();
                User u = LitePal.find(User.class,user.getId());
                Log.e("TAB","注册成功,"+u.toString());
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private boolean check() {
        if (TextUtils.isEmpty(etName.getText().toString()) ||
                TextUtils.isEmpty(etConfirm.getText().toString()) ||
                TextUtils.isEmpty(etPaw.getText().toString()) ||
                TextUtils.isEmpty(etPhone.getText().toString()))
            return false;

        return etPaw.getText().toString().equals(etConfirm.getText().toString());
    }
}
