package com.iiijiaban.u56player.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.iiijiaban.u56player.beans.OneMovieBean;
import com.iiijiaban.u56player.beans.ZhuTi;


public class JsonUtil {
	private static JSONArray jArray;
	private static JSONObject object;
    private static  ZhuTi bean;
    
    
	public static void getSearch(String json,ArrayList<ZhuTi> beans){
		if(json==null){
			return;
		}
		try {
			object=new JSONObject(json);
            Iterator<String> it=object.keys();
            
         while(it.hasNext()){
        	 String s=it.next();
        	 bean=new ZhuTi();
        	 if(!s.equals("total")){
        		 JSONObject jb=object.getJSONObject(s); 
 
        			bean.setVid(jb.getString("vid"));
        			bean.setTitle(jb.getString("title"));
        			bean.setBimg(jb.getString("bimg"));
        			bean.setTotaltime(jb.getString("totaltime"));
        			bean.setTag(jb.getString("tag"));
        			bean.setImg(jb.getString("img"));
        			bean.setMing(jb.getString("mimg"));
        			bean.setCid(jb.getString("cid"));
        			
//        			bean.setContent(jb.getString("content"));
        			beans.add(bean);
        	  }
 
        	
         }
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	public static void getInfo(String jsString,OneMovieBean bean)
	{
		try {
			object=new JSONObject(jsString);
			object=object.getJSONObject("0");
			bean.setContent(object.getString("desc"));
		} catch (Exception e) {
			
 		}
		
	}
	public static void getMovieinfo(String json, OneMovieBean bean) {
		try {
			object=new JSONObject(json);
			JSONObject jObject=object.getJSONObject("info");
			if (jObject!=null) {
				bean.setHd(jObject.getString("hd"));
				bean.setImg(jObject.getString("img"));
				bean.setType("cid2");
				jArray=jObject.getJSONArray("rfiles");
				ArrayList<String> strings=new ArrayList<String>();
				for (int i = 0; i < jArray.length(); i++) {
					String string=jArray.getJSONObject(i).getString("url");
					strings.add(string);
				}
				bean.setUrls(strings);
			}
		} catch (Exception e) {
		}
		
	}
}
