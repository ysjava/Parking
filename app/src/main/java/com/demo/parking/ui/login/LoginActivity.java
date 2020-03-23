package com.demo.parking.ui.login;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.parking.Account;
import com.demo.parking.MainActivity;
import com.demo.parking.R;
import com.demo.parking.db.User;
import com.demo.parking.ui.SpalshActivity;

import org.litepal.LitePal;

public class LoginActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        String load = intent.getStringExtra("load");


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.btn_login);
        final Button registerButton = findViewById(R.id.btn_register);
        if (load != null)
            if (load.equals("1"))
                usernameEditText.setText(Account.getUser().getName());

                loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(usernameEditText.getText().toString()) || TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = LitePal.where("name = ? and password = ?", usernameEditText.getText().toString(),
                        passwordEditText.getText().toString()).findFirst(User.class);

                if (user == null) {
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    return;
                }
                //登陆  保存到持久化文件
                Account.login(user);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
