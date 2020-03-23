package com.demo.parking.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by White paper on 2020/3/14
 * Describe :  收藏
 */
public class Collection extends LitePalSupport {
    private String cid;
    private CarPark carPark;
    private User user;

    public String getId() {
        return cid;
    }

    public void setId(String id) {
        this.cid = id;
    }

    public CarPark getCarPark() {
        return carPark;
    }

    public void setCarPark(CarPark carPark) {
        this.carPark = carPark;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
