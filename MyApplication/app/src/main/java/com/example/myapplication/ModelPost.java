package com.example.myapplication;

public class ModelPost {
    String pId,pTitle,pDescr,pImage,pTime,uid,uEmail,uDp,uName,pLikes,pComments;

    public ModelPost(){};

    public ModelPost(String pId,String pTitle,String pDescr,String pImage,String pTime,String uid,String pLikes,String uEmail,String uDp,String uName,String pComments){
        this.pId=pId;
        this.pTitle=pTitle;
        this.pDescr=pDescr;
        this.pImage=pImage;
        this.pTime=pTime;
        this.uid=uid;
        this.uEmail=uEmail;
        this.uDp=uDp;
        this.uName=uName;
        this.pLikes=pLikes;
        this.pComments=pComments;
    }

    public String getpComments() {
        return pComments;
    }

    public void setpComments(String pComments) {
        this.pComments = pComments;
    }

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }

    public String getUid() {
        return uid;
    }

    public String getpDescr() {
        return pDescr;
    }

    public String getpId() {
        return pId;
    }

    public String getpImage() {
        return pImage;
    }

    public String getpTime() {
        return pTime;
    }

    public String getpTitle() {
        return pTitle;
    }

    public String getuDp() {
        return uDp;
    }

    public String getuEmail() {
        return uEmail;
    }

    public String getuName() {
        return uName;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setpDescr(String pDescr) {
        this.pDescr = pDescr;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

}

