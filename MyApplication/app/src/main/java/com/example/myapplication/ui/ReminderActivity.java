package com.example.myapplication.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.ReminderDetailsActivity;
import com.example.myapplication.bean.Reminder;
import com.example.myapplication.utils.DBSOpenHelper;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.weiget.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private String seldate;
    private int[] cDate = CalendarUtil.getCurrentDate();
    private DBSOpenHelper dbOpenHelper;
    private List<Reminder> list = new ArrayList<>();
    private ListView listView;
    private ReminderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("日曆備忘錄");

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        calendarView = (CalendarView) findViewById(R.id.calendar);
        final TextView title = (TextView) findViewById(R.id.title);
        //current date
        Calendar calendar = Calendar.getInstance();
        //year
        int year = calendar.get(Calendar.YEAR);
        //month
        int month = calendar.get(Calendar.MONTH)+1;
        //day
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        title.setText(year+"-"+month);
        //init, sarepate with ,
        calendarView
                .setInitDate(year+"."+month+"."+day)
                .init();

        //PAGECHANGE
        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                title.setText(date[0] + "-" + date[1]);
            }
        });

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                title.setText(date.getSolar()[0] + "-" + date.getSolar()[1]);
                if (date.getType() == 1) {
                    seldate=date.getSolar()[0] + "-" + date.getSolar()[1] + "-" + date.getSolar()[2];
                    Intent intent = new Intent(ReminderActivity.this, AddReminderActivity.class);
                    intent.putExtra("seldate",seldate);
                    startActivity(intent);
                }
            }
        });
        dbOpenHelper = new DBSOpenHelper(this);
        list = dbOpenHelper.getAvbReminder();
        adapter = new ReminderAdapter(this,list);
        listView = findViewById(R.id.lv);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ReminderActivity.this, ReminderDetailsActivity.class);
                intent.putExtra("id",list.get(i).getrId());
                startActivity(intent);
            }
        });

    }

    public void someday(View v) {
        View view = LayoutInflater.from(ReminderActivity.this).inflate(R.layout.input_layout, null);
        final EditText year = (EditText) view.findViewById(R.id.year);
        final EditText month = (EditText) view.findViewById(R.id.month);
        final EditText day = (EditText) view.findViewById(R.id.day);

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(year.getText())
                                || TextUtils.isEmpty(month.getText())
                                || TextUtils.isEmpty(day.getText())) {
                            Toast.makeText(ReminderActivity.this, "请完善日期！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean result = calendarView.toSpecifyDate(Integer.valueOf(year.getText().toString()),
                                Integer.valueOf(month.getText().toString()),
                                Integer.valueOf(day.getText().toString()));
                        if (!result) {
                            Toast.makeText(ReminderActivity.this, "日期錯誤！", Toast.LENGTH_SHORT).show();
                        } else {
                            seldate = year.getText() + "-" + month.getText() + "-" + day.getText();
                            Intent intent = new Intent(ReminderActivity.this, AddReminderActivity.class);
                            intent.putExtra("seldate",seldate);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", null).show();
    }

    public void today(View view) {
        calendarView.today();
        seldate=cDate[0] + "-" + cDate[1] + "-" + cDate[2];
        Intent intent = new Intent(ReminderActivity.this, AddReminderActivity.class);
        intent.putExtra("seldate",seldate);
        startActivity(intent);
    }

    public void lastMonth(View view) {
        calendarView.lastMonth();
    }

    public void nextMonth(View view) {
        calendarView.nextMonth();
    }

    public void lastYear(View view) {
        calendarView.lastYear();
    }

    public void nextYear(View view) {
        calendarView.nextYear();
    }
}