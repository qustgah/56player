package com.iiijiaban.u56player.ui;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.fragment.SearchFragment;
import com.iiijiaban.u56player.fragment.SearchResult;
import com.wole56.sdk.Video;

public class SearchActivity extends FragmentActivity  implements SearchView.OnQueryTextListener {
    String searchText, json;
    private Handler handler;
    private SearchView searchView;
    private MenuItem backbutton;
    private RelativeLayout r;
    private InputMethodManager input;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.searchactivity);
		searchView = (SearchView) findViewById(R.id.search);
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(false);
		searchView.setQueryHint("查找");
		r=(RelativeLayout)findViewById(R.id.searchadlayout);
		AdView adView = new AdView(this);
		input = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = new MenuInflater(this);
		inflator.inflate(R.menu.backaction, menu);
		backbutton = (MenuItem) menu.findItem(R.id.back);
		
		backbutton.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(SearchActivity.this, MainActivity.class);
				startActivity(intent);
				return false;
			}
			
		});
		return super.onCreateOptionsMenu(menu);
	}




	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@SuppressLint("HandlerLeak")
	@Override
	public boolean onQueryTextSubmit(String query) {
		input.hideSoftInputFromWindow(searchView.getWindowToken(),0);
		searchText = query;
		new Thread(new Runnable(){
			
			@Override
			public void run() {
				
                json = Video.searchVideo(SearchActivity.this, searchText, "20", "0").toString();
                Message msg = new Message();
                msg.obj = json;
                handler.sendMessage(msg);
			}
			
		}).start();
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				String video = null;
				video = (String) msg.obj;
				
				if( video != null)
				{
					SearchFragment fragment = new SearchFragment();
					Bundle data = new Bundle();
					data.putString("SearchText", searchText);
					fragment.setArguments(data);
					getSupportFragmentManager().beginTransaction().replace(R.id.searchview, fragment).commit();
				}else{
					SearchResult rFragment = new SearchResult();
					getSupportFragmentManager().beginTransaction().replace(R.id.searchview, rFragment).commit();
				}
				
				super.handleMessage(msg);
			}
			
		};
		return false;
	}

}
