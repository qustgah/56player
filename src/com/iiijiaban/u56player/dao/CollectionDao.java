package com.iiijiaban.u56player.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iiijiaban.u56player.beans.Videos;
import com.iiijiaban.u56player.db.DatabaseHelper;

public class CollectionDao {

	private SQLiteDatabase db;
	private DatabaseHelper dbHelper;
	
	public CollectionDao(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		db.close();
	}
	
	public void insertSvideo(Videos video){
		
		ContentValues contentValues = new ContentValues();
		contentValues.put("vid", video.getVid());
		contentValues.put("tag", video.getTag());
		contentValues.put("title", video.getTitle());
		contentValues.put("mimg", video.getMimg());
		contentValues.put("totaltime", String.valueOf(video.getTotaltime()));
		contentValues.put("date", video.getDate());
		contentValues.put("type", video.getType());
		contentValues.put("bimg", video.getBimg());
		
	    // Insert into DB
		db.insert("shvideo", null, contentValues); 
	}
	
	public long insertCVideo(Videos video){
		
		if(findExitsCVideo(video)){
			return -1;
		}
		
		ContentValues contentValues = new ContentValues();
		contentValues.put("vid", video.getVid());
		contentValues.put("tag", video.getTag());
		contentValues.put("title", video.getTitle());
		contentValues.put("mimg", video.getMimg());
		contentValues.put("totaltime", String.valueOf(video.getTotaltime()));
		contentValues.put("date", video.getDate());
		contentValues.put("type", video.getType()); 
		contentValues.put("bimg", video.getBimg());
	    // Insert into DB
		long rowid = db.insert("shvideo", null, contentValues);  
		return rowid;
	} 
	public int  deleteSvideo(int id) { 
		int rows = db.delete("shvideo", "id = " + id, null); 
		return rows;
	}
	
	public List<Videos> findAllSvideo(){
		List<Videos> videos = new ArrayList<Videos>();
		Cursor cursor = db.query("shvideo", null, "type = 0", null, null, null, null);
		cursor.moveToFirst(); 
		// Iterate the results
	    while (!cursor.isAfterLast()) {
	    	Videos video = new Videos();
	    	// Take values from the DB
	    	video.setId(cursor.getInt(0));
	    	video.setVid(cursor.getString(1));
	    	video.setTag(cursor.getString(2));
	    	video.setTitle(cursor.getString(3));
	    	video.setMimg(cursor.getString(4));
	    	video.setTotaltime(cursor.getString(5));
	    	video.setBimg(cursor.getString(8));
	    	videos.add(video);
	    	cursor.moveToNext();
	    }
		return videos; 
	}
	public boolean findExitsCVideo(Videos video){
		
		if(video.getType()==0){
			Cursor cursor = db.query("shvideo", null, "type = 0 and vid = "+video.getVid(), null, null, null,null);
			if(cursor.getCount()==0){
				return false;
			}
		} 
		return true;
	}
	
	public List<Videos> findAllHvideo(){
		List<Videos> videos = new ArrayList<Videos>();
		Cursor cursor = db.query("shvideo", null, "type = 1", null, null, null, "id desc");
		cursor.moveToFirst(); 
		// Iterate the results
	    while (!cursor.isAfterLast()) {
	    	Videos video = new Videos();
	    	// Take values from the DB
	    	video.setId(cursor.getInt(0));
	    	video.setVid(cursor.getString(1));
	    	video.setTag(cursor.getString(2));
	    	video.setTitle(cursor.getString(3));
	    	video.setMimg(cursor.getString(4)); 
	    	video.setTotaltime(cursor.getString(5));
	    	video.setDate(cursor.getString(6));
	    	video.setType(cursor.getInt(7));
	    	video.setBimg(cursor.getString(8));
	    	videos.add(video);
	    	cursor.moveToNext();
	    }
		return videos; 
	}
	
	public List<Videos> findPageHistory(int count,int start){
		
		List<Videos> videos = new ArrayList<Videos>();  
		String sql = "select * from (select * from shvideo where type = 1 order by id desc) a limit "+ count + "  offset "+ start;
		Cursor cursor=db.rawQuery(sql, null);
//		 = db.query("shvideo", null, "type = 1", null, null, null, null);
		cursor.moveToFirst(); 
		// Iterate the results
	    while (!cursor.isAfterLast()) {
	    	Videos video = new Videos();
	    	// Take values from the DB
	    	video.setId(cursor.getInt(0));
	    	video.setVid(cursor.getString(1));
	    	video.setTag(cursor.getString(2));
	    	video.setTitle(cursor.getString(3));
	    	video.setMimg(cursor.getString(4)); 
	    	video.setTotaltime(cursor.getString(5));
	    	video.setDate(cursor.getString(6));
	    	video.setType(cursor.getInt(7));
	    	video.setBimg(cursor.getString(8));
	    	videos.add(video);
	    	cursor.moveToNext();
	    }
		return videos; 
	} 
	public int deleteAllHistory(){
		int rows = db.delete("shvideo", "type = 1", null); 
		return rows;
	} 
}
