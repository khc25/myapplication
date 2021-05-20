package com.example.myapplication.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NetUtils {

    public static boolean isActive(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.isConnected();
        }
        return false;
    }


    public static String doGet(String uri) {
        StringBuffer stringBuffer = new StringBuffer();
        String result = null;
        URLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(uri);
            connection = url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuffer.append(line);
                line = bufferedReader.readLine();
            }
            result = stringBuffer.substring(stringBuffer.indexOf("(") + 1, stringBuffer.lastIndexOf(")"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String doGetToday(String uri) {
        StringBuffer stringBuffer = new StringBuffer();
        String result = null;
        URLConnection connection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(uri);
            connection = url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuffer.append(line);
                line = bufferedReader.readLine();
            }
            result=stringBuffer.toString();
            Log.d("NetUtils", "result:"+result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}