package com.example.myapplication.bean;

public class Reminder {
    private int id;
    private String rseldate;
    private String  rcontent;
    private String  rcategory;
    private String  ralertime;
    private String  rimg;
    private int status;
    private String raddtime;

    public int getrId() {
        return id;
    }

    public void setrId(int id) {
        this.id = id;
    }

    public String getrSeldate() {
        return rseldate;
    }

    public void setrSeldate(String seldate) {
        this.rseldate = rseldate;
    }

    public String getrContent() {
        return rcontent;
    }

    public void setrContent(String rcontent) {
        this.rcontent = rcontent;
    }

    public int getrStatus() {
        return status;
    }

    public void setrStatus(int status) {
        this.status = status;
    }

    public String getrCategory() {
        return rcategory;
    }

    public void setrCategory(String rcategory) {
        this.rcategory = rcategory;
    }

    public String getrAlerTime() {
        return ralertime;
    }

    public void setrAlerTime(String ralerTime) {
        this.ralertime = ralerTime;
    }

    public String getrAddtime() {
        return raddtime;
    }

    public void setrAddtime(String addtime) {
        this.raddtime = raddtime;
    }

    public String getrImg() {
        return rimg;
    }

    public void setrImg(String rimg) {
        this.rimg = rimg;
    }
}
