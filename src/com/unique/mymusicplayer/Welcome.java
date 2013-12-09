package com.unique.mymusicplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

public class Welcome extends Activity {

	@SuppressLint("WorldReadableFiles")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences0 = this.getSharedPreferences("share", MODE_PRIVATE);    
        
        boolean isFirstRun = sharedPreferences0.getBoolean("isFirstRun", true);    
        Editor editor0 = sharedPreferences0.edit();    
            
        if (isFirstRun){    
            Log.e("debug", "第一次运行");    
            editor0.putBoolean("isFirstRun", false);    
            editor0.commit();    
            Intent intent = new Intent();  
            intent.setClass(Welcome.this,WelcomeActivity.class);  
            startActivity(intent);  
            finish();
        } else {    
            Log.e("debug", "不是第一次运行");   
            Intent intent = new Intent();  
            intent.setClass(Welcome.this,WelcomeActivity.class);  
            startActivity(intent);  
            finish();
        }    
	}
}