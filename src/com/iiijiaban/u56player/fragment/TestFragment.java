package com.iiijiaban.u56player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.ui.VideoViewPlayingActivity;

public class TestFragment extends Fragment{
private TextView t;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.testchildfragment, container,false);
		t=(TextView)v.findViewById(R.id.testtextid);  
		t.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) { 
				Intent intent = new Intent(getActivity(), VideoViewPlayingActivity.class);
				startActivity(intent);
			}
		});  
		return v;
	}
	

}
