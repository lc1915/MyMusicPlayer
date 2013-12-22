package com.unique.mymusicplayer;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PlayService0 extends Service {
	static MediaPlayer mediaPlayer; // 媒体播放器对象
	private String path; // 音乐文件路径
	private int msg;
	private boolean isPause; // 暂停状态
	private int current = 0; // 记录当前正在播放的音乐
	private List<Mp3Info> mp3Infos; // 存放Mp3Info对象的集合
	private int status = 3; // 播放状态，默认为顺序播放
	private MyReceiver myReceiver; // 自定义广播接收器
	private int currentTime; // 当前播放进度
	private int duration; // 播放长度
	private LrcProcess mLrcProcess; // 歌词处理
	private List<LrcContent> lrcList = new ArrayList<LrcContent>(); // 存放歌词列表对象
	private int index = 0; // 歌词检索值

	String TAG = "PlayService0";

	// 服务要发送的一些Action
	public static final String UPDATE_ACTION = "com.unique.action.UPDATE_ACTION"; // 更新动作
	public static final String CONTROL_ACTION = "com.unique.action.CTL_ACTION"; // 控制动作
	public static final String MUSIC_CURRENT = "com.unique.action.MUSIC_CURRENT"; // 当前音乐播放时间更新动作
	public static final String MUSIC_DURATION = "com.unique.action.MUSIC_DURATION";// 新音乐长度更新动作
	public static final String SHOW_LRC = "com.unique.action.SHOW_LRC"; // 通知显示歌词

	private static final String START_ACTION = "com.unique.action.START_APP_WIDGET";
	private static final String FRONT_ACTION = "com.unique.action.FRONT_APP_WIDGET";
	private static final String NEXT_ACTION = "com.unique.action.NEXT_APP_WIDGET";
	private static final String PAUSE_ACTION = "com.unique.action.PAUSE_APP_WIDGET";

	// handler用来接收消息，来发送广播更新播放时间
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (mediaPlayer != null) {
					currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
					Intent intent = new Intent();
					intent.setAction(MUSIC_CURRENT);
					intent.putExtra("currentTime", currentTime);
					sendBroadcast(intent); // 给PlayActivity0发送广播
					handler.sendEmptyMessageDelayed(1, 1000);
				}
			}
		};
	};

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service", "service created");
		mediaPlayer = new MediaPlayer();
		mp3Infos = MusicListActivity0.mp3Infos;

		// 设置音乐播放完成时的监听器
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				if (status == 1) { // 单曲循环
					mediaPlayer.start();
				} else if (status == 2) { // 全部循环
					current++;
					if (current > mp3Infos.size() - 1) { // 变为第一首的位置继续播放
						current = 0;
					}
					Intent sendIntent = new Intent(UPDATE_ACTION);
					sendIntent.putExtra("current", current);
					// 发送广播，将被Activity组件中的BroadcastReceiver接收到
					sendBroadcast(sendIntent);
					path = mp3Infos.get(current).getUrl();
					play(0);
				} else if (status == 3) { // 顺序播放
					current++; // 下一首位置
					if (current <= mp3Infos.size() - 1) {
						Intent sendIntent = new Intent(UPDATE_ACTION);
						sendIntent.putExtra("current", current);
						// 发送广播，将被Activity组件中的BroadcastReceiver接收到
						sendBroadcast(sendIntent);
						path = mp3Infos.get(current).getUrl();
						play(0);
					} else {
						mediaPlayer.seekTo(0);
						current = 0;
						Intent sendIntent = new Intent(UPDATE_ACTION);
						sendIntent.putExtra("current", current);
						// 发送广播，将被Activity组件中的BroadcastReceiver接收到
						sendBroadcast(sendIntent);
					}
				} else if (status == 4) { // 随机播放
					current = getRandomIndex(mp3Infos.size() - 1);
					System.out.println("currentIndex ->" + current);
					Intent sendIntent = new Intent(UPDATE_ACTION);
					sendIntent.putExtra("current", current);
					// 发送广播，将被Activity组件中的BroadcastReceiver接收到
					sendBroadcast(sendIntent);
					path = mp3Infos.get(current).getUrl();
					play(0);
				}
			}
		});

		myReceiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(PlayActivity0.CONTROL_ACTION);
		filter.addAction(SHOW_LRC);
		filter.addAction(START_ACTION);
		filter.addAction(FRONT_ACTION);
		filter.addAction(NEXT_ACTION);
		filter.addAction(PAUSE_ACTION);
		registerReceiver(myReceiver, filter);
	}

	// 获取随机位置
	protected int getRandomIndex(int end) {
		int index = (int) (Math.random() * end);
		return index;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("PlayService0", "onStart");
		if (intent.getStringExtra("url") != null) {
			path = intent.getStringExtra("url"); // 歌曲路径
			current = intent.getIntExtra("listPosition", -1); // 当前播放歌曲的在mp3Infos的位置
			msg = intent.getIntExtra("MSG", 0); // 播放信息
			Log.e("PlayService0", "if");
		} else {
			path = PlayActivity0.url;
			msg = intent.getIntExtra("MSG", 0); // 播放信息
			Log.e(TAG, path);
			Log.e(TAG, msg + "");
			Log.e("PlayService0", "else");
		}

		if (msg == Constant.PlayerMsg.PLAY_MSG) { // 直接播放音乐
			play(0);
		} else if (msg == Constant.PlayerMsg.PAUSE_MSG) { // 暂停
			pause();
		} else if (msg == Constant.PlayerMsg.STOP_MSG) { // 停止
			stop();
		} else if (msg == Constant.PlayerMsg.CONTINUE_MSG) { // 继续播放
			resume();
		} else if (msg == Constant.PlayerMsg.PRIVIOUS_MSG) { // 上一首
			previous();
		} else if (msg == Constant.PlayerMsg.NEXT_MSG) { // 下一首
			next();
		} else if (msg == Constant.PlayerMsg.PROGRESS_CHANGE) { // 进度更新
			currentTime = intent.getIntExtra("progress", -1);
			play(currentTime);
		} else if (msg == Constant.PlayerMsg.PLAYING_MSG) {
			handler.sendEmptyMessage(1);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 初始化歌词配置
	 */
	public void initLrc() {
		mLrcProcess = new LrcProcess();
		// 读取歌词文件
		Log.e(TAG, "aaaaaaaaaaaaaaaaaa" + path);
		mLrcProcess.readLRC(path);
		// 传回处理后的歌词文件
		lrcList = mLrcProcess.getLrcList();
		PlayActivity0.lrcView.setmLrcList(lrcList);
		// 切换带动画显示歌词
		PlayActivity0.lrcView.setAnimation(AnimationUtils.loadAnimation(
				PlayService0.this, R.anim.alpha_z));
		handler.post(mRunnable);
	}

	public void initLrc0() {
		mLrcProcess = new LrcProcess();
		// 读取歌词文件
		Log.e(TAG, "aaaaaaaaaaaaaaaaaa" + PlayActivity0.url);
		mLrcProcess.readLRC(PlayActivity0.url);
		// 传回处理后的歌词文件
		lrcList = mLrcProcess.getLrcList();
		PlayActivity0.lrcView.setmLrcList(lrcList);
		// 切换带动画显示歌词
		PlayActivity0.lrcView.setAnimation(AnimationUtils.loadAnimation(
				PlayService0.this, R.anim.alpha_z));
		handler.post(mRunnable);
	}

	Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			PlayActivity0.lrcView.setIndex(lrcIndex());
			PlayActivity0.lrcView.invalidate();
			handler.postDelayed(mRunnable, 100);
		}
	};

	/**
	 * 根据时间获取歌词显示的索引值
	 * 
	 * @return
	 */
	public int lrcIndex() {
		try {
			if (mediaPlayer.isPlaying()) {
				currentTime = mediaPlayer.getCurrentPosition();
				duration = mediaPlayer.getDuration();
			}
		} catch (Exception e) {

		}
		if (currentTime < duration) {
			for (int i = 0; i < lrcList.size(); i++) {
				if (i < lrcList.size() - 1) {
					if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
						index = i;
					}
					if (currentTime > lrcList.get(i).getLrcTime()
							&& currentTime < lrcList.get(i + 1).getLrcTime()) {
						index = i;
					}
				}
				if (i == lrcList.size() - 1
						&& currentTime > lrcList.get(i).getLrcTime()) {
					index = i;
				}
			}
		}
		return index;
	}

	// 播放音乐
	private void play(int currentTime) {
		try {
			initLrc();
			mediaPlayer.reset();// 把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare(); // 进行缓冲
			mediaPlayer
					.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
			handler.sendEmptyMessage(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 暂停音乐
	private void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}

	private void resume() {
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}

	// 上一首
	private void previous() {
		Intent sendIntent = new Intent(UPDATE_ACTION);
		sendIntent.putExtra("current", current);
		Log.e(TAG, current + "");
		// 发送广播，将被Activity组件中的BroadcastReceiver接收到
		sendBroadcast(sendIntent);
		play(0);
	}

	// 下一首
	private void next() {
		Intent sendIntent = new Intent(UPDATE_ACTION);
		sendIntent.putExtra("current", current);
		// 发送广播，将被Activity组件中的BroadcastReceiver接收到
		sendBroadcast(sendIntent);
		play(0);
	}

	// 停止音乐
	static void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}

	}

	// 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
	private final class PreparedListener implements OnPreparedListener {
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start(); // 开始播放
			if (currentTime > 0) { // 如果音乐不是从头播放
				mediaPlayer.seekTo(currentTime);
			}
			Intent intent = new Intent();
			Log.e("PlayService0", "发送duration的广播");
			intent.setAction(MUSIC_DURATION);
			duration = mediaPlayer.getDuration();
			intent.putExtra("duration", duration); // 通过Intent来传递歌曲的总长度
			sendBroadcast(intent);
		}
	}

	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "a");
			int control = intent.getIntExtra("control", -1);
			switch (control) {
			case 1:
				status = 1; // 将播放状态置为1表示：单曲循环
				break;
			case 2:
				status = 2; // 将播放状态置为2表示：全部循环
				break;
			case 3:
				status = 3; // 将播放状态置为3表示：顺序播放
				break;
			case 4:
				status = 4; // 将播放状态置为4表示：随机播放
				break;
			}
			String action = intent.getAction();
			if (action.equals(SHOW_LRC)) {
				current = intent.getIntExtra("listPosition", -1);
				initLrc();
			} else if (action.equals(PAUSE_ACTION)) {
				Log.e(TAG, "pause");
				pause();
			} else if (action.equals(FRONT_ACTION)) {
				Log.e(TAG, "previous");
				try {
					current--;
					String url = mp3Infos.get(current).getUrl();
					Log.e(TAG, url);
					mediaPlayer.reset();// 把各项参数恢复到初始状态
					mediaPlayer.setDataSource(url);
					mediaPlayer.prepare(); // 进行缓冲
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.e(TAG, current + "");
			} else if (action.equals(NEXT_ACTION)) {
				Log.e(TAG, "next");
				try {
					current++;
					Log.e(TAG, current + "current");
					String url = mp3Infos.get(current).getUrl();
					Log.e(TAG, url);
					mediaPlayer.reset();// 把各项参数恢复到初始状态
					mediaPlayer.setDataSource(url);
					mediaPlayer.prepare(); // 进行缓冲
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.e(TAG, current + "");
			} else if (action.equals(START_ACTION)) {
				play(currentTime);
			}

		}
	}

}