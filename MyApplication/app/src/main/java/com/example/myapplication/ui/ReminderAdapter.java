package com.example.myapplication.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Reminder;
import com.example.myapplication.bean.Task;

import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends BaseAdapter {
    private List<Reminder> list = new ArrayList<>();
    private Context mContext;

    public ReminderAdapter(Context context, List<Reminder> objects) {
        this.mContext = context;
        this.list = objects;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        view = inflater.inflate(R.layout.task_list_item, null);
        Reminder reminder = (Reminder) getItem(position);
        TextView tv = view.findViewById(R.id.tv_mvname);
        tv.setText(reminder.getrContent() + "(" + reminder.getrSeldate() + ")");
        return view;
    }
}