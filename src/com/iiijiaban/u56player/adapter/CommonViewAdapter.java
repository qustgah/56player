package com.iiijiaban.u56player.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonViewAdapter extends BaseAdapter{

	public List<?> datas = new ArrayList<Object>(); 
	
	protected LayoutInflater      _inflater;
	/**
	 * 上下文
	 */
	public Context context;
	public CommonViewAdapter(List<?> list,Context context) {
		// TODO 初始值
		this.datas = list; 
		this.context = context;
		 _inflater = LayoutInflater.from(context);
	} 
	@Override
	public int getCount() {
		// TODO 获取行数
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO  获取某个item的数据
		return datas.get(position);
	} 
	@Override
	public long getItemId(int position) {
		// TODO 获取标识
		return setItemId(position);
	} 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 初始化每一个item
		return setView(convertView, position, parent);
	}
	/**
	 * 初始化 getView()
	 * @param convertView
	 * @param position
	 * @param parent
	 * @return
	 */
	public abstract View setView(View convertView, int position,ViewGroup parent);
	/**
	 * 获取itemid
	 * @param position
	 * @return
	 */
	public abstract long setItemId(int position );
	
	public void refresh(List<?> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}  
	
	public void setData(List<?> list){
		this.datas = list;
		notifyDataSetChanged();
	}
}
