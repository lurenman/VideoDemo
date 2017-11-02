package com.example.lurenman.videodemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.lurenman.videodemo.video.VideoFileListActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tv_readVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_readVideo = (TextView) findViewById(R.id.tv_readVideo);
        initEvents();
    }

    private void initEvents() {
        tv_readVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, VideoFileListActivity.class);
                startActivity(intent);
            }
        });

    }
}
