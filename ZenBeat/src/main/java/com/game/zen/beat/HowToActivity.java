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

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class HowToActivity extends ActionBarActivity {
	public static final String PREFS_NAME = "ZenBeatPrefs";
	MediaPlayer mp;
	protected float level;
    protected int sound1, sound2, sound3, sound4, sound5, sound6, sound7, sound8;
	protected SoundPool sp;
    protected static final int vol = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.try_notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_to_game:
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                Intent intent1 = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent1);
                return true;
            default: break;
        }
        return true;
    }

    private OnClickListener Ocl1 = new OnClickListener() {
		public void onClick(View v) {
				sp.play(sound1, vol, vol, 1, 0, 1f);
		}
	};
	private OnClickListener Ocl2 = new OnClickListener() {
		public void onClick(View v) {
				sp.play(sound2, vol, vol, 1, 0, 1f);
		}
	};
	private OnClickListener Ocl3 = new OnClickListener() {
		public void onClick(View v) {
			if ( level >= 2) {
				sp.play(sound3, vol, vol, 1, 0, 1f);
			}
		}
	};
	private OnClickListener Ocl4 = new OnClickListener() {
		public void onClick(View v) {
			if ( level >= 2) {
				sp.play(sound4, vol, vol, 1, 0, 1f);
			}
		}
	};
	private OnClickListener Ocl5 = new OnClickListener() {
		public void onClick(View v) {
			if ( level >= 7) {
				sp.play(sound5, vol, vol, 1, 0, 1f);
			}
		}
	};
	private OnClickListener Ocl6 = new OnClickListener() {
		public void onClick(View v) {
			if ( level >= 7) {
				sp.play(sound6, vol, vol, 1, 0, 1f);
			}
		}	
	};
	private OnClickListener Ocl7 = new OnClickListener() {
		public void onClick(View v) {
			if ( level >= 11) {
				sp.play(sound7, vol, vol, 1, 0, 1f);
			}
		}
	};
	private OnClickListener Ocl8 = new OnClickListener() {
		public void onClick(View v) {
			if ( level >= 11) {
				sp.play(sound8, vol, vol, 1, 0, 1f);
			}
		}
	};
	private OnClickListener Oclreplay = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(intent);
		}
	};
	private OnCompletionListener Ocl = new OnCompletionListener() {
		public void onCompletion(MediaPlayer media) {
			mp.release();
			dismissDialog(0);
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ht);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    level = settings.getFloat("level", 0f);
		
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		ImageView b1 = (ImageView)findViewById(R.id.b1);
		b1.setOnClickListener(Ocl1);
		ImageView b2 = (ImageView)findViewById(R.id.b2);
		b2.setOnClickListener(Ocl2);
		ImageView b3 = (ImageView)findViewById(R.id.b3);
		b3.setOnClickListener(Ocl3);
		ImageView b4 = (ImageView)findViewById(R.id.b4);
		b4.setOnClickListener(Ocl4);
		ImageView b5 = (ImageView)findViewById(R.id.b5);
		b5.setOnClickListener(Ocl5);
		ImageView b6 = (ImageView)findViewById(R.id.b6);
		b6.setOnClickListener(Ocl6);
		ImageView b7 = (ImageView)findViewById(R.id.b7);
		b7.setOnClickListener(Ocl7);
		ImageView b8 = (ImageView)findViewById(R.id.b8);
		b8.setOnClickListener(Ocl8);
		
		if (level < 11) {
			 b7.setVisibility(View.GONE);//8 = GONE
			 b7.setImageResource(R.drawable.trans);
			 b8.setVisibility(View.GONE);//8 = GONE
			 b8.setImageResource(R.drawable.trans);
		 }
		if (level < 7) {
			 b5.setVisibility(View.GONE);//8 = GONE
			 b5.setImageResource(R.drawable.trans);
			 b6.setVisibility(View.GONE);//8 = GONE
			 b6.setImageResource(R.drawable.trans);
		 }
		if (level < 2) {
			b3.setVisibility(View.GONE);//8 = GONE
			b3.setImageResource(R.drawable.trans);
			b4.setVisibility(View.GONE);//8 = GONE
			b4.setImageResource(R.drawable.trans);
		}
		
		sound2= sp.load(this, R.raw.s1, 1);
		sound1= sp.load(this, R.raw.s2, 1);
		sound4= sp.load(this, R.raw.s3, 1);
		sound3= sp.load(this, R.raw.s4, 1);
		sound6= sp.load(this, R.raw.s5, 1);
		sound5= sp.load(this, R.raw.s6, 1);
		sound8= sp.load(this, R.raw.s7, 1);
		sound7= sp.load(this, R.raw.s8, 1);
	}
}
