package com.unique.mymusicplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MusicListActivity extends Activity {

	ListView musicListView;
	private SimpleAdapter mAdapter;
	private int listPosition = 0;
	static int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 填充listview
		List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("title", "魔鬼中的天使");
		map.put("Artist", "田馥甄");
		mp3list.add(map);
		HashMap<String, String> map0 = new HashMap<String, String>();
		map0.put("title", "心酸");
		map0.put("Artist", "林宥嘉");
		mp3list.add(map0);
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("title", "分手快乐");
		map1.put("Artist", "梁静茹");
		mp3list.add(map1);
		mAdapter = new SimpleAdapter(this, mp3list,
				android.R.layout.simple_list_item_2, new String[] { "title",
						"Artist" }, new int[] { android.R.id.text1,
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
		i = listPosition;
		Intent intent = new Intent(MusicListActivity.this, PlayActivity.class);
		intent.putExtra("position", i);
		if(i==0){
		intent.putExtra("title", "魔鬼中的天使");     
        intent.putExtra("artist","田馥甄"); 
		}
		else if(i==1){
			intent.putExtra("title", "心酸");     
	        intent.putExtra("artist","林宥嘉"); 
		}
		else if(i==2){
			intent.putExtra("title", "分手快乐");     
	        intent.putExtra("artist","梁静茹"); 
		}
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
