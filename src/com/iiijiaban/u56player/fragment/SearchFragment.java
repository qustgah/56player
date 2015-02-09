package com.iiijiaban.u56player.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.adapter.ZhuTiAdapter;
import com.iiijiaban.u56player.beans.ZhuTi;
import com.iiijiaban.u56player.ui.VideoViewPlayingActivity;
import com.iiijiaban.u56player.util.JsonUtil;
import com.wole56.sdk.Video;

public class SearchFragment extends Fragment {
	private ListView videoList;
	private ArrayList<ZhuTi> mList;
	private String video;
	private ZhuTiAdapter vAdapter;
	private String searchText;
	private int page = 1;
	private boolean isresh;
    private Context context;
    private ArrayList<ZhuTi> vList;
  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.searchfragment, container, false);
		context = getActivity();
		searchText = getArguments().getString("SearchText");
		videoList = (ListView) view.findViewById(R.id.searchlist);
		mList = new ArrayList<ZhuTi>();
		init();
		
		return view;
		
	}
	
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			vList = (ArrayList<ZhuTi>) msg.obj;
			isresh = true;
			videoList.setVisibility(View.GONE);
			vAdapter.resh(vList);
			videoList.setVisibility(View.VISIBLE);
			
		}
		
    };
	
	private void init(){
		
        new Thread(new Runnable() {

			@Override
			public void run() {

				
				video=Video.searchVideo(context, searchText, "10", "0").toString();
				JsonUtil.getSearch(video, mList);
			}
		}).run();
        
        vAdapter = new ZhuTiAdapter(context, mList);
        videoList.setAdapter(vAdapter);
        
        
        videoList.setFocusable(true);
		videoList.setFocusableInTouchMode(true);
        videoList.requestFocus();
        
        videoList.setOnItemClickListener(new OnItemClickListener(){
        	

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				
				videoList.setFocusableInTouchMode(true);
	            videoList.requestFocus();
	            
				Intent intent = new Intent(context,
						VideoViewPlayingActivity.class);
				intent.putExtra("movie", mList.get(position));
				
				startActivity(intent);
	
			}
			
		});
        
	
		
		videoList.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0){
					isresh = true;
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
				if(isresh == true && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
					page+=1;
					new Thread(new Runnable(){

						@Override
						public void run() {

							vList = new ArrayList<ZhuTi>();
							video = Video.searchVideo(context, searchText, 
									"10", Integer.toString(page)).toString();

							JsonUtil.getSearch(video, vList);
							Message message = Message.obtain();
							message.obj = vList;

							handler.sendMessage(message);
						}
						
					}).start();
					
				}
			}
					
		});
	}
	
}
