package com.iiijiaban.u56player.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.fragment.MainFragment;
import com.iiijiaban.u56player.util.ConStants;
import com.wole56.sdk.APP;

public class MainActivity extends FragmentActivity{
	private static final int CONTENT_VIEW_ID = 11111;
	private MenuItem searchView; 
	private MenuItem collection;
	private MenuItem history;
	private GoogleProgressBar gp;
	private ImageView image;
	private int[] colors={R.color.actionbar,R.color.actionbar,R.color.actionbar,R.color.actionbar};
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		FrameLayout frame = new FrameLayout(this);
		FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,   
                ViewGroup.LayoutParams.WRAP_CONTENT);//定义框架布局器参数  
		FrameLayout.LayoutParams params2=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,   
                ViewGroup.LayoutParams.WRAP_CONTENT);//定义框架布局器参数  
        FrameLayout.LayoutParams tparams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,   
                ViewGroup.LayoutParams.MATCH_PARENT);//定义显示组件参数  
        FrameLayout.LayoutParams params3=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,   
                ViewGroup.LayoutParams.WRAP_CONTENT);//定义框架布局器参数  
		gp=new GoogleProgressBar(getApplicationContext());
		 gp.setIndeterminateDrawable(new GoogleMusicDicesDrawable.Builder().build());
		 image=new ImageView(getApplicationContext());
		 image.setImageDrawable(getResources().getDrawable(R.drawable.login));
		 DisplayMetrics metric = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(metric);
	        int width = metric.widthPixels;  // 屏幕宽度（像素）
	        int height = metric.heightPixels;  // 屏幕高度（像素）
	        TextView v=new TextView(getApplicationContext());
	        v.setText("加载中..请稍候~");
	        v.setTextColor(getResources().getColor(R.color.actionbar));
	        v.setTextSize(17);
		params.setMargins(width/4, height/2, 0, 0);
		params2.setMargins(width/2, height/2, 0, 0);
		frame.addView(image,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		frame.addView(gp, params);
		frame.addView(v,params2);
		
		frame.setId(CONTENT_VIEW_ID);

		setContentView(frame, tparams);
		if (arg0 == null) {
			new Handler().postDelayed(new Runnable(){    
			    public void run() {    
			    	setInitialFragment();
			    }    
			 }, 2000);  
		
		}

		
		
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = new MenuInflater(this);
        inflator.inflate(R.menu.searchaction, menu);
        searchView = (MenuItem) menu.findItem(R.id.search);  
        
        collection = (MenuItem) menu.findItem(R.id.menu_collect);
        history = (MenuItem) menu.findItem(R.id.menu_history);
        history.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
				startActivity(intent); 
				return true;
			}
		});
        
        collection.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) { 
				
				Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
				startActivity(intent); 
				return true;
			}
		});
        
        searchView.setOnMenuItemClickListener(new OnMenuItemClickListener(){

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(MainActivity.this, SearchActivity.class);
				startActivity(intent); 
				return false;
			}
        	
        });
			
		return super.onCreateOptionsMenu(menu);
	}


	private void init() {
		new  Thread(){
			@Override
			public void run() {
				super.run();
				APP.init(getApplicationContext(),ConStants.app_id,ConStants.app_key).toString();
			}
			
		}.start();			
	}

	/**
	 * 設置主fragment
	 */
	private void setInitialFragment() {

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(CONTENT_VIEW_ID, MainFragment.newInstance())
		.commit(); 
	}
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
}
