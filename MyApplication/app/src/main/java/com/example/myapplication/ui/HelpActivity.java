package com.example.myapplication.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.example.myapplication.R;
import com.example.myapplication.bean.WeatherBean;
import com.example.myapplication.utils.NetUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity implements OnGetRoutePlanResultListener {
    private LinearLayout linearLayout;
    private EditText et_from;
    private EditText et_to;
    private RoutePlanSearch mSearch = null;
    private ListView mRouteListView = null;
    private RouteLineListAdapter mRouteLineListAdapter = null;
    private List<? extends RouteLine> mRouteLines;
    private List<String> weekList;
    private Handler handler;
    private Handler handler2;
    private LinearLayout otherLl;
    private HorizontalScrollView horizontalScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        horizontalScrollView = findViewById(R.id.sl_weather);
        otherLl = (LinearLayout) findViewById(R.id.other_ll);
        linearLayout = findViewById(R.id.lv_daohang);

        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 200) {
                    Toast.makeText(HelpActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                }
                if (msg.what == 100) {
                    List<WeatherBean> list = (List<WeatherBean>) msg.obj;

//                    CLEAR VIEW
                    otherLl.removeAllViews();
//                    ui GET
                    for (int i = 1; i < list.size(); i++) {
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.weather_item, null);
                        TextView weekItemTv = (TextView) view.findViewById(R.id.tv_week_item);
                        TextView weatherItemTv = (TextView) view.findViewById(R.id.tv_weather_item);
                        TextView tempItemTv = (TextView) view.findViewById(R.id.tv_temp_item);
                        weekItemTv.setText(list.get(i).getWeek());
                        weatherItemTv.setText(list.get(i).getWeather());
                        tempItemTv.setText(list.get(i).getTemp());
//                      ADD INTO VIEW
                        otherLl.addView(view);
                    }
                }
            }
        };
        handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 200) {
                    Toast.makeText(HelpActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                }
                if (msg.what == 100) {
                    String result = (String) msg.obj;
                    TextView details = findViewById(R.id.today_details);
                    details.setText(result);
                }
            }
        };

        weekList = new ArrayList<>();
        weekList.add("Monday");
        weekList.add("Tuesday");
        weekList.add("Wednesday");
        weekList.add("Thursday");
        weekList.add("Friday");
        weekList.add("Saturday");
        weekList.add("Sunday");

        findViewById(R.id.tv_weather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetUtils.isActive(HelpActivity.this)) {
                    Toast.makeText(HelpActivity.this, "Please confirm whether you have Internet！", Toast.LENGTH_LONG).show();
                    return;
                }

                horizontalScrollView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);

//       THREAD
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String uri = "http://weather.123.duba.net/static/weather_info/101320101.html";
                        String result = NetUtils.doGet(uri);
                        Log.d("",result);
                        String uri1 = "https://data.weather.gov.hk/weatherAPI/opendata/weather.php?dataType=flw&lang=tc";
                        String result1 = NetUtils.doGetToday(uri1);
                        Log.d("",result1);
//                SAVE INTO LIST
                        List<WeatherBean> list = parserJson(result);
                        Message message = handler.obtainMessage();

                        String todayweather=parserTodayJson(result1);

                        Message message1 = handler2.obtainMessage();
