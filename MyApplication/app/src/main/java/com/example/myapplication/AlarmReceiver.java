package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.ui.AddReminderActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager mManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        String content =intent.getStringExtra("content");
        int notId=intent.getIntExtra("id",0);
        Intent activiyIntent = new Intent(context, AddReminderActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,activiyIntent,PendingIntent.FLAG_ONE_SHOT);

        String channelId="channel_id";
        CharSequence name="channel_name";
        String description="descripition";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            if (mManager == null) {
                mManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            mManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context, channelId)
                .setContentTitle("提示!")
                .setContentText(content)
                .setSmallIcon(R.drawable.logo)
                .setDeleteIntent(pendingIntent)
                .setGroup("Group_calender_view")
                .setContentText(content).setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + context.getPackageName() + "/raw/notify"));

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());;

        Intent x = new Intent(context, Alert.class);
        x.putExtra("content",content);
        x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(x);
    }
}