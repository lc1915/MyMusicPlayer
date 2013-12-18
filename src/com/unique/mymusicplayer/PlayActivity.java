package com.unique.mymusicplayer;

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
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {

	int position;
	private Button playButton;
	private Button lastButton, nextButton;
	TextView titleTextView;
	TextView artistTextView;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	boolean isplay = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music);

		int i = MusicListActivity.i;
		Intent intent0 = new Intent(PlayActivity.this, PlayService.class);
		intent0.putExtra("position", i);
		startService(intent0);

		Intent intent = getIntent();
		titleTextView=(TextView)findViewById(R.id.musicArtist);
		titleTextView.setText(intent.getStringExtra("title"));
		artistTextView=(TextView)findViewById(R.id.musicTitle);
		artistTextView.setText(intent.getStringExtra("artist"));
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
					Intent intent = new Intent(PlayActivity.this,
							PlayService.class);
					intent.putExtra("play_position", 1);
					startService(intent);
					isplay = false;
				} else {
					playButton.setBackgroundResource(R.drawable.play_selector);
					Log.e("aaa", "暂停");
					Intent intent = new Intent(PlayActivity.this,
							PlayService.class);
					intent.putExtra("play_position", 2);
					startService(intent);
					isplay = true;
				}
			}
		});

		lastButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playButton.setBackgroundResource(R.drawable.pause_selector);
				isplay = false;
				Intent intent = new Intent(PlayActivity.this, PlayService.class);
				int i = PlayService.i;
				i--;
				if (i < 0) {
					Toast.makeText(getApplicationContext(), "已是第一首歌",
							Toast.LENGTH_LONG).show();
					i=0;
				} else {
					intent.putExtra("position", i);
					startService(intent);
				}
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				playButton.setBackgroundResource(R.drawable.pause_selector);
				isplay = false;
				Intent intent = new Intent(PlayActivity.this, PlayService.class);
				int i = PlayService.i;
				i++;
				if (i > 2) {
					Toast.makeText(getApplicationContext(), "已是最后一首歌",
							Toast.LENGTH_LONG).show();
					i=2;
				} else {
					intent.putExtra("position", i);
					startService(intent);
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