//                CHECK NETWORK
                        if (list == null || list.size() == 0) {
                            message.what = 200;
                            message.obj = "Check NetWork";
                            handler.sendMessage(message);
                            return;
                        }
                        if (todayweather == null || todayweather.length() == 0) {
                            message1.what = 200;
                            message1.obj = "Check NetWork";
                            handler2.sendMessage(message1);
                            return;
                        }
                        message1.what = 100;
                        message1.obj = todayweather;
                        handler2.sendMessage(message1);

                        message.what = 100;
                        message.obj = list;
                        handler.sendMessage(message);
                    }
                }).start();



            }
        });

        findViewById(R.id.tv_daohang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.VISIBLE);
                horizontalScrollView.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.okbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = et_from.getText().toString().trim();
                String to = et_to.getText().toString().trim();
                if (from.equals("") || to.equals("")) {
                    Toast.makeText(HelpActivity.this, "請輸入目的地和終點", Toast.LENGTH_SHORT).show();
                } else {
                    searchPlans(from, to);
                }
            }
        });

        et_from = findViewById(R.id.et_from);
        et_to = findViewById(R.id.et_to);
        mRouteListView = (ListView) findViewById(R.id.route_result_listview);
    }

    private void searchPlans(String from,String to){
        PlanNode startNode = PlanNode.withCityNameAndPlaceName("香港",from);
        PlanNode endNode = PlanNode.withCityNameAndPlaceName("香港",
                to);
        TransitRoutePlanOption transitRoutePlanOption =
                new TransitRoutePlanOption().from(startNode).to(endNode).city("香港");
        mSearch.transitSearch(transitRoutePlanOption);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearch != null) {
            mSearch.destroy();
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result != null && result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // result.getSuggestAddrInfo()
            Toast.makeText(HelpActivity.this,
                    "error: please user result.getSuggestAddrInfo()",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(HelpActivity.this, "抱歉,沒有資料", Toast.LENGTH_SHORT).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mRouteListView.setVisibility(View.VISIBLE);
            if (mRouteLines != null) {
                mRouteLines.clear();
            }
            mRouteLines = result.getRouteLines();
            initRouteOverViewData();
            mRouteLineListAdapter.notifyDataSetChanged();

        }
    }

    private void initRouteOverViewData() {
        mRouteLineListAdapter =
                new RouteLineListAdapter(HelpActivity.this.getApplicationContext(),
                        mRouteLines,
                        RouteLineListAdapter.Type.TRANSIT_ROUTE,et_to.getText().toString());
        mRouteListView.setAdapter(mRouteLineListAdapter);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult result) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult result) {

    }

    /**
     * breaking down json
     *
     * @param result
     */
    private String parserTodayJson(String result) {
        String todayWeather="";

        try {
            JSONObject jsonObject = new JSONObject(result);

                    todayWeather+=jsonObject.getString("forecastPeriod")+":\n"+jsonObject.getString("forecastDesc")+"\n"+jsonObject.getString("generalSituation");
                   /* if(!jsonObject.get("tcInfo").equals("")){
                        todayWeather+="現在的熱帶氣旋警告信號:\n"+jsonObject.get("tcInfo")+"\n";
                    }
                    if(!jsonObject.get("fireDangerWarning").equals("")){
                        todayWeather+="現在的火災危險警告:\n"+jsonObject.getString("fireDangerWarning")+"\n";
                    }
                    if(!jsonObject.get("outlook").equals("")){
                        todayWeather+="現在的熱帶氣旋警告信號:\n"+jsonObject.getString("outlook")+"\n";
                    }*/
            Log.d("",todayWeather);

            return  todayWeather;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private List<WeatherBean> parserJson(String result) {
        List<WeatherBean> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            //save today and 5 days afterwards
            for (int i = 1; i < 7; i++) {
                WeatherBean weatherBean = new WeatherBean();
                if (i == 1) {
                    weatherBean.setCity(weatherInfo.getString("city"));
                    weatherBean.setCityId(weatherInfo.getString("cityid"));
                    weatherBean.setDate_y(weatherInfo.getString("date_y"));
                    weatherBean.setPm("PM:" + weatherInfo.getString("pm") + " " + weatherInfo.getString("pm-level"));
                    weatherBean.setTempCurrent(weatherInfo.getString("temp") + "°");
                    weatherBean.setWindCurrent(weatherInfo.getString("wd") + " " + weatherInfo.getString("ws"));
                }
                weatherBean.setWeek(getWeek(i, weatherInfo.getString("week")));
                weatherBean.setTemp(weatherInfo.getString("temp" + i));
                if (weatherInfo.getString("weather" + i)=="多云") {
                    weatherBean.setWeather("Cloudy");
                }
                if (weatherInfo.getString("weather" + i).contains("雨")) {
                    weatherBean.setWeather("Rainy");
                }if (weatherInfo.getString("weather" + i).contains("晴")) {
                    weatherBean.setWeather("Sunny");
                } else{
                    weatherBean.setWeather("Cloudy");
                    //weatherBean.setWeather(weatherInfo.getString("weather" + i));
                }
                // ADD TO LIST
                list.add(weatherBean);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param i
     * @param week
     * @return
     */
    private String getWeek(int i, String week) {
        int index = 0;
        for (int j = 0; j < weekList.size(); j++) {
            if (weekList.get(j).equals(week)) {
                index = j;
                break;
//                continue;
            }
        }
        if (index + i < 8) {
            index = index + i - 1;
        } else {
            index = index + i - 8;
        }
        return weekList.get(index);
    }
}