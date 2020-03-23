package com.demo.parking.db;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.litepal.crud.LitePalSupport;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by White paper on 2020/3/14
 * Describe : 评论
 */
public class Comment extends LitePalSupport {
    private long id;
    private String content;
    private LocalDateTime date;
    private String dateStr;
    private User user;
    private CarPark carPark;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.dateStr = date.format(formatter);
        this.date = date;
    }

    public String getDateStr() {
        return dateStr;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }
}
