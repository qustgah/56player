package com.iiijiaban.u56player.ui; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.adapter.CommonViewAdapter;
import com.iiijiaban.u56player.beans.OneMovieBean;
import com.iiijiaban.u56player.beans.Videos;
import com.iiijiaban.u56player.beans.ZhuTi;
import com.iiijiaban.u56player.dao.CollectionDao;
import com.iiijiaban.u56player.util.CommonAsyncTaskLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends Activity{

	public ImageLoader imageLoader = ImageLoader.getInstance();
	private ListView gridView ;
	private CommonViewAdapter gridviewAdapter;
	private List<Videos> videos; 
	private CollectionDao videoDao;
	
	private MenuItem edititem;
	private MenuItem clearAllitem;
	private MenuItem clearPageitem;
	private int start = 1;
	private int count = 20 ;
	private TextView nextpage;
	private TextView prepage;
	private RelativeLayout r;
	DisplayImageOptions options = new DisplayImageOptions.Builder()
	.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
	.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
	.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
	.cacheInMemory(true) // 设置下载的图片是否缓存在内存中 
	.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
	.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
	.build(); // 创建配置过得DisplayImageOption对象

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		videoDao = new CollectionDao(this);
		videos = videoDao.findPageHistory(count, start);
		initView(); 
		r=(RelativeLayout)findViewById(R.id.historyadlayout);
		AdView adView = new AdView(this);
//		 代码设置AppSid和Appsec，此函数必须在AdView实例化前调用
		 AdView.setAppSid(this,"f028398f");
		 AdView.setAppSec(this,"f028398f");
			// 设置监听器
			adView.setListener(new AdViewListener() {
				public void onAdSwitch() {
					Log.w("", "onAdSwitch");
				}
				public void onAdShow(JSONObject info) {
					Log.w("", "onAdShow " + info.toString());
				}
				public void onAdReady(AdView adView) {
					Log.w("", "onAdReady " + adView);
				}
				public void onAdFailed(String reason) {
					Log.w("", "onAdFailed " + reason);
				}
				public void onAdClick(JSONObject info) {
					Log.w("", "onAdClick " + info.toString());
				}
				public void onVideoStart() {
					Log.w("", "onVideoStart");
				}
				public void onVideoFinish() {
					Log.w("", "onVideoFinish");
				}
				@Override
				public void onVideoClickAd() {
					Log.w("", "onVideoFinish");
				}
				@Override
				public void onVideoClickClose() {
					Log.w("", "onVideoFinish");
				}
				@Override
				public void onVideoClickReplay() {
					Log.w("", "onVideoFinish");
				}
				@Override
				public void onVideoError() {
					Log.w("", "onVideoFinish");
				}
			});
			r.addView(adView);
	} 
	public void initView(){
		gridView = (ListView) findViewById(R.id.histories);
		LayoutInflater inf = LayoutInflater.from(this);
		View view = inf.inflate(R.layout.clickbutton, null);
		nextpage = (TextView) view.findViewById(R.id.nextpage);
		prepage = (TextView) view.findViewById(R.id.prepage);
		if(videos.size()<20){
			nextpage.setVisibility(View.INVISIBLE);
		}else{
			nextpage.setVisibility(View.VISIBLE);
		}
		
		prepage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				start -= 20;
				videos = videoDao.findPageHistory(count, start); 
				nextpage.setVisibility(View.VISIBLE);
				if(start<20){
					prepage.setVisibility(View.INVISIBLE);
				}
				gridviewAdapter.setData(videos);
			}
		}); 
		nextpage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				start += 20;
				videos = videoDao.findPageHistory(count, start);
				prepage.setVisibility(View.VISIBLE);
				if(videos.size()<20){
					nextpage.setVisibility(View.INVISIBLE); 
				}else{
					nextpage.setVisibility(View.VISIBLE);
				}
				gridviewAdapter.setData(videos);
			}
		});
		gridView.addFooterView(view, null, false);;
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent();  
				ZhuTi movie = new ZhuTi();
				Videos myvideo = (Videos)arg0.getItemAtPosition(arg2);
			    movie.setTitle(myvideo.getTitle());
			    movie.setTag(myvideo.getTag());
			    movie.setImg(myvideo.getMimg());
			    movie.setTotaltime(myvideo.getTotaltime());
			    movie.setVid(myvideo.getVid());
			    movie.setBimg(myvideo.getBimg()); 
				intent.putExtra("movie",movie);
				intent.setClass(HistoryActivity.this, VideoViewPlayingActivity.class);
				startActivity(intent);
			}
		});
		gridviewAdapter = new CommonViewAdapter(videos,this) { 
			@Override
			public View setView(View convertView, final int position, ViewGroup parent) {  
				ViewHolder holder;
				imageLoader.init(ImageLoaderConfiguration.createDefault(context));
				if(convertView == null){ 
					holder = new ViewHolder();
					convertView = _inflater.inflate(R.layout.item_history_gridview, null);
					holder.imageView = (ImageView) convertView.findViewById(R.id.image_history);
					holder.textView = (TextView) convertView.findViewById(R.id.text_history);
					holder.timeView = (TextView) convertView.findViewById(R.id.text_history_time);
					holder.deleteView = (ImageView) convertView.findViewById(R.id.delete);
					convertView.setTag(holder);
				}else{
					holder = (ViewHolder) convertView.getTag();
				}
				Videos video = (Videos)datas.get(position);
				long time = Long.valueOf(video.getTotaltime());
				int second = (int) (time/1000);
				int hh = (int) (second / 3600);
				int mm = (int) (second % 3600 / 60);
				int ss = (int) (second % 60);
				String strTemp = null;
				if (0 != hh) {
					strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
				} else {
					strTemp = String.format("%02d:%02d", mm, ss);
				}  
				holder.deleteView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) { 
						deleteVideo(((Videos)datas.get(position)).getId());
					}
				});
				holder.timeView.setText(strTemp);
				holder.textView.setText(((Videos)datas.get(position)).getTitle());
				imageLoader.displayImage(((Videos)datas.get(position)).getMimg(), holder.imageView, options);
				return convertView;
			} 
			@Override
			public long setItemId(int position) { 
				return position;
			}
		};
		gridView.setAdapter(gridviewAdapter);
	} 
	static class ViewHolder{
		ImageView imageView;
		TextView textView;
		TextView timeView;
		ImageView deleteView;
	}  
	
	public void deleteVideo(int id){
		videoDao.deleteSvideo(id);
		videos = videoDao.findPageHistory(count,start);
		gridviewAdapter.setData(videos);
	}
	public void deletePage(){
		for(int i = 0;i<videos.size();i++){
			 videoDao.deleteSvideo(videos.get(i).getId());
		} 
		videos = videoDao.findPageHistory(count,start);
		gridviewAdapter.setData(videos);
	}
	public void deleteAll(){
		videoDao.deleteAllHistory();
		videos = new ArrayList<Videos>();
		gridviewAdapter.setData(videos);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		MenuInflater inflator = new MenuInflater(this);
		inflator.inflate(R.menu.historymenu, menu);
		
//		edititem = menu.findItem(R.id.edit);
		clearAllitem = menu.findItem(R.id.clearAll);
		clearPageitem = menu.findItem(R.id.clearthispage);
		
		clearPageitem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
			 
				deletePage();
				return true;
			}
		});
		clearAllitem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				deleteAll();
				return true;
			}
		}); 
		return super.onCreateOptionsMenu(menu);
	}
}
