package com.iiijiaban.u56player.ui; 

import main.java.com.github.pedrovgs.DraggablePanel;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.fragment.DetailFragment;
import com.iiijiaban.u56player.fragment.VideoDetailFragment;

public class VideoViewPlayingActivity extends FragmentActivity	{ 
	DraggablePanel draggablePanel;
	VideoDetailFragment detailfragment;
	VideoPlayFragment videoplay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
//		draggablePanel=(DraggablePanel)findViewById(R.id.zhutidrag);
//		detailfragment=new VideoDetailFragment();
//		videoplay=new VideoPlayFragment();
//		draggablePanel.setFragmentManager(getSupportFragmentManager());
//		draggablePanel.setTopFragment(videoplay);
//		draggablePanel.setBottomFragment(detailfragment);
//		draggablePanel.initializeView();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Fragment fragment = new VideoPlayFragment();
		fragmentTransaction.replace(R.id.test,fragment).commit();  
	}
}
