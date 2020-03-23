package com.demo.parking.ui.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.parking.Account;
import com.demo.parking.R;
import com.demo.parking.db.User;

import org.litepal.LitePal;

public class UpdatePawActivity extends AppCompatActivity {
    private EditText etOldPaw;
    private EditText etNewPaw;
    private EditText etNewPawc;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_paw);
        user = Account.getUser();

        Button submitBtn = findViewById(R.id.btn_submit);
        Button cancelBtn = findViewById(R.id.btn_cancel);

        etOldPaw = findViewById(R.id.et_old_pwd);
        etNewPaw = findViewById(R.id.et_new_paw);
        etNewPawc = findViewById(R.id.et_new_paw_c);

        final TextView tvBack = findViewById(R.id.tv_back);
        final TextView tvName = findViewById(R.id.tv_name);


        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvName.setText(String.format("用户名:%s", user.getName()));


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check()) {
                    Toast.makeText(UpdatePawActivity.this, "初始密码错误或重复密码没输对", Toast.LENGTH_SHORT).show();
                    return;
                }

                //修改
                user.setPassword(etNewPaw.getText().toString());

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

    private boolean check() {
        if (!user.getPassword().equals(etOldPaw.getText().toString()))
            return false;

        return etNewPaw.getText().toString().equals(etNewPawc.getText().toString());
    }
}
