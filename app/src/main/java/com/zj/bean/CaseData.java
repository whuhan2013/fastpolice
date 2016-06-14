package com.zj.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jjx on 2016/6/6.
 */
public class CaseData implements Serializable{

    public int id;
    public Date updatetime;
    public String name;
    public String telephone;

    public CaseData(int id, Date updatetime, String name, String telephone, String address, double latitude, double longtitude, String description, int dealed, int policeid) {
        this.id = id;
        this.updatetime = updatetime;
        this.name = name;
        this.telephone = telephone;
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.description = description;
        this.dealed = dealed;
        this.policeid = policeid;
    }

    public String address;
    public double latitude;
    public double longtitude;
    public String description;
    public int dealed;
    public int policeid;

    public CaseData()
    {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDealed() {
        return dealed;
    }

    public void setDealed(int dealed) {
        this.dealed = dealed;
    }

    public int getPoliceid() {
        return policeid;
    }

    public void setPoliceid(int policeid) {
        this.policeid = policeid;
    }


}
