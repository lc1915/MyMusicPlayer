package com.unique.mymusicplayer;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

public class PlayService0 extends Service {

	private MediaPlayer mediaPlayer = new MediaPlayer(); // 媒体播放器对象
	String path; // 音乐文件路径
	private boolean isPause; // 暂停状态

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mediaPlayer.isPlaying()) {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				try {
					mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		int play_position = intent.getIntExtra("play_position", -1);
		int value = intent.getIntExtra("value", 0);

		if (value == 0) {
			path = PlayActivity0.path;
			Log.e("PlayService0", "url:" + path);
			try {
				mediaPlayer.setDataSource(path);
				Log.e("PlayService0aaaaaaaaaa", "url:" + path);
				mediaPlayer.prepare(); // 进行缓冲
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mediaPlayer.start();

			if (play_position == 1) {
				mediaPlayer.start();
Log.e("aaaaaaaa", "播放");
			} else if (play_position == 2) {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					Log.e("aaaaaaaa", "暂停");
				}
			}

		} else if (value == 1) {
			path = PlayActivity0.path;
			Log.e("PlayService0", "url:" + path);
			try {
				mediaPlayer.reset();// 把各项参数恢复到初始状态
				mediaPlayer.setDataSource(path);
				Log.e("PlayService0bbbbbbbb", "url:" + path);
				mediaPlayer.prepare(); // 进行缓冲
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			mediaPlayer.start();

			if (play_position == 1) {
				mediaPlayer.start();
				Log.e("aaaaaaaa", "播放");

			} else if (play_position == 2) {
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					Log.e("aaaaaaaa", "暂停");
				}
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 播放音乐
	 * 
	 * @param position
	 */
	private void play(int position) {
		try {
			mediaPlayer.reset();// 把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare(); // 进行缓冲
			mediaPlayer.setOnPreparedListener(new PreparedListener(position));// 注册一个监听器
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 暂停音乐
	 */
	private void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}

	/**
	 * 停止音乐
	 */
	private void stop() {
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
		}
	}

	/**
	 * 
	 * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
	 * 
	 */
	private final class PreparedListener implements OnPreparedListener {
		private int positon;

		public PreparedListener(int positon) {
			this.positon = positon;
		}

		public void onPrepared(MediaPlayer mp) {
			mediaPlayer.start(); // 开始播放
			if (positon > 0) { // 如果音乐不是从头播放
				mediaPlayer.seekTo(positon);
			}
		}
	}

}
