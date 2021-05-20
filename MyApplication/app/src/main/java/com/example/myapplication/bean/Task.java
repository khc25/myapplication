package com.example.myapplication.bean;

public class Task {
    private int id;
    private String mvname;
    private String mvimg;
    private String mvpath;
    private int status;
    private String addtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMvname() {
        return mvname;
    }

    public void setMvname(String mvname) {
        this.mvname = mvname;
    }

    public String getMvpath() {
        return mvpath;
    }

    public void setMvpath(String mvpath) {
        this.mvpath = mvpath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getMvimg() {
        return mvimg;
    }

    public void setMvimg(String mvimg) {
        this.mvimg = mvimg;
    }
}
