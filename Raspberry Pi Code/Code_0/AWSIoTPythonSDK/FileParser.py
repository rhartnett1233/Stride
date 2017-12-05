import argparse
import mqtt_client

parser = argparse.ArgumentParser()
parser.add_argument("--file", help="datafile")
args=parser.parse_args()

file = args.file
file_data = open(file)

accel = []
mag = []
gyro = []

accel_meas = [" ", " ", " "]
mag_meas = [" ", " ", " "]
gyro_meas = [" ", " ", " "]

index = 0
while( 1 ):
    cur_line = file_data.readline()
    if( cur_line == "" ):
        break
    line_list = cur_line.split()
    accel_meas = [line_list[0], line_list[1], line_list[2]]
    accel.append(accel_meas)
    
    cur_line = file_data.readline()
    if( cur_line == "" ):
        break
    line_list = cur_line.split()
    gyro_meas = [line_list[0], line_list[1], line_list[2]]
    gyro.append(gyro_meas)
    
    cur_line = file_data.readline()
    if( cur_line == "" ):
        break
    line_list = cur_line.split()
    mag_meas = [line_list[0], line_list[1], line_list[2]]
    mag.append( mag_meas )
    
    index = index + 1
    

client = mqtt_client.mqtt_client( accel, gyro, mag )
client.store_data()
        
    