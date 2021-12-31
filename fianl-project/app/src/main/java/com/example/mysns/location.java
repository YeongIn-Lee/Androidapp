package com.example.mysns;

import java.io.Serializable;

public class location implements Serializable {
    private String x;
    private String y;
    private String newx;
    private String newy;
    private String address;

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNewx() {
        return newx;
    }

    public void setNewx(String newx) {
        this.newx = newx;
    }

    public String getNewy() {
        return newy;
    }

    public void setNewy(String newy) {
        this.newy = newy;
    }
}

