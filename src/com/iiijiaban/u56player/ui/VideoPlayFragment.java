package com.iiijiaban.u56player.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.baidu.cyberplayer.core.BVideoView.OnSeekCompleteListener;
import com.iiijiaban.u56player.R;
import com.iiijiaban.u56player.beans.OneMovieBean;
import com.iiijiaban.u56player.beans.ZhuTi;
import com.iiijiaban.u56player.dao.CollectionDao;
import com.iiijiaban.u56player.util.JsonUtil;
import com.wole56.sdk.Video;

public class VideoPlayFragment extends Fragment implements OnPreparedListener,
		OnCompletionListener, OnErrorListener, OnInfoListener,
		OnPlayingBufferCacheListener, OnTouchListener, OnSeekCompleteListener {

	private final String TAG = "VideoPlayFragment";
	/**
	 * 您的ak
	 */
	private String AK = "wC9GYh77fYg5mkMcIT3mY7Rz";
	/**
	 * 您的sk的前16位
	 */
	private String SK = "TjlwIkdfjxIrZ7Q79jsKGD4320A57qy9";

	private String mVideoSource = null;

	private ImageButton mPlaybtn = null;
	private ImageButton mPrebtn = null;
	private ImageButton mForwardbtn = null;
	private ImageButton mFullbtn = null;

	private LinearLayout mController = null;

	private SeekBar mProgress = null;
	private TextView mDuration = null;
	private TextView mCurrPostion = null;

	private FrameLayout fLayout;
	private TextView nametTextView;
	private View mVolumeBrightnessLayout;
	/** 当前声音 */
	private int mVolume = -1;
	private int mMaxVolume;
	/** 当前亮度 */
	private float mBrightness = -1f;
	private AudioManager mAudioManager;
	private ImageView mOperationBg;
	private ImageView mOperationPercent;
	private GestureDetector mGestureDetector;
	private boolean isPlay = true;
	private ZhuTi movieBean;
	private OneMovieBean oBean;
	private boolean isfull = false;
	private String vid;
	private CollectionDao videoDao;
	private ImageButton collect;

	/**
	 * 记录播放位置
	 */
	private int mLastPos = 0;

	/**
	 * 播放状态
	 */
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED, PLAYER_COMPLETION
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

	private BVideoView mVV = null;

	private EventHandler mEventHandler;
	private HandlerThread mHandlerThread;

	private final Object SYNC_Playing = new Object();

	private WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "VideoViewPlayingActivity";
	private TextView mtitle; // 视频标题
	private boolean mIsHwDecode = false;

	private final int EVENT_PLAY = 0;
	private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
	private final int UI_EVENT_COMPLETION = 2;
	String s;
	private View view;

	class EventHandler extends Handler {
		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_PLAY:
				/**
				 * 如果已经播放了，等待上一次播放结束
				 */
				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
					synchronized (SYNC_Playing) {
						try {
							SYNC_Playing.wait();
							Log.v(TAG, "wait player status to idle");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				/**
				 * 设置播放url
				 */
				mVV.setVideoPath(mVideoSource);

				/**
				 * 续播，如果需要如此
				 */
				if (mLastPos > 0) {

					mVV.seekTo(mLastPos);
					mLastPos = 0;
				}

				/**
				 * 显示或者隐藏缓冲提示
				 */
				mVV.showCacheInfo(true);
				/**
				 * 开始播放
				 */
				mVV.start();
				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				break;
			default:
				break;
			}
		}
	}

	Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/**
			 * 更新进度及时间
			 */
			case UI_EVENT_UPDATE_CURRPOSITION:
				int currPosition = mVV.getCurrentPosition();
				int duration = mVV.getDuration();
				updateTextViewWithTimeFormat(mCurrPostion, currPosition);
				updateTextViewWithTimeFormat(mDuration, duration);
				mProgress.setMax(duration);
				mProgress.setProgress(currPosition);
				mUIHandler.sendEmptyMessageDelayed(
						UI_EVENT_UPDATE_CURRPOSITION, 200);
				break;
			case UI_EVENT_COMPLETION:
				mPlaybtn.setImageResource(R.drawable.play_btn_style);
				break;
			default:
				break;
			}
		}
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.controllerplaying, null);
		view.setOnTouchListener(this);
		PowerManager pm = (PowerManager) getActivity().getSystemService(
				Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, POWER_LOCK);

		mIsHwDecode = getActivity().getIntent().getBooleanExtra("isHW", false);
		// Uri uriPath = getIntent().getData();
		movieBean = (ZhuTi) getActivity().getIntent().getSerializableExtra("movie");
		oBean = new OneMovieBean();
		oBean.setTitle(movieBean.getTitle());
		oBean.setTag(movieBean.getTag());
		// oBean.setComments(movieBean.getComments());
		oBean.setContent(movieBean.getContent());
		// oBean.setPublic_time(movieBean.getAddTime());
		oBean.setTotaltime(movieBean.getTotaltime());
		oBean.setId(movieBean.getVid());
		vid = movieBean.getVid();
		videoDao = new CollectionDao(getActivity());
		com.iiijiaban.u56player.beans.Videos video = new com.iiijiaban.u56player.beans.Videos();

		video.setTitle(movieBean.getTitle());
		video.setTag(movieBean.getTag());
		video.setMimg(movieBean.getImg());
		video.setTotaltime(movieBean.getTotaltime());
		video.setBimg(movieBean.getBimg());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		video.setDate(str);
		video.setVid(movieBean.getVid());
		video.setType(1);
		videoDao.insertSvideo(video);

		new Thread(new Runnable() {

			@Override
			public void run() {
				String string = Video.getVideoAddress(getActivity(), vid)
						.toString();
				JsonUtil.getMovieinfo(string, oBean);
			}
		}).run();
