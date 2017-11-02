package com.example.lurenman.videodemo.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lurenman.videodemo.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: baiyang.
 * Created on 2017/11/2.
 */

public class VideodetailListviewAdapter extends BaseAdapter {
    private Context mContext;
    //查询视频的信息
    private List<VideoInfoEntity> mVideoInfoArray = new ArrayList<VideoInfoEntity>();

    public VideodetailListviewAdapter(Context mContext, List<VideoInfoEntity> mBitmapArray) {
        this.mContext = mContext;
        this.mVideoInfoArray = mBitmapArray;
    }

    @Override
    public int getCount() {
        if (mVideoInfoArray == null) {
            return 0;
        } else {
            return mVideoInfoArray.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mVideoInfoArray == null) {
            return null;
        } else {
            return mVideoInfoArray.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.lv_item_videofile, parent, false);
            holder.iv_imagVideo=(ImageView)convertView.findViewById(R.id.iv_imagVideo);
            holder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_videoSize=(TextView)convertView.findViewById(R.id.tv_videoSize);
            holder.tv_videoTime=(TextView)convertView.findViewById(R.id.tv_videoTime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mVideoInfoArray !=null && mVideoInfoArray.size() != 0) {
            Glide.with(mContext).load(mVideoInfoArray.get(position).getUri_thumb()).dontAnimate()
                    .placeholder(R.mipmap.icon_defaultimg).into(holder.iv_imagVideo);
            holder.tv_name.setText(mVideoInfoArray.get(position).getName());
            holder.tv_videoSize.setText(bytes2kb(mVideoInfoArray.get(position).getSize()));
            holder.tv_videoTime.setText(setTime(mVideoInfoArray.get(position).getDuration()));



        }

        return convertView;
    }
    private class ViewHolder {
        private ImageView iv_imagVideo;
        private TextView tv_name;
        private TextView tv_videoSize;
        private TextView tv_videoTime;
    }
    /*
    * byte 转kb
    * */
    public String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1) {
            return (returnValue + "MB");
        }
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "KB");
    }
    public String setTime(long total) {
        long hour = total / (1000 * 60 * 60);
        long letf1 = total % (1000 * 60 * 60);
        long minute = letf1 / (1000 * 60);
        long left2 = letf1 % (1000 * 60);

        long second = left2 / 1000;

        return hour + ":" + minute + ":" + second + "";
    }

}
