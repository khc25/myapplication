package com.example.myapplication;

public class ModelComment {
    String timestamp,uid,uName,uimg,ucomment;

    public ModelComment(){}
    public ModelComment(String timestamp,String uid,String uName,String uimg,String ucomment){
        this.timestamp=timestamp;
        this.uid=uid;
        this.uName=uName;
        this.uimg=uimg;
        this.ucomment=ucomment;
    }

    public String getUcomment() {
        return ucomment;
    }

    public void setUcomment(String ucomment) {
        this.ucomment = ucomment;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getUid() {
        return uid;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