ArrayList<String> murls=oBean.getUrls();
//		Intent intent=getActivity().getIntent();
//	ArrayList<String> urls=	intent.getStringArrayListExtra("url");
//	String name=intent.getStringExtra("name");

	if(murls.size()==3){
		s=murls.get(2);
	}else
	{
		s=murls.get(0);
	}
    
		Uri uriPath = Uri.parse(s);

		if (null != uriPath) {
			String scheme = uriPath.getScheme();
			if (null != scheme) {
				mVideoSource = uriPath.toString();
			} else {
				mVideoSource = uriPath.getPath();
			}
		}

		initUI();
		mtitle.setText(oBean.getTitle());

		/**
		 * 开启后台事件处理线程
		 */
		mHandlerThread = new HandlerThread("event handler thread",
				Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mEventHandler = new EventHandler(mHandlerThread.getLooper());

		return view;
	};
	/**
	 * 初始化界面
	 */
	private void initUI() {
		mtitle = (TextView) view.findViewById(R.id.movie_name);
		mPlaybtn = (ImageButton) view.findViewById(R.id.play_btn);
		mPrebtn = (ImageButton) view.findViewById(R.id.pre_btn);
		mForwardbtn = (ImageButton) view.findViewById(R.id.next_btn);
		mController = (LinearLayout) view.findViewById(R.id.controlbar);
		mFullbtn = (ImageButton) view.findViewById(R.id.full_btn);
		fLayout = (FrameLayout) view.findViewById(R.id.title_fl);
		nametTextView = (TextView) view.findViewById(R.id.movie_name);
		mVolumeBrightnessLayout = view
				.findViewById(R.id.operation_volume_brightness);
		mAudioManager = (AudioManager) getActivity().getSystemService(
				Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mOperationBg = (ImageView) view.findViewById(R.id.operation_bg);
		mOperationPercent = (ImageView) view
				.findViewById(R.id.operation_percent);
		mGestureDetector = new GestureDetector(getActivity(),
				new MyGestureListener());
		mProgress = (SeekBar) view.findViewById(R.id.media_progress);
		mDuration = (TextView) view.findViewById(R.id.time_total);
		mCurrPostion = (TextView) view.findViewById(R.id.time_current);
		collect = (ImageButton) view.findViewById(R.id.collect);

		registerCallbackForControl();

		/**
		 * 设置ak及sk的前16位
		 */
		BVideoView.setAKSK(AK, SK);

		/**
		 * 获取BVideoView对象
		 */
		mVV = (BVideoView) view.findViewById(R.id.video_view);
		/**
		 * 注册listener
		 */
		mVV.setOnPreparedListener(this);
		mVV.setOnCompletionListener(this);
		mVV.setOnErrorListener(this);
		mVV.setOnInfoListener(this);

		isPlay = true;
		/**
		 * 设置解码模式
		 */
		mVV.setDecodeMode(mIsHwDecode ? BVideoView.DECODE_HW
				: BVideoView.DECODE_SW);
	}

	/**
	 * 为控件注册回调处理函数
	 */
	private void registerCallbackForControl() {
		mPlaybtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mVV.isPlaying()) {
					mPlaybtn.setImageResource(R.drawable.play_btn_style);
					/**
					 * 暂停播放
					 */

					mVV.pause();
					isPlay = false;
				} else if (mPlayerStatus == PLAYER_STATUS.PLAYER_IDLE) {
					if (mEventHandler.hasMessages(EVENT_PLAY))
						mEventHandler.removeMessages(EVENT_PLAY);
					mEventHandler.sendEmptyMessage(EVENT_PLAY);
					mPlaybtn.setImageResource(R.drawable.pause_btn_style);
				} else {
					mPlaybtn.setImageResource(R.drawable.pause_btn_style);
					/**
					 * 继续播放
					 */
					mVV.resume();
					isPlay = true;
				}
			}
		});

		collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				com.iiijiaban.u56player.beans.Videos video = new com.iiijiaban.u56player.beans.Videos();

				video.setTitle(movieBean.getTitle());
				video.setTag(movieBean.getTag());
				video.setMimg(movieBean.getImg());
				video.setTotaltime(movieBean.getTotaltime());
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String str = sdf.format(date);
				video.setDate(str);
				video.setVid(movieBean.getVid());
				video.setBimg(movieBean.getBimg());
				video.setType(0);
				long i = videoDao.insertCVideo(video);
				if (i == -1) {
					Toast.makeText(getActivity(), "收藏已存在", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
				}
			}
		});
		/**
		 * 实现向前几秒播放
		 */
		mPrebtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// /**
				// * 如果已经播放了，等待上一次播放结束
				// */
				// if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
				// mVV.stopPlayback();
				// }
				// /**
				// * 发起一次新的播放任务
				// */

				//
				mVV.pause();
				if (mVV != null) {
					long size = mVV.getCurrentPosition() - mVV.getDuration()
							/ 10;
					mVV.seekTo(size);
					mVV.resume();
				}
			}
		});

		mForwardbtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mVV.pause();
				if (mVV != null) {
					long size = mVV.getCurrentPosition() + mVV.getDuration()
							/ 10;
					mVV.seekTo(size);
					mVV.resume();
					// mVV.start();
				}
			}
		});
		mFullbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isfull) {
					mFullbtn.setImageDrawable(getResources().getDrawable(
							R.drawable.full_btn_style));
					getActivity().getWindow().clearFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
					getActivity().setRequestedOrientation(
							ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					getActivity().findViewById(R.id.test).setLayoutParams(
							new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT, 750));
					isfull = false;
				} else {
					mFullbtn.setImageDrawable(getResources().getDrawable(
							R.drawable.fill_btn_style));
					getActivity().getWindow().setFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
					getActivity().setRequestedOrientation(
							ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					getActivity().findViewById(R.id.test).setLayoutParams(
							new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT));

					isfull = true;
				}
			}
		});

		OnSeekBarChangeListener osbc1 = new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				updateTextViewWithTimeFormat(mCurrPostion, progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				/**
				 * SeekBar开始seek时停止更新
				 */
				mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int iseekPos = seekBar.getProgress();
				/**
				 * SeekBark完成seek时执行seekTo操作并更新界面
				 * 
				 */
				mVV.seekTo(iseekPos);
				Log.v(TAG, "seek to " + iseekPos);
				mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
			}
		};
		mProgress.setOnSeekBarChangeListener(osbc1);
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/**
		 * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		 */
		if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
			mLastPos = mVV.getCurrentPosition();
			mVV.stopPlayback();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onResume");
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		/**
		 * 发起一次播放任务,当然您不一定要在这发起
		 */
		mEventHandler.sendEmptyMessage(EVENT_PLAY);
	}

	private long mTouchTime;
	private boolean barShow = false;

	private class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			float mOldX = e1.getX(), mOldY = e1.getY();
			int y = (int) e2.getRawY();
			Display disp = getActivity().getWindowManager().getDefaultDisplay();
			int windowWidth = disp.getWidth();
			int windowHeight = disp.getHeight();

			if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
				onVolumeSlide((mOldY - y) / windowHeight);
			else if (mOldX < windowWidth / 5.0)// 左边滑动
				onBrightnessSlide((mOldY - y) / windowHeight);

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	private void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = getActivity().getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}
		WindowManager.LayoutParams lpa = getActivity().getWindow()
				.getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		getActivity().getWindow().setAttributes(lpa);

		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = (int) (view.findViewById(R.id.operation_full)
				.getLayoutParams().width * lpa.screenBrightness);
		mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;
		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = view.findViewById(R.id.operation_full).getLayoutParams().width
				* index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	public void updateControlBar(boolean show) {

		if (show) {
			fLayout.setVisibility(View.VISIBLE);
			mController.setVisibility(View.VISIBLE);
			getActivity().getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			fLayout.setVisibility(View.INVISIBLE);
			mController.setVisibility(View.INVISIBLE);
			getActivity().getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		}
		barShow = show;
	}

	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;
		mVolumeBrightnessLayout.setVisibility(View.GONE);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		/**
		 * 退出后台事件处理线程
		 */
		mHandlerThread.quit();
	}

	@Override
	public boolean onInfo(int what, int extra) {
		// TODO Auto-generated method stub
		switch (what) {
		/**
		 * 开始缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_START:
			break;
		case BVideoView.MEDIA_INFO_BUFFERING_END:

			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
	 */
	@Override
	public void onPlayingBufferCache(int percent) {
		// TODO Auto-generated method stub

	}

	/**
	 * 播放出错
	 */
	@Override
	public boolean onError(int what, int extra) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onError");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
		return true;
	}

	/**
	 * 准备播放就绪
	 */
	@Override
	public void onPrepared() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
		mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 触摸控制
		if (mGestureDetector.onTouchEvent(event))
			return true;
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			mTouchTime = System.currentTimeMillis();
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			long time = System.currentTimeMillis() - mTouchTime;
			if (time < 400) {
				updateControlBar(!barShow);
			} else {
				endGesture();
			}
		}
		return true;
	}

	@Override
	public void onSeekComplete() {
		// 跳转的问题
		mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
	}

	/**
	 * 播放完成
	 */
	@Override
	public void onCompletion() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onCompletion");
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		mUIHandler.sendEmptyMessage(UI_EVENT_COMPLETION);
	}

}