package com.unique.mymusicplayer;

import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

//创建一个类appWidgetProvider继承AppWidgetProvider类，重写它的生命周期函数
public class WidgetProvider extends AppWidgetProvider {

	private boolean isPlaying = true; // 正在播放
	private boolean isPause = false; // 暂停
	String TAG = "WidgetProvider";
	// 定义一个常量字符串，该常量用于命名Action(开始/暂停按钮发出的)
	private static final String START_ACTION = "com.unique.action.START_APP_WIDGET";
	// 定义一个常量字符串，该常量用于命名Action(上一首按钮发出的)
	private static final String FRONT_ACTION = "com.unique.action.FRONT_APP_WIDGET";
	// 定义一个常量字符串，该常量用于命名Action(下一首按钮发出的)
	private static final String NEXT_ACTION = "com.unique.action.NEXT_APP_WIDGET";
	// 定义一个常量字符串，该常量用于命名Action(停止按钮发出的)
	private static final String PAUSE_ACTION = "com.unique.action.PAUSE_APP_WIDGET";

	// 每次添加widget到桌面和删除widget时都会调用(经过这个方法后，onReceive会选择调用它的四个生命周期函数)
	@Override
	public void onReceive(Context context, Intent intent) {

		// 当点击widget上的按钮时会发出广播，在这里可以接受这个广播
		// （前提是已经对按钮设置了监听，且在manifest.xml中注册了Action）
		// 当这里接受的Action是我们说自己设置说明点击了按钮，然后根据Action
		// 的不同即可判断点击的是哪个按钮，做相应处理
		String action = intent.getAction();
		if (action.equals("START_ACTION")) {

			// 判断按下widget里中间的播放按钮是开始播放功能还是开始/暂停功能
			if (PlayService0.mediaPlayer == null) {
				int listPosition = MusicListActivity0.listPosition;
				Mp3Info mp3Info = MusicListActivity0.mp3Infos.get(listPosition);
				Intent intent0 = new Intent();
				intent0.setAction("com.unique.media.MUSIC_SERVICE");
				intent0.putExtra("title", mp3Info.getTitle());
				intent0.putExtra("url", mp3Info.getUrl());
				intent0.putExtra("artist", mp3Info.getArtist());
				intent0.putExtra("listPosition", listPosition);
				intent0.putExtra("MSG", Constant.PlayerMsg.PLAY_MSG);
				startService(intent);
			} else if (PlayService0.mediaPlayer != null) {

				if (isPlaying) {
					Intent intent0 = new Intent();
					intent0.setAction("com.unique.media.MUSIC_SERVICE");
					intent0.putExtra("MSG", Constant.PlayerMsg.PAUSE_MSG);
					startService(intent0);
					Log.e(TAG, "11");
					isPlaying = false;
					isPause = true;
				} else if (isPause) {
					Intent intent0 = new Intent();
					intent0.setAction("com.unique.media.MUSIC_SERVICE");
					intent0.putExtra("MSG", Constant.PlayerMsg.CONTINUE_MSG);
					startService(intent0);
					Log.e(TAG, "22");
					isPause = false;
					isPlaying = true;
				}
			}

		} else if (action.equals("FRONT_ACTION")) {

			int listPosition = MusicListActivity0.listPosition - 1;
			if (listPosition >= 0) {
				Mp3Info mp3Info = MusicListActivity0.mp3Infos.get(listPosition); // 上一首MP3
				Intent intent0 = new Intent();
				intent0.setAction("com.unique.media.MUSIC_SERVICE");
				intent0.putExtra("url", mp3Info.getUrl());
				intent0.putExtra("listPosition", listPosition);
				intent0.putExtra("MSG", Constant.PlayerMsg.PRIVIOUS_MSG);
				startService(intent0);
			} else {
				listPosition++;
			}

		} else if (action.equals("NEXT_ACTION")) {

			int listPosition = MusicListActivity0.listPosition + 1;
			List<Mp3Info> mp3Infos = MusicListActivity0.mp3Infos;
			if (listPosition <= mp3Infos.size() - 1) {
				Mp3Info mp3Info = mp3Infos.get(listPosition);
				Intent intent0 = new Intent();
				intent0.setAction("com.unique.media.MUSIC_SERVICE");
				intent0.putExtra("url", mp3Info.getUrl());
				intent0.putExtra("listPosition", listPosition);
				intent0.putExtra("MSG", Constant.PlayerMsg.NEXT_MSG);
				startService(intent);
			} else {
				listPosition--;
			}

		} else {
			super.onReceive(context, intent); // 当不是我们自己发送的时候调用这个
		}

	}

