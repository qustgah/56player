package com.iiijiaban.u56player.beans;

import java.io.Serializable;

public class ZhuTi implements Serializable{
	private String vid; //视频id
	private String cid; //分类id
	private String title; //标题
	private String tag; //标签
	private String content; //视频描述（内容）
	private String totaltime; //视频总长度
	private String ming; //视频缩略图地址  中图
	private String bimg; //大图
	private String img;  //小图
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTotaltime() {
		return totaltime;
	}
	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}
	public String getMing() {
		return ming;
	}
	public void setMing(String ming) {
		this.ming = ming;
	}
	public String getBimg() {
		return bimg;
	}
	public void setBimg(String bing) {
		this.bimg = bing;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

}
