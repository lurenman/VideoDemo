package com.example.lurenman.videodemo.video;

import android.graphics.Bitmap;

public class VideoInfoEntity {
	private String name;//video名字
	private String path;//video路径
	private long size;//video文件大小
	private String uri_thumb;//video图片
	private long duration;//持续时间
	public VideoInfoEntity(String name, String path, long size, String uri_thumb, long duration) {
		super();
		this.name = name;
		this.path =path;
		this.size = size;
		this.uri_thumb = uri_thumb;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getUri_thumb() {
		return uri_thumb;
	}

	public void setUri_thumb(String uri_thumb) {
		this.uri_thumb = uri_thumb;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
