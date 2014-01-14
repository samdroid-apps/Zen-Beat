/*
  This file is part of Zen Beat.

    Zen Beat is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Zen Beat is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Zen Beat.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.game.zen.beat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class MenuActivity extends Activity{
	public static final String PREFS_NAME = "ZenBeatPrefs";
	boolean hasPlayed;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.just_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void relaunch () {
		if (hasPlayed) {
			Intent intent = new Intent(this, GameActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(this, HowToSlider.class);
			startActivity(intent);
		}
	}
	private OnClickListener Oclplay = new OnClickListener() {
		public void onClick(View v) {
			relaunch();
			
		}
	};
	private OnClickListener Oclsettings = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
			startActivity(intent);
		}
	};
	private OnClickListener OclHelp = new OnClickListener() {
		public void onClick(View v) {
			    	Intent intent = new Intent(getBaseContext(), HowToSlider.class);
					startActivity(intent);
        }
    };
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_interface);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        ImageView play = (ImageView) findViewById(R.id.play);
        play.setOnClickListener(Oclplay);

        ImageView help = (ImageView) findViewById(R.id.imageView);
        help.setOnClickListener(OclHelp);
	}
	public void onResume() {
		super.onResume();
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    float userPoints = settings.getFloat("userPoints", 0f);
	    float level = settings.getFloat("level", 0f);
	    hasPlayed = settings.getBoolean("HasPlayed", false);
	    level++;
	    
		float progress = (userPoints/8)*100;
		//Log.i("Points",Integer.toString((int)progress));
		//ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar2);
		//Progress.setProgress((int)progress);
		
		//TextView ltv = (TextView) findViewById(R.id.levelText);
		//ltv.setText("Level " + Integer.toString((int)level));
	}
	
	
}
