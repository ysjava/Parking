package com.demo.parking;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import com.demo.parking.db.User;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;


/**
 * Created by White paper on 2019/6/14
 * Describe :
 */
public class Account {
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    // 登录的学生ID
    private static String userId;
    // 登录的账户
    private static String account;

    /**
     * 存储数据到XML文件，持久化
     */
    private static void save(Context context) {
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit()
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    /**
     * 删除数据  XML文件
     */
    private static void delete(Context context) {
        // 获取数据持久化的SP
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);
        // 存储数据
        sp.edit()
                .clear()
                .apply();
    }

    /**
     * 进行数据加载
     */
    public static void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Account.class.getName(),
                Context.MODE_PRIVATE);

        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_ACCOUNT, "");
    }

    /**
     * 返回当前账户是否登录
     *
     * @return True已登录
     */
    public static boolean isLogin() {
        // 用户Id
        return !TextUtils.isEmpty(userId);
    }


    /**
     * 保存我自己的信息到持久化XML中
     *
     * @param user user
     */
    public static void login(final User user) {
        Account.account = user.getName();
        Account.userId = String.valueOf(user.getId());
        save(ParkingApplication.getApplication());
    }

    /**
     * 退出操作
     *
     */
//    public static void signOut(Context context, Intent intent) {
//        // 删除xml持续化文件
//        delete(Factory.app());
//        //数据初始化
//        load(context);
//        //删除数据库
//        AppDatabase.delete();
//        //关闭所有activity
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//
//    }


    /**
     * 获取当前登录的用户信息
     *
     * @return User
     */
    public static User getUser() {
        // 如果为null返回一个new的User，其次从数据库查询
        return TextUtils.isEmpty(userId) ? new User() : LitePal.where("id = ?", userId).findFirst(User.class);
    }

}
