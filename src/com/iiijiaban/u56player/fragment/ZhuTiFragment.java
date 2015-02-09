package com.iiijiaban.u56player.fragment;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.adapter.ZhuTiAdapter;
import com.iiijiaban.u56player.beans.ZhuTi;
import com.iiijiaban.u56player.ui.VideoViewPlayingActivity;
import com.iiijiaban.u56player.util.JsonUtil;
import com.ijiaban.actionbar.pulltorefreshlib.ActionBarPullToRefresh;
import com.ijiaban.actionbar.pulltorefreshlib.OnRefreshListener;
import com.ijiaban.actionbar.pulltorefreshlib.PullToRefreshLayout;
import com.wole56.sdk.Video;

public class ZhuTiFragment extends Fragment implements OnRefreshListener{
	private PullToRefreshLayout mPullToRefreshLayout;
	private ListView listView; //展示数据的listview
	private ZhuTiAdapter adapter; //
	private String json;  //接收服务器端返回的json数据
	private ArrayList<ZhuTi> zhutis;
	RelativeLayout r;
	
	private boolean isResh; //是否获取数据结束，防止listview滚动到最下面，重复获取数据
	private int page = 0;  
	
	private ArrayList<ZhuTi>  zt;
	@SuppressWarnings("unchecked")
	@Override
	public void onRefreshStarted(View view) {
	page+=1;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				json=Video.searchVideo(getActivity(), "美甲教程", "20", Integer.toString(page)).toString();
			    JsonUtil.getSearch(json, zhutis);
			    Message message=Message.obtain();
			    message.obj=zhutis;
			    handler.sendMessage(message);

			}
		}).start();
		mPullToRefreshLayout.setRefreshComplete();
	} 
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			zt=(ArrayList<ZhuTi>) msg.obj;
			isResh=true; 
			listView.setVisibility(View.GONE);
//			listView.setVisibility(View.GONE);
			adapter.resh(zt);
			listView.setVisibility(View.VISIBLE);
			Toast.makeText(getActivity(), "更新完成", 100).show();
			super.handleMessage(msg);
		}
		
	};
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup viewGroup = (ViewGroup) view; 
        // As we're using a ListFragment we create a PullToRefreshLayout manually
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
        // We can now setup the PullToRefreshLayout
        ActionBarPullToRefresh.from(getActivity())
                // We need to insert the PullToRefreshLayout into the Fragment's ViewGroup
                .insertLayoutInto(viewGroup)
                .theseChildrenArePullable(R.id.zhuti_list)
                .listener(this)
                .setup(mPullToRefreshLayout); 
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.main_zhuti, null);
		listView=(ListView)view.findViewById(R.id.zhuti_list);
		listView.setSelector(R.drawable.griditemselector);
		r=(RelativeLayout)view.findViewById(R.id.adlayyout);
		AdView adView = new AdView(getActivity());
//		 代码设置AppSid和Appsec，此函数必须在AdView实例化前调用
		 AdView.setAppSid(getActivity(),"c3314f57");
		 AdView.setAppSec(getActivity(),"c3314f57");
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
		/**
		 * getdata
		 */
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				zhutis = new ArrayList<ZhuTi>();
				json=Video.searchVideo(getActivity(), "美甲教程", "20", "1").toString();
				JsonUtil.getSearch(json, zhutis);
				
	
			}
		}).run();
		
		
		/**
		 * setdata
		 */
		isResh=true;
		adapter=new ZhuTiAdapter(getActivity(), zhutis);
		setListListener(listView);
		
		
		int s=adapter.getCount();
		
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent();
				intent.putExtra("movie", zhutis.get(position));
				intent.setClass(getActivity(), VideoViewPlayingActivity.class);
				startActivity(intent);
			}
		});
	
		return view;
	}
	
	
	
	private void setListListener(final ListView listView) {
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
               if(firstVisibleItem+visibleItemCount==totalItemCount&&totalItemCount>0)
               {
            	   page+=1;
            	   new Thread(new Runnable() {
					
					@Override
					public void run() {
						zt = new ArrayList<ZhuTi>();
						json=Video.searchVideo(getActivity(), "美甲视频", "20", Integer.toString(page)).toString();
						JsonUtil.getSearch(json, zt);
						Message message=Message.obtain();
						message.obj=zt;
						handler.sendMessage(message);
					}
				}).start();
            	   isResh=false;
               }
			}
		});
		
	} 
	public class Threads extends Thread{
     String page;
		@Override
		public void run() {
			super.run();
			getData(page);
		}


		public Threads(String page) {
			super();
			this.page = page;
		} 
	}
	public void getData(String page) {
		zhutis = new ArrayList<ZhuTi>();
		json=Video.searchVideo(getActivity(), "美甲教程", "20", page.trim()).toString();
		JsonUtil.getSearch(json, zhutis);
	}

 
}
