package com.example.myapplication;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.CountDownTimer;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;


public class MyApplication extends Application {
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean hvSensor;
    private AudioManager vib;
    private CountDownTimer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        // Baidu map
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

}
