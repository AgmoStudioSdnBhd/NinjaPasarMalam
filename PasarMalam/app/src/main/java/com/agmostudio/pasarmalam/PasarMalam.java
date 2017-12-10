package com.agmostudio.pasarmalam;

import com.google.firebase.firestore.GeoPoint;

/**
 * Created by agmostudio on 09/12/2017.
 */

public class PasarMalam {

    private String name;
    private String desc;
    private String hours;
    private GeoPoint coordinate;

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

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public GeoPoint getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoPoint coordinate) {
        this.coordinate = coordinate;
    }
}
