package com.demo.parking.db;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by White paper on 2020/3/14
 * Describe : 停车场
 */
public class CarPark extends LitePalSupport {
    private String cid;
    private String name;
    private String desc;
    private String address;
    private int pic;
    private String picStr;
    private double longitude;//经度
    private double latitude;//维度

    //一个停车场对应多个预约
    private List<Appointment> appointments = new ArrayList<>();
    //车位
    private List<Parking> parkings = new ArrayList<>();

    private List<Collection> collections = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    public String getId() {
        return cid;
    }

    public void setId(String id) {
        this.cid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<Parking> getParkings() {
        return parkings;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public String getPicStr() {
        return picStr;
    }

    public void setPicStr(String picStr) {
        this.picStr = picStr;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }



    public void setParkings(List<Parking> parkings) {
        this.parkings = parkings;
    }



    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


}
