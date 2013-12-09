package com.unique.mymusicplayer;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

@SuppressLint("NewApi")
public class PlayService extends Service {
	private MediaPlayer mediaPlayer = new MediaPlayer(); // 媒体播放器对象
	static int i;

	@Override
	public IBinder onBind(Intent arg0) {
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
		int position = intent.getIntExtra("position", -1);
		i=position;
		int play_position = intent.getIntExtra("play_position", -1);

		if (i == 0)
			mediaPlayer = MediaPlayer.create(this, R.raw.a1);
		else if (i == 1)
			mediaPlayer = MediaPlayer.create(this, R.raw.a2);
		else if (i == 2)
			mediaPlayer = MediaPlayer.create(this, R.raw.a3);

		mediaPlayer.start();

		if (play_position == 1) {
			mediaPlayer.start();
			
		} else if (play_position == 2) {
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				mediaPlayer.pause();
			}
		} else if (play_position == 0) {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
					try {
						mediaPlayer.prepare();
						mediaPlayer.seekTo(0);
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
	}

}