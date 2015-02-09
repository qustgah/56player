package com.iiijiaban.u56player.ui;

import java.util.List;

import org.json.JSONObject;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.adapter.CommonViewAdapter;
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
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CollectionActivity extends Activity{

	public ImageLoader imageLoader = ImageLoader.getInstance();
	private GridView gridView;
	private CommonViewAdapter gridviewAdapter;
	private List<Videos> videos;
	private CollectionDao videoDao;
	private MenuItem edititem;
	private boolean iscan = false;
	RelativeLayout r;
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
		setContentView(R.layout.activity_collection);
		videoDao = new CollectionDao(this);
		videos = videoDao.findAllSvideo();
		initView();
		r=(RelativeLayout)findViewById(R.id.collectionadlayout);
		AdView adView = new AdView(this);
//		 代码设置AppSid和Appsec，此函数必须在AdView实例化前调用
		 AdView.setAppSid(this,"c3314f57");
		 AdView.setAppSec(this,"c3314f57");
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
	public void initView() {
		gridView = (GridView) findViewById(R.id.collections);
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
			    movie.setBimg(myvideo.getBimg());
			    movie.setTotaltime(myvideo.getTotaltime());
			    movie.setVid(myvideo.getVid()); 
				intent.putExtra("movie",movie);
				intent.setClass(CollectionActivity.this, VideoViewPlayingActivity.class);
				startActivity(intent);
			}
		}); 
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				iscan = true;
				gridviewAdapter.notifyDataSetChanged();
				Toast.makeText(CollectionActivity.this, "长按",	Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		gridviewAdapter = new CommonViewAdapter(videos, this) {
			@Override
			public View setView(View convertView, final int position, ViewGroup parent) {
				ViewHolder holder;
				imageLoader.init(ImageLoaderConfiguration
						.createDefault(context));
				if (convertView == null) { 
					holder = new ViewHolder();
					convertView = _inflater.inflate(R.layout.item_collection_gridview, null);
					holder.imageView = (ImageView) convertView.findViewById(R.id.image_collection);
					holder.textView = (TextView) convertView.findViewById(R.id.text_collection);
					holder.deleteView = (ImageView) convertView.findViewById(R.id.delete); 
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if(iscan){
					holder.deleteView.setVisibility(View.VISIBLE);
				}else{
					holder.deleteView.setVisibility(View.GONE);
				}
				holder.deleteView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Toast.makeText(CollectionActivity.this, "删除", Toast.LENGTH_SHORT).show();
						Videos video = (Videos) v.getTag();
						videoDao.deleteSvideo(video.getId()); 
						videos = videoDao.findAllSvideo();
						setEditable(false);
					}
				});
				holder.deleteView.setTag(datas.get(position));
				holder.textView.setText(((Videos) datas.get(position)).getTitle());
				imageLoader.displayImage(((Videos) datas.get(position)).getMimg(),holder.imageView, options);
				return convertView;
			} 
			@Override
			public long setItemId(int position) {
				return position;
			}
		};
		gridView.setAdapter(gridviewAdapter);

	}

	static class ViewHolder {
		ImageView deleteView;
		ImageView imageView;
		TextView textView;
	} 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		MenuInflater inflator = new MenuInflater(this);
		inflator.inflate(R.menu.collectionmenu, menu);
		edititem = menu.findItem(R.id.edit);
		edititem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				if(iscan){
					item.setIcon(getResources().getDrawable(R.drawable.edit));
					setEditable(false);
				}else{
					item.setIcon(getResources().getDrawable(R.drawable.finished));
					setEditable(true);
				} 
				return true;
			}
		});
		return super.onCreateOptionsMenu(menu);
	} 
	
	public void setEditable(boolean iscan){
		this.iscan = iscan;
		gridviewAdapter.setData(videos); 
	}
}
