package com.unique.mymusicplayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

//处理歌词的类 
public class LrcProcess {
	private List<LrcContent> lrcList; // List集合存放歌词内容对象
	private LrcContent mLrcContent; // 声明一个歌词内容对象
	String TAG = "LrcProcess";

	// 无参构造函数用来实例化对象
	public LrcProcess() {
		mLrcContent = new LrcContent();
		lrcList = new ArrayList<LrcContent>();
	}

	// 读取歌词
	public String readLRC(String path) {
		// 定义一个StringBuilder对象，用来存放歌词内容
		StringBuilder stringBuilder = new StringBuilder();
		File file = new File(path.replace(".mp3", ".lrc"));
		Log.e(TAG, path.replace(".mp3", ".lrc"));

		try {
			// 创建一个文件输入流对象
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "utf-8");
			@SuppressWarnings("resource")
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				// 获取歌曲名信息
				if (line.startsWith("[ti:")) {
					String title = line.substring(4, line.length() - 1);
					Log.i("", "title-->" + title);
				}
				// 取得歌手信息
				else if (line.startsWith("[ar:")) {
					String artist = line.substring(4, line.length() - 1);
					Log.i("", "artist-->" + artist);
				}
				// 取得专辑信息
				else if (line.startsWith("[al:")) {
					String album = line.substring(4, line.length() - 1);
					Log.i("", "album-->" + album);
				}
				// 取得歌词制作者
				else if (line.startsWith("[by:")) {
					String bysomebody = line.substring(4, line.length() - 1);
					Log.i("", "by-->" + bysomebody);
				}
				// 通过正则表达式取得每句歌词信息
				else {
					// 替换字符
					line = line.replace("[", "");
					line = line.replace("]", "@");

					// 分离“@”字符
					String splitLrcData[] = line.split("@");
					if (splitLrcData.length > 1) {
						mLrcContent.setLrcString(splitLrcData[1]);

						// 处理歌词取得歌曲的时间
						int lrcTime = time2Str(splitLrcData[0]);

						mLrcContent.setLrcTime(lrcTime);

						// 添加进列表数组
						lrcList.add(mLrcContent);

						// 新创建歌词内容对象
						mLrcContent = new LrcContent();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			stringBuilder.append("没有找到歌词文件");
		} catch (IOException e) {
			e.printStackTrace();
			stringBuilder.append("读取不到歌词");
		}
		return stringBuilder.toString();
	}

	//解析歌词
	public int time2Str(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");

		String timeData[] = timeStr.split("@"); // 将时间分隔成字符串数组

		// 分离出分、秒并转换为整型
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);

		// 计算上一行与下一行的时间转换为毫秒数
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}

	public List<LrcContent> getLrcList() {
		return lrcList;
	}
}