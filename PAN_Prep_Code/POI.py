

class POI():

	def __init__( self, longi, lat, loc, date, picture_list, note_list ):
		self.longitude = longi
		self.latitude = lat
		self.location = loc
		self.date = date
		self.picture_list = picture_list    #contains picture objects
		self.note_list = note_list		#contains note objects


	def getLongitude( self ):
		return self.longitude

	def setLongitude( self, coord ):
		self.longitude = coord

	def getLatitiude( self ):
		return self.latitude

	def setLatitude( self, coord ):
		self.latitude = coord

	def getLocation( self ):
		return self.location

	def setLocation( self, name ):
		self.loaction = name

	def getDate( self ):
		return self.date

	def setLongitude( self, date ):
		self.date = date

	def getPictureList( self ):
		return self.picture_list

	def setLongitude( self, plist ):
		self.picture_list = plist

	def getNoteList( self ):
		return self.note_list

	def setNoteList( self, nlist ):
		self.note_list = nlist





