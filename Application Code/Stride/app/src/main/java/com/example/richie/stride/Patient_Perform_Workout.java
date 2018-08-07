package com.example.richie.stride;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Map;

public class Patient_Perform_Workout extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener {

    private TextView status;
    private TextView count;
    private ImageButton imageButton;
    private CountDownTimer workoutTimer;
    private CountDownTimer countDownTimer;
    private CountDownTimer goTimer;
    private CountDownTimer endTimer;
    private CountDownTimer interTimer;

    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realTimeLength;
    final Handler handler = new Handler();
    MQTT_Helper mqttHelper;
    int cur_time;
    private DynamoDBMapper dynamoDBMapper2;
    private int int_sessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__perform__workout);
        Intent in = getIntent();
        final String cur_patient = in.getStringExtra("com.example.richie.CUR_PATIENT");
        final String workout_bpm = in.getStringExtra("com.example.richie.WORKOUT_BPM");
        final String workout_time = in.getStringExtra("com.example.richie.WORKOUT_TIME");
        final String workout_type = in.getStringExtra( "com.example.richie.WORKOUT_TYPE");
        final int workout_time_int = Integer.parseInt(workout_time);

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
        dynamoDBMapper2 = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(awsConfig)
                .build();
        final DynamoDBMapper dynamoDBMapper = dynamoDBMapper2;
        /**********************************************/

        status = (TextView) findViewById( R.id.textViewStatus );
        count = (TextView) findViewById( R.id.textViewCount );
        imageButton = (ImageButton) findViewById( R.id.imageButtonStartStop );

        status.setText( "PRESS PLAY TO BEGIN" );
        String temp = "";
        try {
            temp = startMqtt( dynamoDBMapper, cur_patient, workout_time, workout_type );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final String workout_info = temp;

        imageButton.setOnClickListener(new View.OnClickListener() {
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
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mediaFileLength = mediaPlayer.getDuration();
                        realTimeLength = mediaFileLength;
                        if( !mediaPlayer.isPlaying() ){
                            MqttMessage myMess = new MqttMessage(workout_info.getBytes());
                            myMess.setQos(1);
                            myMess.setRetained(false);
                            try {
                                mqttHelper.publish("WORKOUT",myMess);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                            myMess = new MqttMessage("START".getBytes());
                            myMess.setQos(1);
                            myMess.setRetained(false);
                            try {
                                mqttHelper.publish("WORKOUT",myMess);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                            startCountDown( workout_time_int, cur_patient );
                            imageButton.setImageResource( R.drawable.ic_stop );
                        }
                        else{
                            startInterTimer( cur_patient, Integer.toString(workout_time_int - cur_time) );
                            imageButton.setImageResource( R.drawable.ic_stop );
                        }

                        mDialog.dismiss();
                    }
                };
                mp3Play.execute(); //direct linked mp3
            }
        });

        createMediaPlayer(workout_bpm);
    }


    private void startCountDown( final int workout_time, final String cur_patient ){
        status.setText( "BEGIN WORKOUT IN:");
        count.setText( "5" );
        imageButton.setClickable( false );
        imageButton.setEnabled( false );
        countDownTimer = new CountDownTimer( 5000, 1000) {
            @Override
            public void onTick(long l) {
                count.setText( Long.toString(l/1000));
            }

            @Override
            public void onFinish() {
                startGoTimer( workout_time, cur_patient );
            }
        };
        countDownTimer.start();
    }


    private void startGoTimer( final int workout_time, final String cur_patient ){
        status.setText( "GO!" );
        count.setText("");
        goTimer = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {

            }
            @Override
            public void onFinish() {
                startCountWorkout( workout_time, cur_patient );
            }
        };
        goTimer.start();
    }


    private void startCountWorkout(final int workout_time, final String cur_patient ){
        status.setText( "PERFORM WORKOUT" );
        count.setText( Integer.toString(workout_time) );
        imageButton.setClickable( true );
        imageButton.setEnabled( true );
        mediaPlayer.start();
        workoutTimer = new CountDownTimer( workout_time * 1000, 1000) {
            @Override
            public void onTick(long l) {
                count.setText( Long.toString( l/1000 ));
                cur_time = (int)(l/1000);
            }

            @Override
            public void onFinish() {
                startInterTimer( cur_patient, Integer.toString(workout_time) );
            }
        };
        workoutTimer.start();
    }


    private void startInterTimer( final String cur_patient, final String time ){
        mediaPlayer.stop();
        workoutTimer.cancel();
        count.setText( "0" );
        status.setText( "PERFORM WORKOUT" );
        String sending = "STOP " + time;
        MqttMessage myMess = new MqttMessage(sending.getBytes());
        myMess.setQos(1);
        myMess.setRetained(false);
        try {
            mqttHelper.publish("WORKOUT",myMess);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        interTimer = new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                try {
                    startEndTimer( cur_patient, time );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        interTimer.start();
    }


    private void startEndTimer( final String cur_patient, final String time ) throws InterruptedException {
        mediaPlayer.stop();
        workoutTimer.cancel();
        ErrorDataNewDO errorDataNewDO = new ErrorDataNewDO();
        String result = errorDataNewDO.checkError( dynamoDBMapper2, cur_patient, int_sessions );
        status.setText( result );
        count.setText( "" );
        final Intent next = new Intent( Patient_Perform_Workout.this, Patient_First_Screen.class );
        next.putExtra( "com.example.richie.CURRENT_PATIENT", cur_patient );
        endTimer = new CountDownTimer( 1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity( next );
                finish();
            }
        };
        endTimer.start();
    }


    private String startMqtt( final DynamoDBMapper dynamoDBMapper, final String cur_patient, final String workout_time, final String workout_type ) throws InterruptedException {
        OverallPatientPerformanceTableDO temp = new OverallPatientPerformanceTableDO();
        temp.setPUser( "(" + cur_patient + ")" );
        int_sessions = temp.getTotalSessions(dynamoDBMapper, temp);
        String total_sessions = Integer.toString( int_sessions );
        double goal_stride = 0;
        if( workout_type.equals("Progressive") ){
            PaginatedQueryList<OverallPatientPerformanceTableDO> xx = temp.getSessionData( dynamoDBMapper, temp, int_sessions-1);
            Map<String, String> yy = xx.get(0).getData();
            goal_stride = Double.parseDouble( yy.get("AvgStrideLength") );
            double zz = goal_stride*0.05;
            goal_stride = goal_stride + zz;
        }
        UserInfoNewDO temp_user = new UserInfoNewDO();
        temp_user.setUsername( cur_patient );
        String temp_leg_length = "";
        String temp_height = "";
        double leg_length = 0;
        double height = 0;
        PaginatedQueryList<UserInfoNewDO> list_leg = temp_user.findUser( dynamoDBMapper, temp_user );
        if( list_leg.size() > 0 ){
            temp_leg_length = list_leg.get(0).getLegLength();
            leg_length = Double.parseDouble( temp_leg_length );
            leg_length = leg_length*0.0254;

            if( workout_type.equals( "Standardized") ){
                temp_height = list_leg.get(0).getHeight();
                height = Double.parseDouble( temp_height );
                goal_stride = findStrideLength( height );
            }
        }
        final String info = total_sessions + " " + String.format( "%.2f", leg_length) + " " + cur_patient + " " + workout_time + " " + String.format( "%.2f", goal_stride);
        mqttHelper = new MQTT_Helper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug", "MQTT CONNECTED");
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
        return info;
    }


    public double findStrideLength( double height ){
        double result = height*0.413*0.0254;
        return result;
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        //need to fix
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        /*//need to fix
        MqttMessage myMess = new MqttMessage("STOP".getBytes());
        myMess.setQos(1);
        myMess.setRetained(false);
        try {
            mqttHelper.publish("WORKOUT",myMess);
        } catch (MqttException e) {
            e.printStackTrace();
        }*/
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
