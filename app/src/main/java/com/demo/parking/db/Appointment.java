package com.demo.parking.db;


import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * Created by White paper on 2020/3/14
 * Describe : 预约
 */
public class Appointment extends LitePalSupport {
    private long id;
    private User user;
    private CarPark carPark;
    private Parking parking;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Parking getParking() {
        return parking;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }
}