	private void startService(Intent intent) {
		// TODO Auto-generated method stub

	}

	// 每次添加widget到桌面都会调用
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		// 设置按下暂停按钮发出的广播Action
		Intent pauseIntent = new Intent();
		pauseIntent.setAction(PAUSE_ACTION); // 为Intent对象设置Action
		int listPosition = MusicListActivity0.listPosition;
		pauseIntent.putExtra("listPosition", listPosition);
		Log.e(TAG, listPosition + "");
		pauseIntent.putExtra("MSG", Constant.PlayerMsg.PAUSE_MSG);
		// 使用getBroadcast方法得到一个PendingIntent对象，当该对象执行时会发送一个广播
		PendingIntent pausePendingIntent = PendingIntent.getBroadcast(context,
				0, pauseIntent, 0);

		// 设置按下上一首按钮发出的广播Action
		Intent frontIntent = new Intent();
		frontIntent.setAction(FRONT_ACTION); // 为Intent对象设置Action
		Log.e(TAG, listPosition + "");
		int listPosition0 = listPosition - 1;
		frontIntent.putExtra("listPosition", listPosition0);
		frontIntent.putExtra("MSG", Constant.PlayerMsg.PRIVIOUS_MSG);
		// 使用getBroadcast方法得到一个PendingIntent对象，当该对象执行时会发送一个广播
		PendingIntent frontPendingIntent = PendingIntent.getBroadcast(context,
				0, frontIntent, 0);

		// 设置按下下一首按钮发出的广播Action
		Intent nextIntent = new Intent();
		nextIntent.setAction(NEXT_ACTION); // 为Intent对象设置Action
		frontIntent.putExtra("listPosition", listPosition + 1);
		nextIntent.putExtra("MSG", Constant.PlayerMsg.NEXT_MSG);
		// 使用getBroadcast方法得到一个PendingIntent对象，当该对象执行时会发送一个广播
		PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context,
				0, nextIntent, 0);

		// 设置按下播放按钮发出的广播Action
		Intent startIntent = new Intent();
		startIntent.setAction(START_ACTION); // 为Intent对象设置Action
		startIntent.putExtra("listPosition", listPosition);
		Log.e(TAG, listPosition + "");
		startIntent.putExtra("MSG", Constant.PlayerMsg.PLAY_MSG);
		// 使用getBroadcast方法得到一个PendingIntent对象，当该对象执行时会发送一个广播
		PendingIntent startPendingIntent = PendingIntent.getBroadcast(context,
				0, startIntent, 0);

		// 得到RemoteViews对象
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_play);
		// 给RemoteViews注册一个onclick事件，每个按钮点击时发出广播，根据Action的不同即可判别是哪个按钮按下
		remoteViews.setOnClickPendingIntent(R.id.widget_frontBtn,
				frontPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_pauseBtn,
				pausePendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_nextBtn,
				nextPendingIntent);
		remoteViews.setOnClickPendingIntent(R.id.widget_startBtn,
				startPendingIntent);
		// 更新widget
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
	}

	// 添加第一个widget到桌面时调用
	@Override
	public void onEnabled(Context context) {

		System.out.println("onEnabled");
		super.onEnabled(context);
	}

	// 删除widget时调用
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {

		System.out.println("onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

	// 删除最后一个widget时调用
	@Override
	public void onDisabled(Context context) {

		System.out.println("onDisabled");
		super.onDisabled(context);
	}
}