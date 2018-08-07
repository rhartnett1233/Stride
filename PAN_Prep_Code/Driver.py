

import CreateUser
import Note
import Picture


def validateUser():
	temp_username = raw_input( "Enter Username: " )
	temp_password = raw_input( "Enter Password: " )
	##############################################
	#create method to validate user from database#
	##############################################
	return True

def displayMenu():
	println( "Enter the number of the desired choice: " )
	println( "1 --> View Locations" )
	println( "2 --> View Photos" )
	println( "3 --> View Notes" )
	println( "4 --> Add Location" )
	println( "5 --> Add Photo" )
	println( "6 --> Add Note" )
	println( "7 --> Edit Profile" )
	println( "8 --> Logout")


#######################
#        LOGIN        #
#######################
valid_res = False
while( valid_res == False ):
	response1 = raw_input( "Enter 0 to Login or 1 to Create Account: " )
	if( response1 == "0" ):
		validateUser()
		valid_res = True
	elif( response1 == "1" ):
		fname = raw_input( "Enter first name: " )
		lname = raw_input( "Enter last name: " )
		uname = raw_input( "Enter username: " )
		pword = raw_input( "Enter password: " )
		cur_user = CreateUser.CreateUser( fname, lname, uname, pword )
		cur_user.createAccount()
		valid_res = True
	else:
		println( "Invalid Response!" )

'''
#######################
#        MENU         #
1 --> View Locations
2 --> View Photos
3 --> View Notes
4 --> New Location
5 --> New Photo
6 --> New Note
7 --> Edit Profile
8 --> Logout
#######################
'''

displayMenu()
response = raw_input( "Selection: " )
while (response != "8" ):
	if( response == "1" ):
		#
	elif( response == "2" ):
		#
	elif( response == "3" ):
		#
	elif( response == "4" ):
		#
	elif( response == "5" ):
		#
	elif( response == "6" ):
		#
	elif( response == "7" ):
		#
	elif( response == "8" ):
		#
	else:
		println( "Invalid Response!")
	raw_input( " " )
	displayMenu()
	response = raw_input( "Selection: " )





