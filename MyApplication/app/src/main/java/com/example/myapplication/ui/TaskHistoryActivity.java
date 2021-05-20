package com.example.myapplication.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.bean.Task;
import com.example.myapplication.utils.DBSOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskHistoryActivity extends AppCompatActivity {

    private DBSOpenHelper dbOpenHelper;
    private List<Task> list = new ArrayList<>();
    private ListView listView;
    private TaskAdapter adapter;
    private TextView tv_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);
        dbOpenHelper = new DBSOpenHelper(this);
        list = dbOpenHelper.getHistoryTasks();
        tv_nodata=findViewById(R.id.tv_nodata);
        adapter = new TaskAdapter(this,list);
        listView = findViewById(R.id.lv);
        listView.setAdapter(adapter);
        if(list.size()==0){
            tv_nodata.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            tv_nodata.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }
}