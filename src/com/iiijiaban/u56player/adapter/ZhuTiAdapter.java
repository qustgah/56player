package com.iiijiaban.u56player.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.beans.ZhuTi;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ZhuTiAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ZhuTi> beans;

	public ZhuTiAdapter(Context context, ArrayList<ZhuTi> beans) {
		super();
		this.context = context;
		this.beans = beans;
	}

	public ImageLoader imageloader = ImageLoader.getInstance();
	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.loading) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.loading) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.loading) // 设置图片加载或解码过程中发生错误显示的图片
			.displayer(new RoundedBitmapDisplayer(10)).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build(); // 创建配置过得DisplayImageOption对象

	@Override
	public int getCount() {
		return beans.size();
	}

	@Override
	public Object getItem(int position) {
		return beans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		GetView getView;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_zhuti,
					null);
			getView = new GetView();
			getView.thumb = (ImageView) view.findViewById(R.id.thumb);
			// getView.time=(TextView)view.findViewById(R.id.time);
			getView.title = (TextView) view.findViewById(R.id.title);
			view.setTag(getView);
		} else
			getView = (GetView) view.getTag();
		imageloader.init(ImageLoaderConfiguration.createDefault(context));
		imageloader.displayImage(beans.get(position).getBimg(), getView.thumb,options);
		getView.title.setWidth(getView.thumb.getWidth()-40);
		getView.title.setText(beans.get(position).getTitle());
		// getView.time.setText(beans.get(position).getTotaltime());
		return view;
	}

	public class GetView {
		ImageView thumb;
		ImageView play;
		TextView title;
		TextView time;
	}

	public void resh(ArrayList<ZhuTi> beans) {
		this.beans.addAll(beans);
		this.notifyDataSetChanged();
	}
}
