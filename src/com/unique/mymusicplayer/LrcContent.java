package com.unique.mymusicplayer;

import java.util.Map;

//歌词实体类
public class LrcContent {
	private String title;
	private String artist;
	private String album;
	private String bySomeBody;
	private String offset;
	private Map<Long, String> infos;// 保存歌词信息和时间点一一对应的Map

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getBySomeBody() {
		return bySomeBody;
	}

	public void setBySomeBody(String bySomeBody) {
		this.bySomeBody = bySomeBody;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public Map<Long, String> getInfos() {
		return infos;
	}

	public void setInfos(Map<Long, String> infos) {
		this.infos = infos;
	}

	private String lrcString; // 歌词内容
	private int lrcTime; // 歌词当前时间

	public String getLrcString() {
		return lrcString;
	}

	public void setLrcString(String lrcString) {
		this.lrcString = lrcString;
	}

	public int getLrcTime() {
		return lrcTime;
	}

	public void setLrcTime(int lrcTime) {
		this.lrcTime = lrcTime;
	}
}