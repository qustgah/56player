package com.iiijiaban.u56player.beans;

import java.io.Serializable;

public class Videos implements Serializable{
	private String status_code;     //视频状态码
	private String vid;             //视频ID
	private String tag;             //视频标签
	private String title;           //视频标题
	private String desc;            //视频描述
	private int img;             //视频截图
	private String mimg;            //视频截图（中）
	private String bimg;            //视频截图（大）
	private String chk_yn;          //是否通过审核
	private String totaltime;         //视频总时长
	private String swf;             //视频播放地址
	private long code;              //错误码
	private String video_status;    //视频状态提示语
	private String date;           //用于收藏和历史记录   
	private Integer type ;             //用于收藏和历史记录   0 是收藏  1是历史记录
	private Integer id;
	
	
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
	public String getMimg() {
		return mimg;
	}
	public void setMimg(String mimg) {
		this.mimg = mimg;
	}
	public String getBimg() {
		return bimg;
	}
	public void setBimg(String bimg) {
		this.bimg = bimg;
	}
	public String getChk_yn() {
		return chk_yn;
	}
	public void setChk_yn(String chk_yn) {
		this.chk_yn = chk_yn;
	}
	 
	public String getSwf() {
		return swf;
	}
	public void setSwf(String swf) {
		this.swf = swf;
	}
	public long getCode() {
		return code;
	}
	public void setCode(long code) {
		this.code = code;
	}
	public String getVideo_status() {
		return video_status;
	}
	public void setVideo_status(String video_status) {
		this.video_status = video_status;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTotaltime() {
		return totaltime;
	}
	public void setTotaltime(String totaltime) {
		this.totaltime = totaltime;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	} 
}
