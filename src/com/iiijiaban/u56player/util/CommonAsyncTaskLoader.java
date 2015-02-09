package com.iiijiaban.u56player.util;

import java.util.List;

import com.iiijiaban.u56player.beans.Videos;
import com.iiijiaban.u56player.dao.CollectionDao;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class CommonAsyncTaskLoader extends AsyncTaskLoader<List<Videos>>{

	private CollectionDao videoDao;
	
	public CommonAsyncTaskLoader(Context context) {
		super(context); 
		videoDao = new CollectionDao(context);
	}

	@Override
	public List<Videos> loadInBackground() { 
		List<Videos> list = videoDao.findAllHvideo();
		return list;
	} 
}
