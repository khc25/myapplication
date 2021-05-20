package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.myapplication.databinding.ActivityHomeBinding;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.ui.AddReminderActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity implements SensorEventListener {
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseUser user;
    TextView textView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ActivityMainBinding binding;
    private LocationClient mLocationClient;
    private CountDownTimer timer;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean hvSensor;
    private Vibrator vib;
    boolean hvtimer;
    private Integer count;
    private String result;
    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 5000;
    FirebaseAuth firebaseAuth;
    boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        user=firebaseAuth.getCurrentUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        TextView navEmail = (TextView) headerView.findViewById(R.id.textView);
        ImageView navImg=(ImageView)headerView.findViewById(R.id.imageView);
        navUsername.setText(user.getUid());
        navEmail.setText(user.getEmail());
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String ds_image = "" + ds.child("image").getValue();
                    try {
                        Picasso.get().load(ds_image).into(navImg);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_default_img).into(navImg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference ReminderRef = FirebaseDatabase.getInstance().getReference("Reminders");

        addTask(1,"如何用臉書(Facebook)","","youtube","gpaDzhVBLvc",0,"",user.getUid());
        addTask(2,"如何用Whatsappi之一","","youtube","9ajW0Kxel2M",0,"",user.getUid());
        addTask(3,"如何用Whatsapp之二","","youtube","rEcNo1e0oAI",0,"",user.getUid());
        addTask(4,"如何用Whatsapp之三","","youtube","pYzccMTkLW4",0,"",user.getUid());
        addTask(5,"如何用ZOOM","","youtube","SaqOWMczFFY",0,"",user.getUid());
        addTask(6,"如何用轉數快","","youtube","eicoNqqbHQ",0,"",user.getUid());
        addTask(7,"如何用網上購物使用電子支付(PAYME)","","youtube","wIiwiR4OCMU",0,"",user.getUid());
        addTask(8,"如何用瀏覽器","","youtube","Yfd_TP1auuo",0,"",user.getUid());

        com.example.myapplication.Question q1 = new com.example.myapplication.Question("藍色咪代表甚麼呢", "4","A.錄音按鈕 ", "B.播放鍵", "C.對方已聽了您的錄音", 3);
        addQuestion(q1,1,user.getUid());
        com.example.myapplication.Question q2 = new com.example.myapplication.Question("兩個灰色剔代表甚麼呢","4", "A.對方已收到你的訊息", "B.對方讀了你的訊息", "C.對方已離線", 1);
        addQuestion(q2,2,user.getUid());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        // create instance of sensor manager

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        count=0;
        // create instance of sensor
        // with type linear acceleration

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            hvSensor=true;
        }else {
            hvSensor=false;
        }
        Log.d("", "created");
        timer = newTimer();
        clicked = false;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000) {
            if (!(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(HomeActivity.this, R.string.camera_tip, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("", "Changed");
        hvtimer=false;
        restartTimer();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.action_logout){
            firebaseAuth.signOut();
            FirebaseUser user=firebaseAuth.getCurrentUser();
            if(user!=null){
                firebaseAuth.signOut();
            }else{
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void addTask(int task_id,String mvname,String mvimg,String mvcategory,String mvpath,int status,String addtime,String uid){
        DatabaseReference TaskRef = FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String, Object> map = new HashMap<>();
        map.put("task_id",task_id);
        map.put("mvname",mvname);
        map.put("mvimg",mvimg);
        map.put("mvcategory",mvcategory);
        map.put("mvpath",mvpath);
        map.put("status",status);
        map.put("addtime",addtime);

        TaskRef.child(""+uid).child("Tasks").child(""+task_id).setValue(map);

    }

    private void addQuestion(com.example.myapplication.Question question,int i,String uid) {
        DatabaseReference QuestionRef = FirebaseDatabase.getInstance().getReference("Users");
        HashMap<String, Object> cv= new HashMap<>();
        cv.put("question_id",i);
        cv.put(QuizContract.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContract.QuestionsTable.TASK_ID, question.getTask_id());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizContract.QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        QuestionRef.child(""+uid).child("Questions").child(""+i).setValue(cv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void restartTimer(){
        Log.d("", "restart");
        vib= vib = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(50);
        if(hvtimer=true) {
            timer.cancel();
        }

        if(hvtimer=false) {
            timer = newTimer();
        }
    };


    public CountDownTimer newTimer(){
        Log.d("", "timer start");
        CountDownTimer time;
        hvtimer=true;
        time=new CountDownTimer(5400000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                clicked=false;
                android.app.AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("你在嗎?");

                builder.setPositiveButton("在!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clicked=true;
                    }
                });
                builder.create().show();
                if( clicked!=true) {
                    gpsinit();
                    hvtimer = false;
                    Log.d("", "DONE");
                    //auth
                    firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference("Users");
                    Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String number = "" + ds.child("emergancyppl").getValue();
                                if(""!=number) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:" + number));
                                    startActivity(intent);
                                }

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                    //gps
                    restartTimer();
                }
            }
        }.start();
        return time;
    }
    public void gpsinit(){
        Log.d("code", "gpsinit");
        locationClient = new LocationClient(this);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);

        option.setAddrType("all");

        option.setPriority(LocationClientOption.GpsFirst);
        option.setProdName("LocationDemo");

        locationClient.setLocOption(option);


        locationClient.registerLocationListener(new BDLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation location) {
                Log.d("code", "listener");
                int tag=2;
                result=null;
                if (location == null) {
                    result=null;
                    return;
                }
                StringBuffer sb = new StringBuffer(256);
                sb.append("Time : ");
                sb.append(location.getTime());
                Log.d("time", ""+location.getTime());
                Log.d("\nError code : ", ""+location.getLocType());

                    sb.append("\nAddress : ");
                    sb.append(location.getAddrStr());

                    Log.d("address", sb.toString());
                    result=location.getAddrStr();
                SmsManager smsManager = SmsManager.getDefault();
                if (result != null ){
                    smsManager.sendTextMessage("60911633", null, "你親人的電話未能打通 恐怕有危險 地址是:" + result, null, null);
                }else {
                    smsManager.sendTextMessage("60911633", null, "你親人的電話未能打通 恐怕有危險 抱歉未能提供地址" , null, null);}
            }

        });
        if (locationClient.isStarted()) {
            locationClient.stop();
        }
        else {
            locationClient.start();
            locationClient.requestLocation();

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
    }

    private void showDialog() {

    }




}



