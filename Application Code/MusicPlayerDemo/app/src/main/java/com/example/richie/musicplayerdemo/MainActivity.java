package com.example.richie.musicplayerdemo;
//https://www.youtube.com/watch?v=hLs1wVJH3hc
//1606
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import dyanamitechetan.vusikview.VusikView;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener{

    private ImageButton button_play_pause;
    private SeekBar seekBar;
    private TextView textView;

    private VusikView musicView;

    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realTimeLength;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicView = (VusikView) findViewById(R.id.musicView);


        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(99);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( mediaPlayer.isPlaying() ){
                    SeekBar seekBar = (SeekBar)view;
                    int playPosition = (mediaFileLength/100)*seekBar.getProgress();
                    mediaPlayer.seekTo( playPosition );
                }
                return false;
            }
        });

        textView = (TextView) findViewById(R.id.textTimer);


        button_play_pause = (ImageButton) findViewById(R.id.button_play_pause);
        button_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
                AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {

                    @Override
                    protected void onPreExecute() {
                        mDialog.setMessage("Please Wait");
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            mediaPlayer.setDataSource(strings[0]);
                            mediaPlayer.prepare();
                        } catch (Exception ex) {

                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mediaFileLength = mediaPlayer.getDuration();
                        realTimeLength = mediaFileLength;
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            button_play_pause.setImageResource(R.drawable.ic_pause);
                        } else {
                            mediaPlayer.pause();
                            button_play_pause.setImageResource(R.drawable.ic_play);
                        }

                        updateSeekBar();
                        mDialog.dismiss();

                    }
                };

                mp3Play.execute("http://mic.duytan.edu.vn:86/ncs.mp3"); //direct linked mp3
                musicView.start();
            }
        });

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    private void updateSeekBar(){
        seekBar.setProgress( (int) (((float)mediaPlayer.getCurrentPosition() / mediaFileLength) * 100) );
        if( mediaPlayer.isPlaying() ){
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar();
                    realTimeLength = 1000; //declare 1 second
                    textView.setText( String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realTimeLength),
                            TimeUnit.MILLISECONDS.toSeconds(realTimeLength) -
                                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realTimeLength))));

                }
            };
            handler.postDelayed(updater, 1000);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        seekBar.setSecondaryProgress(i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
    button_play_pause.setImageResource(R.drawable.ic_play);
    musicView.stopNotesFall();
    }
}
