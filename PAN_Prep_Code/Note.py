
import time
import datetime

class Note():


	def __init__( self, location, filename, with_pic ):
		self.location = location
		self.date = self.getDate()
		self.filename = filename
		self.with_pic = with_pic
		if( with_pic == False ):
			self.createNote()
		else:
			self.createPicNote()


	def getLocation( self ):
		return self.location

	def setLocation( self, loc ):
		self.location = loc

	def getFilename( self ):
		return self.filename

	def setFilename( self, name ):
		self.filename = name

	def getNoteDate( self ):
		return self.date


	def getDate( self ):
		ts = time.time()
		self.date = datetime.datetime.fromtimestamp(ts).strftime('%Y-%m-%d_%H:%M:%S')
		return self.date

	def createNote( self ):
		text = raw_input( "\nEnter Note Below:\n")
		file = open( "/Users/Richie/Desktop/PAN Prep Code/Notes/" + self.filename + ".txt", "wb" )
		file.write( str(self.date) + "\n" + self.location + "\n\n" )
		file.write( "Note:\n")
		file.write( text )
		file.close()


	def createPicNote( self ):
		file = open( "/Users/Richie/Desktop/PAN Prep Code/Pictures/" + self.filename + ".txt", "wb" )
		file.write( str(self.date) + "\n" + self.location )
		file.close()


	def deleteNote( self, filename ):
		file = open( "/Users/Richie/Desktop/PAN Prep Code/Notes/" + self.filename + ".txt", "wb" )
		file.write( "DELETED" )
		file.close()



	'''def __init__( self, location, date, filename ):
		self.location = location
		self.date = date
		self.filename = filename


	def getLocation( self ):
		return self.location

	def setLocation( self, loc ):
		self.location = loc

	def getDate( self ):
		return self.date

	def setDate( self, date ):
		self.date = date

	def getFilename( self ):
		return self.filename

	def setFilename( self, name ):
		self.filename = name'''