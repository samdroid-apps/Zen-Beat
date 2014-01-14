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

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

public class HowToSlider extends ActionBarActivity {

    SectionsPagerAdapter sectionsPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_slider);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {}
            @Override
            public void onPageScrollStateChanged(int i) {}

            @Override
            public void onPageSelected(int i) {
                sectionsPagerAdapter.changeView(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.just_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment [] types = {new Fragment1(), new Fragment2(), new Fragment3()};
        private int prevShowing = -1;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return types[position];
        }

        @Override
        public int getCount() {
            return types.length;
        }

        public void changeView(int id) {
            if (prevShowing != -1) {
                switch (id) {
                    case 0:
                        Fragment1 f1 = (Fragment1) types[0];
                        f1.sp.stop(f1.playing);
                        break;
                    case 1:
                        Fragment2 f2 = (Fragment2) types[0];
                        f2.stopVideo();
                        break;
                    default:
                        break;
                }
            }
            switch (id) {
                case 0:
                    Fragment1 f = (Fragment1) types[0];
                    f.playing = f.sp.play(f.soundId, 1, 1, 1, 0, 1);
                    break;
                case 1:
                    Fragment2 f2 = (Fragment2) types[1];
                    f2.restartVideo();
                    break;
                case 2:
                    Intent intent = new Intent(getBaseContext(), HowToActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    public static class Fragment1 extends Fragment {

        SoundPool sp;
        int soundId;
        int playing;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.slide_1, container, false);

            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundId = sp.load(getActivity(), R.raw.slide_1_sound, 1);

            View.OnClickListener playSound = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playing = sp.play(soundId, 1, 1, 1, 0, 1);
                }
            };

            rootView.findViewById(R.id.playSoundButton).setOnClickListener(playSound);
            rootView.findViewById(R.id.slide_1_screenshot).setOnClickListener(playSound);

            return rootView;
        }
    }

    public static class Fragment2 extends Fragment {

        public VideoView videoView;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.slide_2, container, false);
            videoView = (VideoView) rootView.findViewById(R.id.video);

            Uri videoUri = Uri.parse("android.resource://com.game.zen.beat/raw/"+
                                                        R.raw.slide_2_video);
            //Uri videoUri = Uri.parse("rtsp://v5.cache1.c.youtube.com/CjYLENy73wIaLQnhycnrJQ8qmRMYESARFEIJbXYtZ29vZ2xlSARSBXdhdGNoYPj_hYjnq6uUTQw=/0/0/0/video.3gp");
            videoView.setVideoURI(videoUri);

            rootView.findViewById(R.id.slide_2_replay).setOnClickListener(
                                                                new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    restartVideo();
                }
            });

            return rootView;
        }

        public void restartVideo() {
            //videoView.stopPlayback();
            //videoView.seekTo(0);
            videoView.requestFocus();
            videoView.start();
        }

        public void stopVideo() {
            videoView.stopPlayback();
            videoView.seekTo(0);
        }

    }

    public static class Fragment3 extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.main, container, false);
            return rootView;
        }
    }

}
