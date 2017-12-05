# Import package
import paho.mqtt.client as mqtt
import ssl
import time
import json
import time

# Define Variables

class mqtt_client:
    def __init__( self, accel, gyro, mag ):
        self.accel = accel
        self.mag = mag
        self.gyro = gyro
        self.user = "Joe"
        self.sessionID = 1
        
        self.MQTT_PORT = 8883
        self.MQTT_KEEPALIVE_INTERVAL = 45
        self.MQTT_TOPIC = "myTopic"
        
        self.MQTT_HOST = "a35pq9rp66r1yo.iot.us-east-1.amazonaws.com"
        self.CA_ROOT_CERT_FILE = "/home/pi/Desktop/Stride/Raspberry Pi Code/Certificates/New Certificates/VeriSign-Class 3-Public-Primary-Certification-Authority-G5.pem.txt"
        self.THING_CERT_FILE = "/home/pi/Desktop/Stride/Raspberry Pi Code/Certificates/New Certificates/6943a8d54a-certificate.pem.crt"
        self.THING_PRIVATE_KEY = "/home/pi/Desktop/Stride/Raspberry Pi Code/Certificates/New Certificates/6943a8d54a-private.pem.key"
        
        self.total_meas = len(accel)



    def store_data( self ):
        TIMESTAMP = time.time()
        print(TIMESTAMP)

        SESSIONID = 1
        MEA = 1

        # Initiate MQTT Client
        mqttc = mqtt.Client()

        # Configure TLS Set
        mqttc.tls_set(self.CA_ROOT_CERT_FILE, certfile=self.THING_CERT_FILE, keyfile=self.THING_PRIVATE_KEY,
                      cert_reqs=ssl.CERT_REQUIRED, tls_version=ssl.PROTOCOL_TLSv1_2, ciphers=None)

        # Connect with MQTT Broker
        mqttc.connect(self.MQTT_HOST, self.MQTT_PORT, self.MQTT_KEEPALIVE_INTERVAL)		
        mqttc.loop_start()

        counter = 0
        while counter < self.total_meas:
            SESSIONID1 = str(SESSIONID) + "_" + str(counter)
            MQTT_MSG = json.dumps({"user": "Joe","sessionID": SESSIONID1,
                                   "accelerometer x": self.accel[counter][0], "accelerometer y": self.accel[counter][1], "accelerometer z": self.accel[counter][2],
                                   "gyroscope x": self.gyro[counter][0], "gyroscope y": self.gyro[counter][1], "gyroscope z": self.gyro[counter][2],
                                   "magnetometer x": self.mag[counter][0], "magnetometer y": self.mag[counter][1], "magnetometerz": self.mag[counter][2]})

            mqttc.publish(self.MQTT_TOPIC, MQTT_MSG ,qos=1)
            counter+=1
            time.sleep(.1)
            #print(counter)

        # Disconnect from MQTT_Broker
        mqttc.disconnect()



