package com.iiijiaban.u56player.fragment;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.ui.CollectionActivity;
import com.iiijiaban.u56player.ui.GoogleMusicDicesDrawable;
import com.iiijiaban.u56player.ui.GoogleProgressBar;
import com.iiijiaban.u56player.ui.HistoryActivity;
import com.ijiaban.actionbar.pulltorefreshlib.OnRefreshListener;
import com.ijiaban.tabslide.PagerSlidingTabStrip;

 /**
 * 主fragment  包含N個子fragment 
 * @author adamin
 *
 */
public class MainFragment extends Fragment implements  OnRefreshListener{
//	private DrawerLayout mDrawerLayout; 
	private ScrollView sv;
	private MyPagerAdapter adapter;
	private ActionBarHelper mActionBar;
	/**viewpager相关 */
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
//	private TextView history;
//	private TextView collect;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		    View view =inflater.inflate(R.layout.mainfragment_layout, container,false);
		    
		    setRetainInstance(true);
		    setHasOptionsMenu(true);
//		    mDrawerLayout=(DrawerLayout)view.findViewById(R.id.drawer_layout);
//		    sv=(ScrollView)view.findViewById(R.id.left_drawer);
		    
//		    history = (TextView) view.findViewById(R.id.histroy);
//		    collect = (TextView) view.findViewById(R.id.collect);
//		    getActivity().getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.rounderconer2));
//		    history.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) { 
//					
//					Intent intent = new Intent(getActivity(), HistoryActivity.class);
//					startActivity(intent); 
//				}
//			}); 
		    tabs=(PagerSlidingTabStrip)view.findViewById(R.id.maintabs);
			tabs.setIndicatorColor(getResources().getColor(R.color.actionbar));
			tabs.setBackgroundColor(getResources().getColor(R.color.white));
			tabs.setDividerColor(getResources().getColor(R.color.actionbar));
			tabs.setTextColor(getResources().getColor(R.color.actionbar
					));
			tabs.setTextSize(30);
			tabs.setUnderlineHeight(6);
			tabs.setIndicatorHeight(15);
			pager = (ViewPager) view.findViewById(R.id.apager);
			 
			
			
			adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
			pager.setOffscreenPageLimit(3);
			pager.setAdapter(adapter);
			pager.getAdapter().notifyDataSetChanged();
			final int pageMargin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
							.getDisplayMetrics());
			pager.setPageMargin(pageMargin);
		    tabs.setViewPager(pager);
		    mActionBar = createActionBarHelper();
		    mActionBar.init();
		    return view;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	private ActionBarHelper createActionBarHelper() {
		return new ActionBarHelper();
	}
	/**
	 *   實例化重新 
	 * @return
	 */
	public static Fragment newInstance() {
		   Fragment fr=new MainFragment();
		   return fr;
	}
/**
 * OnRefreshListene
 * 接口的方法  
 */
	@Override
	public void onRefreshStarted(View view) {
		
	}
	/**
	 * Drawerlayout listener
	 * @author adamin
	 *
	 */
//   public class MainDrawerListener implements DrawerLayout.DrawerListener{
//		
//		@Override
//		public void onDrawerOpened(View drawerView) {
//			mActionBar.onDrawerOpened();
//		}
//
//		@Override
//		public void onDrawerClosed(View drawerView) {
//			mActionBar.onDrawerClosed();
//		}
//
//		@Override
//		public void onDrawerSlide(View drawerView, float slideOffset) {
//		}
//
//		@Override
//		public void onDrawerStateChanged(int newState) {
//		}
//	}
   /**
    * actionbar 类帮助 
    * @author adamin
    *
    */
   private class ActionBarHelper {
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		private ActionBarHelper() {
			mActionBar = getActivity().getActionBar();
		}

		public void init() {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mTitle = mDrawerTitle = getActivity().getTitle();
		}

		/**
		 * When the drawer is closed we restore the action bar state reflecting
		 * the specific contents in view.
		 */
		public void onDrawerClosed() {
			mActionBar.setTitle(mTitle);
		}

		/**
		 * When the drawer is open we set the action bar to a generic title. The
		 * action bar should only contain data relevant at the top level of the
		 * nav hierarchy represented by the drawer, as the rest of your content
		 * will be dimmed down and non-interactive.
		 */
		public void onDrawerOpened() {
			mActionBar.setTitle(mDrawerTitle);
		}

		public void setTitle(CharSequence title) {
			mTitle = title;
		}
	}

   /**
	 * viewpager适配器
	 * 
	 * @author adamin
	 * 
	 */
	public class MyPagerAdapter extends FragmentPagerAdapter {
	

		private ArrayList<Fragment> fragmentList;
		ZhuTiFragment tj2 = new ZhuTiFragment();
		MeifaFragment tj1 = new MeifaFragment();
		ShoushenFragment tj3 = new ShoushenFragment();
		HuaZhuangFragment tj4=new HuaZhuangFragment();
		private final String[] TITLES = { "扎发", "美甲", "瘦身" ,"化妆"};
		
		

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}
		@SuppressWarnings("unchecked")
		@Override
		public Fragment getItem(int position) {
			
			fragmentList = new ArrayList<Fragment>();
			fragmentList.add(tj1);
			fragmentList.add(tj2);
			fragmentList.add(tj3);
			fragmentList.add(tj4);
			return fragmentList.get(position);
		} 
		
	}

		





}
