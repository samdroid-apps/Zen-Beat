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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	
	public static final String PREFS_NAME = "ZenBeatPrefs";
	
	Context con = this;
	
	private OnClickListener OclBrsg = new OnClickListener() {
		public void onClick(View v) {
			resetDialogSub();
		}
	};
	protected void resetDialogSub() {
		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		builder.setMessage("Are you sure you want to reset all progress?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					    SharedPreferences.Editor editor = settings.edit();
					    editor.putFloat("userPoints", 0);
					    editor.putFloat("level", 0);
					    editor.putFloat("levelCalcX", 0);
					    editor.putFloat("levelCalc", 0);
					    editor.putBoolean("HasPlayed", false);
					    editor.commit();
		           }
		       })
		       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	/*private OnClickListener OclBsl = new OnClickListener() {
		public void onClick(View v) {
			EditText et1 = (EditText) findViewById(R.id.editText1);
			AlertDialog.Builder builder = new AlertDialog.Builder(con);
			builder.setMessage("Are you sure you want to set the level to level "+et1.getText()+"?")
			       .setCancelable(false)
			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	    EditText et1 = (EditText) findViewById(R.id.editText1);
			   				int item = Integer.parseInt(""+et1.getText());
			   				Log.i("#",""+item);
			   				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			   				SharedPreferences.Editor editor = settings.edit();
			   						    if (item == 15) {
			   						    	editor.putFloat("levelPoints", 10);
			   						    } else {
			   						    	editor.putFloat("levelPoints", 13);
			   						    }
			   						    editor.putFloat("userPoints", 12);
			   						    editor.putFloat("level", (float)item);
			   						    editor.commit();
			           }
			       })
			       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
					
			    	   };
	};*/
	
	private OnClickListener OclBal = new OnClickListener() {
		public void onClick(View v) {
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		    float level = settings.getFloat("level", 0f);
		    float levelCalcX = settings.getFloat("levelCalcX", 0f);
		    if (level > 0) {
		    	SharedPreferences.Editor editor = settings.edit();
		    	level =  level - 1;
		    	editor.putFloat("userPoints", 0);
		    	editor.putFloat("level", level);
		    	editor.putFloat("levelCalcX", (levelCalcX - (level / 15)));
		    	float lC = 0;
		    	if (level >= 11) {
		    		lC = level - 10;
		    	}else if (level >= 7) {
		    		lC = level - 5;
		    	}else if (level >= 2) {
		    		lC = level - 2;
		    	}
		    	editor.putFloat("levelCalc", lC);
		    	editor.commit();
		    
		    	Toast toast = Toast.makeText(getApplicationContext(), "You Are Now On Level "+Integer.toString((int)(level+1)), Toast.LENGTH_SHORT);
		    	toast.show();
		    } else {
		    	Toast toast = Toast.makeText(getApplicationContext(), "You Need To Be On At Least Level 2 To Use This", Toast.LENGTH_SHORT);
		    	toast.show();
		    }
		}
	};
	
	private OnClickListener OclFal = new OnClickListener() {
		public void onClick(View v) {
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		    float level = settings.getFloat("level", 0f);
		    float levelCalc = settings.getFloat( "levelCalc", 0f);
		    float levelCalcX = settings.getFloat("levelCalcX", 0f);
		    SharedPreferences.Editor editor = settings.edit();
		    level =  level + 1;
		    editor.putFloat("userPoints", 0);
		    editor.putFloat("level", level);
		    editor.putFloat("levelCalcX", (levelCalcX + (level / 15)));
		    float lC = 0;
		    if (level >= 11) {
				lC = level - 10;
			}else if (level >= 7) {
				lC = level - 5;
			}else if (level >= 2) {
				lC = level - 2;
			}
		    editor.putFloat("levelCalc", lC);
		    editor.commit();
		    
		    Toast toast = Toast.makeText(getApplicationContext(), "You Are Now On Level "+Integer.toString((int)(level+1)), Toast.LENGTH_SHORT);
		    toast.show();
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		//Button bsl = (Button)findViewById(R.id.bsl);
		//bsl.setOnClickListener(OclBsl);
		Button brsg = (Button)findViewById(R.id.brsg);
		brsg.setOnClickListener(OclBrsg);
		Button bbal = (Button)findViewById(R.id.bbal);
		bbal.setOnClickListener(OclBal);
		Button bfal = (Button)findViewById(R.id.bfal);
		bfal.setOnClickListener(OclFal);

        WebView webView = (WebView) findViewById(R.id.webView);
        String html = "<html><body style='font-family: sans-serif'>"
                + "<p>Zen Beat is copyright Sam Parkinson 2012 to 2014.</p>\n"
                + "<p>Zen Beat is distributed under the terms of the GNU General Public License version 3. "
                + "For more info about the GPL please visit <a href='http://www.gnu.org/licenses/licenses.html'>the GNU website</a></p>"
                + "<p>We would like to thank these great resources that are used in this app:" +
                "<ul><li>The Android Icon Set (by Google)</li></p>" +
                "<li>The Android Support Library" +
                "<p style='font-family: monospace; background-color: #DDD; padding: 3px;'>" +
                " * Copyright (C) The Android Open Source Project<br />" +
                " *<br />" +
                " * Licensed under the Apache License, Version 2.0 (the \"License\");<br />" +
                " * you may not use this file except in compliance with the License.<br />" +
                " * You may obtain a copy of the License at<br />" +
                " *<br />" +
                " *      http://www.apache.org/licenses/LICENSE-2.0<br />" +
                " *<br />" +
                " * Unless required by applicable law or agreed to in writing, software<br />" +
                " * distributed under the License is distributed on an \"AS IS\" BASIS,<br />" +
                " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.<br />" +
                " * See the License for the specific language governing permissions and<br />" +
                " * limitations under the License.</p></li></ul>"
                    + "</body></html>";
        webView.loadData(html, "text/html", null);
        webView.setBackgroundColor(Color.TRANSPARENT);
	}
}
