package com.demo.parking.db;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * Created by White paper on 2020/3/14
 * Describe : 车位
 */
public class Parking extends LitePalSupport {
    private long id;
    private String number;//编号
    private double price;
    private boolean occupy;
    private String parkId;
    private CarPark park;
    private User user;
    private Appointment appointment;

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CarPark getCarPark() {
        return park;
    }

    public void setCarPark(CarPark carPark) {
        this.parkId = carPark.getId();
        this.park = carPark;
    }

    public boolean isOccupy() {
        return occupy;
    }

    public void setOccupy(boolean occupy) {
        this.occupy = occupy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CarPark getPark() {
        return park;
    }

    public void setPark(CarPark park) {
        this.park = park;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }


}
