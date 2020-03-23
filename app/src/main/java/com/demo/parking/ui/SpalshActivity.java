package com.demo.parking.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.demo.parking.Account;
import com.demo.parking.MainActivity;
import com.demo.parking.R;
import com.demo.parking.ui.login.LoginActivity;

public class SpalshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        Account.load(getApplicationContext());
        if (!Account.isLogin()) {
            startActivity(new Intent(SpalshActivity.this, LoginActivity.class));
        }else {
            startActivity(new Intent(SpalshActivity.this, MainActivity.class));
        }
    }
}
