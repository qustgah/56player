package com.iiijiaban.u56player.util;

import org.apache.commons.net.ntp.TimeStamp;

public class MyTools {
	public static String changeTime(Long time)
	{
		return new TimeStamp(time*100).toString();
	}
	//将读取的时间转换成“分：秒”
  public  static String changeTime2(Long time)
  {
	   float f=time/1000/60;
	   float m=time%(1000*60);
	   return (int)f+":"+String.valueOf(m).substring(0, 2);
	
  }
}
