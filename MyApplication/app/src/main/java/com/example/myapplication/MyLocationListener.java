package com.example.myapplication;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

public class MyLocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location){
        String locationDescribe = location.getLocationDescribe();
        returnLocation(locationDescribe);
    }
    public String returnLocation(String location) {
        if (location != null) {
            return location;
        }
        return "Sorry, we cannot get the address";
    }
}