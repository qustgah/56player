package com.iiijiaban.u56player.ui;

import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.beans.OneMovieBean;
import com.iiijiaban.u56player.beans.ZhuTi;
import com.iiijiaban.u56player.util.JsonUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wole56.sdk.Video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends Activity{

	private String vid;
	private ZhuTi movieBean;
	private OneMovieBean oBean;
	private ImageView imageView;
	private TextView nameTextView, tagTextView, timetView, pltTextView;
	private Button button;
    private TextView contView;
    private View pView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_movi_info);
		oBean = new OneMovieBean();
		initView();
		getDate();
		getInfo();
		showAd();
	}

	public void play(View view) {
		Intent intent=new Intent();
		intent.putExtra("name", oBean.getTitle());
		intent.putExtra("url", oBean.getUrls());
		intent.setClass(this, VideoViewPlayingActivity.class);
		startActivity(intent);
 	}
   private void showAd()
   {
//	   LinearLayout container =(LinearLayout)findViewById(R.id.AdLinearLayout);
   }
	private void initView() {
//		pView = findViewById(R.id.pb);
		imageView = (ImageView) findViewById(R.id.show_movie_info_imag_id);
		nameTextView = (TextView) findViewById(R.id.show_movie_info_name);
		tagTextView = (TextView) findViewById(R.id.show_movie_tag_name);
//		pltTextView = (TextView) findViewById(R.id.show_movie_pl2);
		timetView = (TextView) findViewById(R.id.show_movie_times);
		contView=(TextView)findViewById(R.id.show_movie_info);

	}

	private void getDate() {
		movieBean = (ZhuTi) getIntent().getSerializableExtra("movie");
		oBean.setTitle(movieBean.getTitle());
		oBean.setTag(movieBean.getTag());
//		oBean.setComments(movieBean.getComments());
		oBean.setContent(movieBean.getContent());
//		oBean.setPublic_time(movieBean.getAddTime());
		oBean.setTotaltime(movieBean.getTotaltime());
		oBean.setId(movieBean.getVid());
		vid=movieBean.getVid();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			setDate();
		};
	};

	private void setDate() {
//		pView.setVisibility(View.GONE);
		ImageLoader.getInstance().displayImage(oBean.getImg(), imageView);
		nameTextView.setText(oBean.getTitle());
		tagTextView.setText(oBean.getTag());
		pltTextView.setText(oBean.getComments());
//		try {
//			timetView.setText(MyTools.changeTime2(Long.parseLong(oBean.getTotaltime())));
//		} catch (Exception e) {
//		}
		contView.setText(oBean.getContent());
	}

	class th extends Thread {
		@Override
		public void run() {
			super.run();
			String string = Video.getVideoAddress(DetailActivity.this,
					vid).toString();
			String jsString=Video.getVideoInfo(DetailActivity.this, vid).toString();
			JsonUtil.getMovieinfo(string, oBean);
			JsonUtil.getInfo(jsString, oBean);
			handler.sendEmptyMessage(1);
		}
	}

	private void getInfo() {
		new th().start();
	}
	private void updateTextViewWithTimeFormat(TextView view, int second) {
		int hh = second / 3600;
		int mm = second % 3600 / 60;
		int ss = second % 60;
		String strTemp = null;
		if (0 != hh) {
			strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
		} else {
			strTemp = String.format("%02d:%02d", mm, ss);
		}
		view.setText(strTemp);
	}
}
