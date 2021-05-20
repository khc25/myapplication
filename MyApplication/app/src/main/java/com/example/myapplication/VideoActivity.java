package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.bean.Task;
import com.example.myapplication.utils.DBSOpenHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoActivity extends AppCompatActivity {
    private DBSOpenHelper dbOpenHelper;
    private String vpath,vname,vimgpath;
    private int vid;
    private Task currentTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        vpath="";vid=0;
        Intent intent = new Intent();
        vid=intent.getIntExtra("id",0);
        currentTask=dbOpenHelper.getOneTask(vid);
        if(null!=currentTask){
            vpath=currentTask.getMvpath();
            vname=currentTask.getMvname();
            vimgpath=currentTask.getMvimg();
        }
        ActionBar actionBar =getSupportActionBar();
        actionBar.setTitle("任務: "+vname);

        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId="";
                if(""!=vpath) {
                    videoId = vpath;
                }
                youTubePlayer.loadVideo(videoId, 0);
            }
        });


    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}