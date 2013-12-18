package com.unique.mymusicplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MusicListActivity0 extends Activity {

	ListView musicListView;
	private SimpleAdapter mAdapter;
	static int listPosition = 0;
	static int i;
	String duration;
	static List<Mp3Info> mp3Infos;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		mp3Infos = new ArrayList<Mp3Info>();
		for (int i = 0; i < cursor.getCount(); i++) {
			Mp3Info mp3Info = new Mp3Info();
			cursor.moveToNext();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
			String title = cursor.getString((cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
			if (isMusic != 0) { // 只把音乐添加到集合当中
				mp3Info.setId(id);
				mp3Info.setTitle(title);
				mp3Info.setArtist(artist);
				mp3Info.setDuration(duration);
				mp3Info.setSize(size);
				mp3Info.setUrl(url);
				mp3Infos.add(mp3Info);
			}
		}

		List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("title", mp3Info.getTitle());
			map.put("artist", mp3Info.getArtist());
			duration=formatTime(mp3Info.getDuration());
			map.put("duration", duration);
			map.put("size", String.valueOf(mp3Info.getSize()));
			map.put("url", mp3Info.getUrl());
			mp3list.add(map);
		}
		
		 
		//在列表中显示歌曲标题和时长
		mAdapter = new SimpleAdapter(this, mp3list,
				android.R.layout.simple_list_item_2, new String[] { "title",
						"duration" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		musicListView = (ListView) findViewById(R.id.music_listview);
		musicListView.setAdapter(mAdapter);

		// 给listview的每一个item添加点击事件
		musicListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listPosition = position;  
	            playMusic(listPosition); 
			}
		});

		// mp3Infos = MediaUtil.getMp3Infos(getApplicationContext()); //获取歌曲对象集合
		// setListAdpter(MediaUtil.getMusicMaps(mp3Infos)); //显示歌曲列表
	}

	public void playMusic(int listPosition) {
		if (mp3Infos != null) {  
            Mp3Info mp3Info = mp3Infos.get(listPosition);   
            Intent intent = new Intent(MusicListActivity0.this, PlayActivity0.class); 
            intent.putExtra("title", mp3Info.getTitle());     
            intent.putExtra("url", mp3Info.getUrl());  
            intent.putExtra("artist", mp3Info.getArtist());  
            intent.putExtra("listPosition", listPosition);  
            intent.putExtra("MSG", Constant.PlayerMsg.PLAY_MSG);  
            startActivity(intent);  
        }  
	}
	
	//格式化时间，将毫秒转换为分:秒格式 
    public static String formatTime(long time) {  
        String min = time / (1000 * 60) + "";  
        String sec = time % (1000 * 60) + "";  
        if (min.length() < 2) {  
            min = "0" + time / (1000 * 60) + "";  
        } else {  
            min = time / (1000 * 60) + "";  
        }  
        if (sec.length() == 4) {  
            sec = "0" + (time % (1000 * 60)) + "";  
        } else if (sec.length() == 3) {  
            sec = "00" + (time % (1000 * 60)) + "";  
        } else if (sec.length() == 2) {  
            sec = "000" + (time % (1000 * 60)) + "";  
        } else if (sec.length() == 1) {  
            sec = "0000" + (time % (1000 * 60)) + "";  
        }  
        return min + ":" + sec.trim().substring(0, 2);  
    }  
}
