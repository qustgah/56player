package com.iiijiaban.u56player.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	/** 数据库名称 */
	private static final String DB_NAME = "56playerdata.db";
	/**
	 * 数据库版本
	 */
	private static final int VERSION = 1;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String svideosql = "create table shvideo(id integer primary key autoincrement ,vid varchar(45) not null ,tag varchar(45) ,"
				+ "title varchar(255),mimg varchar(145),totaltime varchar(65),date text,type Integer,bimg varchar(145))";
		db.execSQL(svideosql); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists shvideo"); 
		onCreate(db);
	}

}
