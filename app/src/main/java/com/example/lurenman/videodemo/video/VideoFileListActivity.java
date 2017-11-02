package com.example.lurenman.videodemo.video;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lurenman.videodemo.R;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author: baiyang.
 * Created on 2017/11/2.
 */

public class VideoFileListActivity extends Activity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "VideoFileListActivity";
    private static final int GET_VIDEOINFO = 100;
    private ListView lv_listView;
    private VideodetailListviewAdapter mAdapter;
    //查询视频的信息
    private List<VideoInfoEntity> mVideoInfoArray = new ArrayList<VideoInfoEntity>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videofilelist);
        initViews();
        initPermissions();
    }

    private void initPermissions() {
        if (!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, "需要读写权限", 200, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            //开启线程查询
            new SearchVideoThread().start();
        }
    }

    private void initViews() {
        lv_listView = (ListView) findViewById(R.id.lv_listView);
    }
    /**
     * 遍历系统数据库找出相应的是视频的信息，每找出一条视频信息的同时利用与之关联的找出对应缩略图的uri 再异步加载缩略图，
     * 由于查询速度非常快，全部查找完成在设置，等待时间不会太长
     *
     * @author Administrator
     */
    private class SearchVideoThread extends Thread {
        @Override
        public void run() {
            // 如果有sd卡（外部存储卡）
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                Uri originalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = VideoFileListActivity.this.getApplicationContext().getContentResolver();
                Cursor cursor = cr.query(originalUri, null, null, null, null);
                if (cursor == null) {
                    return;
                }
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    // 获取当前Video对应的Id，然后根据该ID获取其缩略图的uri
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String[] selectionArgs = new String[]{id + ""};
                    String[] thumbColumns = new String[]{MediaStore.Video.Thumbnails.DATA,
                            MediaStore.Video.Thumbnails.VIDEO_ID};
                    String selection = MediaStore.Video.Thumbnails.VIDEO_ID + "=?";


                    String uri_thumb = "";
                    Cursor thumbCursor = (VideoFileListActivity.this.getApplicationContext().getContentResolver())
                            .query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, thumbColumns, selection,
                                    selectionArgs, null);

                    if (thumbCursor != null && thumbCursor.moveToFirst()) {
                        uri_thumb = thumbCursor
                                .getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));

                    }

                    VideoInfoEntity entity = new VideoInfoEntity(title, path, size, uri_thumb, duration);
                    mVideoInfoArray.add(entity);

                }
                if (cursor != null) {
                    cursor.close();
                    mHandler.sendEmptyMessage(GET_VIDEOINFO);
                }
            }
        }
    }

    //获取视频信息回调的handler (这块内存泄漏先不考虑)
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == GET_VIDEOINFO && mVideoInfoArray != null) {
                //更新adapter
                mAdapter=new VideodetailListviewAdapter(VideoFileListActivity.this,mVideoInfoArray);
                lv_listView.setAdapter(mAdapter);
                //该做什么事做什么
            }

        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 200) {
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //开启线程查询
                new SearchVideoThread().start();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 200) {
            if (perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(getApplicationContext(), "没有读写权限无法使用存储功能", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
