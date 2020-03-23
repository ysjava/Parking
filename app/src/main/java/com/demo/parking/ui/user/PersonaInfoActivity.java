package com.demo.parking.ui.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.parking.Account;
import com.demo.parking.R;
import com.demo.parking.db.User;

import org.litepal.LitePal;

/**
 * Created by White paper on 2020/3/17
 * Describe :
 */
public class PersonaInfoActivity extends AppCompatActivity {
    private EditText etBirthday;
    private EditText etPhone;
    private Button sexBtn;
    private Button submitBtn;
    private Button cancelBtn;
    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        user = Account.getUser();

        sexBtn = findViewById(R.id.btn_sex);
        submitBtn = findViewById(R.id.btn_submit);
        cancelBtn = findViewById(R.id.btn_cancel);
        etPhone = findViewById(R.id.et_phone);
        etBirthday = findViewById(R.id.et_birthday);

        final TextView tvBack = findViewById(R.id.tv_back);
        final TextView tvName = findViewById(R.id.tv_name);
        final TextView tvUpdate = findViewById(R.id.tv_update);

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBtn.setVisibility(View.VISIBLE);
                cancelBtn.setVisibility(View.VISIBLE);
            }
        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvName.setText(user.getName());
        etPhone.setText(user.getPhone());
        etBirthday.setText(user.getBirthday());


        sexBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"男", "女"};
                AlertDialog alertDialog3 = new AlertDialog.Builder(PersonaInfoActivity.this)
                        .setTitle("选择性别")
                        .setIcon(R.mipmap.ic_launcher)
                        .setItems(items, new DialogInterface.OnClickListener() {//添加列表
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sexBtn.setText(items[i]);
                            }
                        })
                        .create();

                WindowManager.LayoutParams wlp=alertDialog3.getWindow().getAttributes();wlp.gravity = Gravity.TOP | Gravity.LEFT;
                wlp.x=380;
                wlp.y=1100;
                alertDialog3.show();

                alertDialog3.getWindow().setLayout(540,500);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    Toast.makeText(PersonaInfoActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //修改
                user.setPhone(etPhone.getText().toString());
                user.setBirthday(etBirthday.getText().toString());
                user.setGender(sexBtn.getText().toString());

                user.update(user.getId());
                User u = LitePal.find(User.class, user.getId());
                Log.e("TAB", "保存成功," + u.toString());
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
}
