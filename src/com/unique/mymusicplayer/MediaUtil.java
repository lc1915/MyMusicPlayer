package com.unique.mymusicplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaUtil {
	//获取专辑封面的Uri
		private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");

		/**
		 * 用于从数据库中查询歌曲的信息，保存在List当中
		 * 
		 * @return
		 */
		public static List<Mp3Info> getMp3Infos(Context context) {
			Cursor cursor = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			
			List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToNext();
				Mp3Info mp3Info = new Mp3Info();
				long id = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media._ID));	//音乐id
				String title = cursor.getString((cursor	
						.getColumnIndex(MediaStore.Audio.Media.TITLE))); // 音乐标题
				String artist = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ARTIST)); // 艺术家
				String album = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM));	//专辑
				String displayName = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
				long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
				long duration = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.DURATION)); // 时长
				long size = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
				String url = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
				int isMusic = cursor.getInt(cursor
						.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
				if (isMusic != 0) { // 只把音乐添加到集合当中
					mp3Info.setId(id);
					mp3Info.setTitle(title);
					mp3Info.setArtist(artist);
					mp3Info.setAlbum(album);
					mp3Info.setDisplayName(displayName);
					mp3Info.setAlbumId(albumId);
					mp3Info.setDuration(duration);
					mp3Info.setSize(size);
					mp3Info.setUrl(url);
					mp3Infos.add(mp3Info);
				}
			}
			return mp3Infos;
		}
		
		/**
		 * 往List集合中添加Map对象数据，每一个Map对象存放一首音乐的所有属性
		 * @param mp3Infos
		 * @return
		 */
		public static List<HashMap<String, String>> getMusicMaps(
				List<Mp3Info> mp3Infos) {
			List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();
			for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
				Mp3Info mp3Info = (Mp3Info) iterator.next();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("title", mp3Info.getTitle());
				map.put("Artist", mp3Info.getArtist());
				map.put("album", mp3Info.getAlbum());
				map.put("displayName", mp3Info.getDisplayName());
				map.put("albumId", String.valueOf(mp3Info.getAlbumId()));
				map.put("size", String.valueOf(mp3Info.getSize()));
				map.put("url", mp3Info.getUrl());
				mp3list.add(map);
			}
			return mp3list;
		}
}
