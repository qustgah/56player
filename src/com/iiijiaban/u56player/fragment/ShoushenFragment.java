package com.iiijiaban.u56player.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.adapter.ZhuTiAdapter;
import com.iiijiaban.u56player.beans.ZhuTi;
import com.iiijiaban.u56player.ui.DetailActivity;
import com.iiijiaban.u56player.ui.VideoViewPlayingActivity;
import com.iiijiaban.u56player.util.JsonUtil;
import com.ijiaban.actionbar.pulltorefreshlib.ActionBarPullToRefresh;
import com.ijiaban.actionbar.pulltorefreshlib.OnRefreshListener;
import com.ijiaban.actionbar.pulltorefreshlib.PullToRefreshLayout;
import com.wole56.sdk.Video;

public class ShoushenFragment extends Fragment implements OnRefreshListener{


	private PullToRefreshLayout mPullToRefreshLayout;
	private ListView listView; //展示数据的listview
	private ZhuTiAdapter adapter; //
	private String json;  //接收服务器端返回的json数据
	private ArrayList<ZhuTi> zhutis;
	private View pView; //转圈圈view
	private boolean isResh; //是否获取数据结束，防止listview滚动到最下面，重复获取数据
	private int postion;
	private int page = 0;  
	private int width;
	@SuppressWarnings("unchecked")
	@Override
	public void onRefreshStarted(View view) {
	page+=1;
		// TODO Auto-generated catch block
		new Thread(new Runnable() {
			
			@Override
			public void run() {
	  json=Video.searchVideo(getActivity(), "现代舞教学", "15", Integer.toString(page)).toString();
	    JsonUtil.getSearch(json, zhutis);
	    Message message=Message.obtain();
	    message.obj=zhutis;
	    handler.sendMessage(message);

   

			}
		}).start();
		mPullToRefreshLayout.setRefreshComplete();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup viewGroup = (ViewGroup) view; 
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
        ActionBarPullToRefresh.from(getActivity())
                .insertLayoutInto(viewGroup)
                .theseChildrenArePullable(R.id.zhuti_list)
                .listener(this)
                .setup(mPullToRefreshLayout); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.main_zhuti, null);
		listView=(ListView)view.findViewById(R.id.zhuti_list);
		/**
		 * getdata
		 */
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				zhutis = new ArrayList<ZhuTi>();
				json=Video.searchVideo(getActivity(), "瑜伽教学", "15", "1").toString();
				JsonUtil.getSearch(json, zhutis);
				
	
			}
		}).run();
//	    new Threads(Integer.toString(page)).start();
	
		adapter=new ZhuTiAdapter(getActivity(), zhutis);
	
		/**
		 * setdata
		 */
		isResh=true;
		int s=adapter.getCount();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				long i=	System.currentTimeMillis();
				String s="https://huaban.com/favorite/modeling_hair/?limit=50&max=10&t="+i;
				String ur = "http://www.duitang.com/album/1733789/masn/p/" + 1 + "/24/";
				intent.putExtra("movie", zhutis.get(position));
				intent.setClass(getActivity(), VideoViewPlayingActivity.class);
				startActivity(intent);
			}
		});
//		setListListener(listView);
		return view;
	}
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			ArrayList<ZhuTi>  zt=(ArrayList<ZhuTi> )msg.obj;
			isResh=true;
			adapter.resh(zhutis);
			Toast.makeText(getActivity(), "更新完成", 1000).show();
			super.handleMessage(msg);
		}
		
	};
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
		json=Video.searchVideo(getActivity(), "瑜伽教学", "15", page.trim()).toString();
		JsonUtil.getSearch(json, zhutis);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


}
