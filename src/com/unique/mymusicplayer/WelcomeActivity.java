package com.unique.mymusicplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends Activity{
	
	Button playSDcardMusicButton;
	Button playInnerMusicButton;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        
        playSDcardMusicButton = (Button) findViewById(R.id.play_sdcard_music_button);
        playSDcardMusicButton.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MusicListActivity0.class);
				startActivity(intent);
				finish();
        	}
        });
        
        /*playInnerMusicButton = (Button) findViewById(R.id.play_inner_music_button);
        playInnerMusicButton.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
        		Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MusicListActivity.class);
				startActivity(intent);
				finish();
        	}
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
