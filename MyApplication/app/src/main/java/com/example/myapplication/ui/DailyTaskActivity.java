package com.example.myapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.VideoActivity;
import com.example.myapplication.bean.Task;
import com.example.myapplication.utils.DBSOpenHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DailyTaskActivity extends AppCompatActivity {

    private TextView tv_sum;
    private TextView tv_todayfinish;
    private ListView lv;
    private DBSOpenHelper dbOpenHelper;
    private List<Task> list = new ArrayList<>();
    private TaskAdapter adapter;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_task);
        dbOpenHelper = new DBSOpenHelper(this);
        user= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String task_id = "" + ds.child("task_id").getValue();
                    String ppl = "" + ds.child("emergancyppl").getValue();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        list = dbOpenHelper.getTasks();
        tv_sum=findViewById(R.id.tv_sum);
        tv_sum.setText("共完成: "+dbOpenHelper.getAllFinishNum());
        tv_todayfinish=findViewById(R.id.tv_todayfinish);
        tv_todayfinish.setText("今日已完成: "+dbOpenHelper.getTodayFinishNum());
        adapter = new TaskAdapter(this,list);
        lv=findViewById(R.id.lv);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DailyTaskActivity.this, VideoActivity.class);
                intent.putExtra("id",dbOpenHelper.updateTaskStatus(list.get(i).getId()));
                startActivity(intent);
            }
        });

        findViewById(R.id.recordbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DailyTaskActivity.this, TaskHistoryActivity.class));
            }
        });
    }


    }