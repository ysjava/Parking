package com.demo.parking.db;

import org.litepal.crud.LitePalSupport;

import java.util.Calendar;

/**
 * Created by White paper on 2020/3/19
 * Describe :
 */
public class Time extends LitePalSupport {
    private long id;
    private long parkingId;

    private int y, m, d, h, mi, s;

    public Time(long parking) {
        Calendar cal = Calendar.getInstance();
        this.y = cal.get(Calendar.YEAR);
        this.m = cal.get(Calendar.MONTH);
        this.d = cal.get(Calendar.DATE);
        this.h = cal.get(Calendar.HOUR_OF_DAY);
        this.mi = cal.get(Calendar.MINUTE);
        this.s = cal.get(Calendar.SECOND);
        this.parkingId = parking;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getParkingId() {
        return parkingId;
    }

    public void setParkingId(long parkingId) {
        this.parkingId = parkingId;
    }

    public int countTime(Calendar cal) {
        int y, m, d, h, mi, s, time;
        y = cal.get(Calendar.YEAR) - this.y;
        m = cal.get(Calendar.MONTH) - this.m;
        d = cal.get(Calendar.DATE) - this.d;
        h = cal.get(Calendar.HOUR_OF_DAY) - this.h;
        mi = cal.get(Calendar.MINUTE) - this.mi;
        s = cal.get(Calendar.SECOND) - this.s;
        time = y * 365 * 24 * 60 * 60 + m * 30 * 24 * 60 * 60 + d * 24 * 60 * 60 + h * 60 * 60 + mi * 60 + s;

        return time;
    }
}
