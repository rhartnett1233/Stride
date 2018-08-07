
import Note
from shutil import copyfile
import time
import datetime
#import Image

class Picture():


	def __init__( self, location, filename ):
		self.location = location
		self.date = self.getDate()
		self.filename = filename


	def getLocation( self ):
		return self.location

	def setLocation( self, loc ):
		self.location = loc

	def getFilename( self ):
		return self.filename

	def setFilename( self, name ):
		self.filename = name

	def getPicDate( self ):
		return self.date


	def getDate( self ):
		ts = time.time()
		self.date = datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d_%H:%M:%S')
		return self.date


	def uploadPicture( self ):
		cur_filename = raw_input( "Enter the filename to be uploaded:\n" )
		copyfile( cur_filename, "/Users/Richie/Desktop/PAN Prep Code/Pictures/" + self.filename + ".JPG")
		note = Note.Note( self.location, self.filename, True )


	def displayPicture( self ):
		#image = Image.open( "/Users/Richie/Desktop/PAN Prep Code/Pictures/" + self.filename + ".JPG" )
		#image.show()
		temp = True