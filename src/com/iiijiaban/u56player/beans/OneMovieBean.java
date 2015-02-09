package com.iiijiaban.u56player.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class OneMovieBean implements Serializable{
	/**
	 * vid	string	视频id
title	string	视频标题
content	string	视频描述
tag	string	视频标签
bimg	string	视频缩略图地址, 大图
user_id	string	用户id
comments	string	视频评论数
public_time	string	视频公开时间
totaltime	string	视频总时长
	 */
	
/**
 * vid	string	视频id
bimg	string	视频缩略图,大图 520*312
textid	string	vid对应base64编码
key	string	视频key
Subject	string	视频标题
img	string	视频缩略图,小图 130*78
duration	string	播放时长
hd	string  	高清参数
cid2	string  	视频类型标记
rfiles	 jsonarray	视频播放地址集合
url	 string	视频播放地址
 */
private String id;
private String title;
private String content;
private String tag;
private String comments;
private String public_time;
private String totaltime;

private String img;
private String bimg;
public String getBimg() {
	return bimg;
}
public void setBimg(String bimg) {
	this.bimg = bimg;
}
private String hd;
private String type;
private ArrayList<String> urls;
private String url;
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}
public String getTag() {
	return tag;
}
public void setTag(String tag) {
	this.tag = tag;
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
public String getPublic_time() {
	return public_time;
}
public void setPublic_time(String public_time) {
	this.public_time = public_time;
}
public String getTotaltime() {
	return totaltime;
}
public void setTotaltime(String totaltime) {
	this.totaltime = totaltime;
}
public String getImg() {
	return img;
}
public void setImg(String img) {
	this.img = img;
}
public String getHd() {
	return hd;
}
public void setHd(String hd) {
	this.hd = hd;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public ArrayList<String> getUrls() {
	return urls;
}
public void setUrls(ArrayList<String> urls) {
	this.urls = urls;
}
public String getUrl() {
	return url;
}
public void setUrl(String url) {
	this.url = url;
}

}
