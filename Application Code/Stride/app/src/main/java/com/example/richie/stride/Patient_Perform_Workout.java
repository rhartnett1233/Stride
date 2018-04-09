package com.example.richie.stride;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import dyanamitechetan.vusikview.VusikView;

public class Patient_Perform_Workout extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener {

    private ImageButton btn_play_pause;
    private SeekBar seekBar;
    private TextView textView;
    private VusikView musicView;

    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realTimeLength;
    final Handler handler = new Handler();
    MQTT_Helper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__perform__workout);
        Intent in = getIntent();
        final String cur_patient = in.getStringExtra( "com.example.richie.CUR_PATIENT" );
        final String workout_bpm = in.getStringExtra( "com.example.richie.WORKOUT_BPM" );
        final String workout_time = in.getStringExtra( "com.example.richie.WORKOUT_TIME" );
        int workout_time_int = Integer.parseInt( workout_time );

        /**********************************************
         * Connects to AWS backend to retrieve information from database
         */
        Context appContext = getApplicationContext();
        AWSConfiguration awsConfig = new AWSConfiguration(appContext);
        IdentityManager identityManager = new IdentityManager(appContext,
                awsConfig);

        IdentityManager.setDefaultIdentityManager(identityManager);
        final AWSCredentialsProvider credentialsProvider = identityManager.getCredentialsProvider();
        final String userId = identityManager.getCachedUserID();
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
        final DynamoDBMapper dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();
        /**********************************************/
        //mediaFileLength = Integer.parseInt(workout_time);

        musicView = (VusikView) findViewById( R.id.musicView );

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(99);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if( mediaPlayer.isPlaying() ){
                    SeekBar seekBar = (SeekBar)view;
                    int playPosition = (mediaFileLength/1000)*seekBar.getProgress();
                    mediaPlayer.seekTo( playPosition );
                }
                return false;
            }
        });

        textView = (TextView) findViewById(R.id.textTimer);
        btn_play_pause = (ImageButton) findViewById( R.id.btn_play_pause );

        try {
            startMqtt( dynamoDBMapper, cur_patient, workout_time );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        btn_play_pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog( Patient_Perform_Workout.this );
                AsyncTask<String, String, String> mp3Play = new AsyncTask<String, String, String>() {

                    @Override
                    protected  void onPreExecute(){
                        mDialog.setMessage( "Please Wait" );
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        try{
                            mediaPlayer.setDataSource( strings[0] );
                            mediaPlayer.prepare();
                        } catch (Exception e){

                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mediaFileLength = mediaPlayer.getDuration();
                        System.out.println("^^^^^^^^^^^^^^^");
                        System.out.println( mediaFileLength );
                        System.out.println("^^^^^^^^^^^^^^^");
                        realTimeLength = mediaFileLength;
                        if( !mediaPlayer.isPlaying() ){
                            MqttMessage myMess = new MqttMessage("START".getBytes());
                            myMess.setQos(1);
                            myMess.setRetained(false);
                            try {
                                mqttHelper.publish("WORKOUT",myMess);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                            System.out.println( "!!!!!!!!!!" );
                            System.out.println( "START MQTT" );
                            System.out.println( "!!!!!!!!!!" );
                            mediaPlayer.start();
                            musicView.start();
                            btn_play_pause.setImageResource( R.drawable.ic_pause );
                        }
                        else{
                            MqttMessage myMess = new MqttMessage("PAUSE".getBytes());
                            myMess.setQos(1);
                            myMess.setRetained(false);
                            try {
                                mqttHelper.publish("WORKOUT",myMess);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                            System.out.println( "!!!!!!!!!!" );
                            System.out.println( "Pause MQTT" );
                            System.out.println( "!!!!!!!!!!" );
                            mediaPlayer.pause();
                            musicView.stopNotesFall();
                            btn_play_pause.setImageResource( R.drawable.ic_play2 );
                        }

                        updateSeekBar( workout_time, workout_bpm );
                        mDialog.dismiss();
                    }
                };
                mp3Play.execute(); //direct linked mp3
            }
        });

        createMediaPlayer(workout_bpm);
    }

    private void updateSeekBar( final String wt, final String workout_bpm ){
        final int workout_time = Integer.parseInt( wt ) * 1000;

        if( mediaPlayer.getCurrentPosition() >= workout_time ){
            seekBar.setProgress( 0 );
            mediaPlayer.stop();
            createMediaPlayer( workout_bpm );
            btn_play_pause.setImageResource(R.drawable.ic_play);
            musicView.stopNotesFall();
            MqttMessage myMess = new MqttMessage("END".getBytes());
            myMess.setQos(1);
            myMess.setRetained(false);
            try {
                mqttHelper.publish("WORKOUT",myMess);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        seekBar.setProgress( (int) (((float)mediaPlayer.getCurrentPosition() / workout_time) * 100) );
        if( mediaPlayer.isPlaying() ){
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    updateSeekBar( wt, workout_bpm );
                    realTimeLength = (int) mediaPlayer.getCurrentPosition()/1000; //declare 1 second
                    int minutes = realTimeLength/60;
                    int seconds = realTimeLength%60;
                    String timer;
                    if( minutes >= 10 ) {
                        if (seconds >= 10)
                            timer = String.format("%d:%d", minutes, seconds);
                        else
                            timer = String.format("%d:0%d", minutes, seconds);
                    }
                    else{
                        if (seconds >= 10)
                            timer = String.format("0%d:%d", minutes, seconds);
                        else
                            timer = String.format("0%d:0%d", minutes, seconds);
                    }
                    textView.setText( timer);

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
        btn_play_pause.setImageResource(R.drawable.ic_play);
        musicView.stopNotesFall();
        MqttMessage myMess = new MqttMessage("END".getBytes());
        myMess.setQos(1);
        myMess.setRetained(false);
        try {
            mqttHelper.publish("WORKOUT",myMess);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void startMqtt( final DynamoDBMapper dynamoDBMapper, final String cur_patient, final String workout_time ) throws InterruptedException {
        OverallPatientPerformanceTableDO temp = new OverallPatientPerformanceTableDO();
        temp.setPUser( "(" + cur_patient + ")" );
        System.out.println( "^^^^^^^^^^^^^^^^" );
        System.out.println( temp.getPUser() );
        System.out.println( "^^^^^^^^^^^^^^^^" );
        String total_sessions = Integer.toString( temp.getTotalSessions(dynamoDBMapper, temp) );
        final String info = workout_time + " " + total_sessions;
        mqttHelper = new MQTT_Helper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug", "MQTT CONNECTED");
                MqttMessage myMess = new MqttMessage(info.getBytes());
                myMess.setQos(1);
                myMess.setRetained(false);
                try {
                    mqttHelper.publish("WORKOUT",myMess);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.w("Debug", "MQTT CONNECTION LOST");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.w("Debug", "MQTT DELIVERY COMPLETE!");
            }
        });
    }

    public void createMediaPlayer( final String workout_bpm ){
        mediaPlayer = new MediaPlayer();
        switch (workout_bpm){
            case "30":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.thirty );
                break;
            case "35":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.thirty_five );
                break;
            case "40":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.forty );
                break;
            case "45":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.forty_five );
                break;
            case "50":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.fifty );
                break;
            case "55":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.fifty_five );
                break;
            case "60":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.sixty );
                break;
            case "65":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.sixty_five );
                break;
            case "70":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.seventy );
                break;
            case "75":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.seventy_five );
                break;
            case "80":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.eighty );
                break;
            case "85":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.eighty_five );
                break;
            case "90":
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.ninty );
                break;
            default:
                mediaPlayer = MediaPlayer.create( getApplicationContext(), R.raw.bpm_sec );
        }
        //mediaPlayer.start();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }
}
