package com.unique.mymusicplayer;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PlayActivity0 extends Activity {

	int position;
	boolean isplay=false;
	private Button playButton, pauseButton, stopButton;
	private Button lastButton, nextButton;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	static String path;
	private int listPosition = MusicListActivity0.listPosition; // 播放歌曲在mp3Infos的位置

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);

		Intent intent = getIntent();
		path = intent.getStringExtra("url");

		Intent intent0 = new Intent();
		intent0.putExtra("url", path);
		Log.e("PlayActivity0", "url:" + path);
		intent0.setClass(PlayActivity0.this, PlayService0.class);
		startService(intent0);

		playButton = (Button) findViewById(R.id.play);
		// pauseButton = (Button) findViewById(R.id.pause);
		// stopButton = (Button) findViewById(R.id.stop);
		lastButton = (Button) findViewById(R.id.last);
		nextButton = (Button) findViewById(R.id.next);

		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isplay) {
					playButton.setBackgroundResource(R.drawable.pause_selector);
					Log.e("aaa", "正在播放");
					Intent intent = new Intent(PlayActivity0.this,
							PlayService0.class);
					intent.putExtra("play_position", 1);
					startService(intent);
					isplay = false;
				} else {
					playButton.setBackgroundResource(R.drawable.play_selector);
					Log.e("aaa", "暂停");
					Intent intent = new Intent(PlayActivity0.this,
							PlayService0.class);
					intent.putExtra("play_position", 2);
					startService(intent);
					isplay = true;
				}
			}
		});

		/*
		 * pauseButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (mediaPlayer != null) {
		 * Intent intent = new Intent(PlayActivity0.this, PlayService0.class);
		 * intent.putExtra("play_position", 2); startService(intent); } } });
		 * 
		 * stopButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (mediaPlayer != null) {
		 * Intent intent = new Intent(PlayActivity0.this, PlayService0.class);
		 * intent.putExtra("play_position", 0); startService(intent); } } });
		 */

		lastButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playButton.setBackgroundResource(R.drawable.pause_selector);
				isplay=false;
				List<Mp3Info> mp3Infos0 = MusicListActivity0.mp3Infos;
				listPosition--;
				if (listPosition >= 0) {
					Mp3Info mp3Info = mp3Infos0.get(listPosition); // 上一首MP3
					// musicTitle.setText(mp3Info.getTitle());
					// musicArtist.setText(mp3Info.getArtist());
					// String url = mp3Info.getUrl();
					Intent intent = new Intent(PlayActivity0.this,
							PlayService0.class);
					// intent.setAction("com.wwj.media.MUSIC_SERVICE");
					path = mp3Info.getUrl();
					intent.putExtra("url", path);
					intent.putExtra("value", 1);
					Log.e("PlayActivity0", "url=" + path);
					intent.putExtra("listPosition", listPosition);
					startService(intent);
				} else {
					Toast.makeText(PlayActivity0.this, "已是第一首歌",
							Toast.LENGTH_SHORT).show();
					listPosition++;
				}

			}
		});

		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playButton.setBackgroundResource(R.drawable.pause_selector);
				isplay=false;
				List<Mp3Info> mp3Infos0 = MusicListActivity0.mp3Infos;
				listPosition++;
				if (listPosition <= mp3Infos0.size() - 1) {
					Mp3Info mp3Info = mp3Infos0.get(listPosition); // 上一首MP3
					// musicTitle.setText(mp3Info.getTitle());
					// musicArtist.setText(mp3Info.getArtist());
					// String url = mp3Info.getUrl();
					Intent intent = new Intent(PlayActivity0.this,
							PlayService0.class);
					// intent.setAction("com.wwj.media.MUSIC_SERVICE");
					path = mp3Info.getUrl();
					intent.putExtra("url", path);
					intent.putExtra("value", 1);
					Log.e("PlayActivity0", "url=" + path);
					intent.putExtra("listPosition", listPosition);
					startService(intent);
				} else {
					Toast.makeText(PlayActivity0.this, "已是最后一首歌",
							Toast.LENGTH_SHORT).show();
					listPosition--;
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		if (mediaPlayer != null)
			mediaPlayer.release();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
