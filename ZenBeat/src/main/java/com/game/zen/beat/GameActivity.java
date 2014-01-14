/*This file is part of Zen Beat.

    Zen Beat is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Zen Beat is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Zen Beat.  If not, see <http://www.gnu.org/licenses/>.*/

package com.game.zen.beat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends ActionBarActivity {

	
	public static final String PREFS_NAME = "ZenBeatPrefs";
	TimerTask timerPhy;
	Timer timer = new Timer();
	protected long usn, soundName;
	protected boolean canAnswer = false;
	protected int [] notes = new int[8];
    protected ImageView [] buttons = new ImageView[8];
	protected SoundPool sp;
	protected float levelPoints = 8;
	protected float level = 0;
	protected float levelCalc = 0;
	protected float levelCalcX = 0;
	protected float userPoints = 0;
	protected boolean replay = false;
	protected int notesPlayedCounter = 0;
	protected int replayCounter = 0;
	protected int skip = 0;
	protected int si = 0;
	protected Random rnd = new Random();
	protected int RND_MAX = 8;
	protected int answered = 0;
	protected float v = 1f;
	protected int length = 3;
    protected PBdrawable abPB = new PBdrawable();


    protected void save() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat("userPoints", userPoints);
        editor.putFloat("level", level);
        editor.putFloat("levelCalcX", levelCalcX);
        editor.putFloat("levelCalc", levelCalc);
        editor.putBoolean("HasPlayed", true);
        editor.commit();
    }


    protected void setAbProgress(int progress, float decimal) {
        abPB.setProgress((int) (decimal*100));
        abPB.number = progress;
        getSupportActionBar().setBackgroundDrawable(abPB);
    }

	protected void calcLength() {
		if (level <= 15){
			length = 3 + Math.round(levelCalc);
			levelCalcX = 0;
		} else {
			length = 3 + Math.round(levelCalc) + Math.round(levelCalcX);
		}
	}
	
	protected void calcBnts() {
		if (level < 11) {
			buttons[6].setVisibility(View.GONE);//8 = GONE
			buttons[7].setVisibility(View.GONE);//8 = GONE
			RND_MAX = 6;
		 }
		if (level < 7) {
            buttons[4].setVisibility(View.GONE);//8 = GONE
			buttons[5].setVisibility(View.GONE);//8 = GONE
			RND_MAX = 4;
		 }
		if (level < 2) {
            buttons[2].setVisibility(View.GONE);//8 = GONE
			buttons[3].setVisibility(View.GONE);//8 = GONE
			RND_MAX = 2;
		}
	}

    protected void startTimer(int delay) {
        timerPhy = new TimerPhy();
        timerPhy.run();
        timer = new Timer();
        timer.schedule(timerPhy, delay, 1000);
    }

	protected void levelUp() {

        String message;
        switch ((int)level+1) {
            case 2:
            case 7:
            case 11:
                message = "More notes are coming!";
                break;
            default:
			    message = "Longer challenges ahead, are you ready?";
		}

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message)
                .setPositiveButton("Make it harder!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        userPoints = 0;
                        level++;
                        levelCalcX = levelCalcX + (level / 15);
                        levelCalc++;
                        if (level == 2 || level == 7 || level == 11) { //Needs A Tutorial
                            if (level == 11f) { levelCalc = level - 10;}
                            if (level == 7f) { levelCalc = level - 5; }
                            if (level == 2f) { levelCalc = level - 2; }
                            save();
                            Intent intent = new Intent(getApplicationContext(), HowToActivity.class); //Let them try the new notes
                            Bundle b = new Bundle();
                            b.putBoolean("htp", false);
                            intent.putExtras(b);
                            startActivity(intent);
                        } else {
                            skip = 1;
                            startTimer(50);
                        }
                        setAbProgress((int) (levelPoints - userPoints), userPoints / levelPoints);
                    }
                })
                .setNegativeButton("Let me practice!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        skip = 1;
                        startTimer(50);
                        setAbProgress(0, 1);
                    }
                });
        builder.create().show();

        timerPhy.cancel();
        timer.cancel();
		
		calcBnts();
	}

    protected void showToast(String text, int iconId) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_with_icon,
                (ViewGroup) findViewById(R.id.toast_with_icon_root));

        TextView textView = (TextView) layout.findViewById(R.id.text);
        textView.setText(text);

        ImageView iconView = (ImageView) layout.findViewById(R.id.icon);
        iconView.setBackgroundResource(iconId);

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

	protected void mark() {
		answered = 0;
		if (usn == soundName) {
			//Correct
			userPoints++; // One more point
			if (userPoints >= levelPoints) {
				levelUp();
			} else {
                showToast("Correct!", R.drawable.ic_action_accept);
			}
		}
		if (usn != soundName){
			//WRONG
			showToast("Wrong", R.drawable.ic_action_cancel);
		}
        setAbProgress((int) (levelPoints - userPoints), userPoints / levelPoints);
		
		usn = 0;
		soundName = 0; 
		canAnswer = false;
        notesPlayedCounter = 0;
		skip = 2;
	}

    protected void playSoundFromNumber(int number) {
        sp.play(notes[number-1], v, v, 1, 0, 1f);
    }
	
	class TimerPhy extends TimerTask {
		public void run() {
			if (skip == 0) { //If we aren't skipping this tick
				
				calcLength();

				if (notesPlayedCounter < length && !replay) { //If more needs to be played and not in replay mode
                    notesPlayedCounter++; //++ our length thing counter
					soundName = soundName * 10; //Shift the sound number 1 << (base 10)
					int rn = rnd.nextInt(RND_MAX) + 1;
					playSoundFromNumber(rn);
                    soundName+=rn;
					if (notesPlayedCounter == 3 + (int)(levelCalc + levelCalcX)) {
						canAnswer = true;
					}
				}
				if (replayCounter < length && replay) { //If we're replaying
					canAnswer = false;
                    playSoundFromNumber(Integer.parseInt(Character.toString(Long.toString(soundName).charAt(replayCounter))));
                        //This is the complex way we get 1 numeral out
                    replayCounter++; //++ the replay counter
				}
				if (replayCounter >= length && replay) { //If the replay has finished
					canAnswer = true;
					replay = false;
                    replayCounter = 0;
				}
			}else{ //If we're skipping this tick
				skip--;
				canAnswer = false;
			}
		}
	}

    protected void touchListenerFunc (int id, int colorId) {
        if (canAnswer) {
            abPB.setColor(colorId);
            usn = usn * 10; //usn is the user's entered sound
            usn = usn + id;
            playSoundFromNumber(id);
            answered++;
            if (String.valueOf(usn).trim().length() == String.valueOf(soundName).trim().length()) {
                mark();
            }
        }
    }
	
	private OnClickListener Ocl1 = new OnClickListener() {
		public void onClick(View v) {
			touchListenerFunc(1, PBdrawable.COLOR_RED);
		}
	};
	private OnClickListener Ocl2 = new OnClickListener() {
		public void onClick(View v) {
            touchListenerFunc(2, PBdrawable.COLOR_RED);
		}
	};
	private OnClickListener Ocl3 = new OnClickListener() {
		public void onClick(View v) {
            touchListenerFunc(3, PBdrawable.COLOR_YELLOW);
		}
	};
	private OnClickListener Ocl4 = new OnClickListener() {
		public void onClick(View v) {
            touchListenerFunc(4, PBdrawable.COLOR_YELLOW);
		}
	};
	private OnClickListener Ocl5 = new OnClickListener() {
		public void onClick(View v) {
            touchListenerFunc(5, PBdrawable.COLOR_PURPLE);
		}
	};
	private OnClickListener Ocl6 = new OnClickListener() {
		public void onClick(View v) {
            touchListenerFunc(6, PBdrawable.COLOR_PURPLE);
		}
	};
	private OnClickListener Ocl7 = new OnClickListener() {
		public void onClick(View v) {
            touchListenerFunc(7, PBdrawable.COLOR_BLUE);
		}
	};
	private OnClickListener Ocl8 = new OnClickListener() {
		public void onClick(View v) {
            touchListenerFunc(8, PBdrawable.COLOR_BLUE);
		}
	};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_replay:
                if (!replay) {
                    replayCounter = 0;
                    replay = true;
                    canAnswer = false;
                }
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    userPoints = settings.getFloat("userPoints", 0f);
	    level = settings.getFloat("level", 0f);
	    levelCalc = settings.getFloat( "levelCalc", 0f);
	    levelCalcX = settings.getFloat("levelCalcX", 0f);

        setContentView(R.layout.main);

        OnClickListener oclToLoad [] = {Ocl1, Ocl2, Ocl3, Ocl4, Ocl5, Ocl6, Ocl7, Ocl8};
        int idsToFind [] = {R.id.b1, R.id.b2, R.id.b3, R.id.b4,
                            R.id.b5, R.id.b6, R.id.b7, R.id.b8};
        for (int i = 0; i < idsToFind.length; i++) {
            buttons[i] = (ImageView)findViewById(idsToFind[i]);
            buttons[i].setOnClickListener(oclToLoad[i]);
        }

        //More ui setup
        calcBnts();

        setAbProgress((int) (levelPoints - userPoints), (userPoints/levelPoints));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

		sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        int [] toLoad = {R.raw.s1, R.raw.s2, R.raw.s3, R.raw.s4, R.raw.s5,
                                R.raw.s6, R.raw.s7, R.raw.s8};
        for (int i = 0; i < toLoad.length; i++) {
            notes[i] = sp.load(this, toLoad[i], 1);
        }
	}

    @Override
    public void onResume() {
		super.onStart();
        skip = 1;
        startTimer(500);
	}

    @Override
    public void onPause(){
       super.onPause();

       save();
       
       timer.cancel();
       sp.stop(si);
     }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(this, MenuActivity.class);
        startActivity(home);
        super.onBackPressed();
    }
}
