package com.example.richie.stride;

//https://api.cloudmqtt.com/console/9776071/details

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTT_Temp extends AppCompatActivity {

    //Button buttonSendMessage;
    MQTT_Helper mqttHelper;
    TextView dataReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt__temp);

        /*buttonSendMessage = (Button) findViewById( R.id.buttonSendMessage );

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        dataReceived = (TextView) findViewById(R.id.dataReceived);

        startMqtt();
    }

    private void startMqtt(){
        mqttHelper = new MQTT_Helper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug", "MQTT CONNECTED");

                MqttMessage myMess = new MqttMessage("Hello, I am Android Mqtt Client.".getBytes());
                myMess.setQos(1);
                myMess.setRetained(false);
                try {
                    mqttHelper.publish("MYTEST",myMess);
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
                dataReceived.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                Log.w("Debug", "MQTT DELIVERY COMPLETE!");
            }
        });
    }
}
