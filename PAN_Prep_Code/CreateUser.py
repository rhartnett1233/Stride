


class CreateUser():

	def __init__( self, f_name, l_name, u_name, p_word ):
		self.f_name = f_name
		self.l_name = l_name
		self.u_name = u_name
		self.p_word = p_word


	def getFirstName( self ):
		return self.f_name

	def setFirstName( self, name ):
		self.f_name = name

	def getLastName( self ):
		return self.l_name

	def setLastName( self, name ):
		self.l_name = name

	def getUserName( self ):
		return self.u_name

	def setFirstName( self, name ):
		self.u_name = name

	def getPassword( self ):
		return self.p_word

	def setPassword( self, word ):
		self.p_word = word


	def createAccount( self ):
		
		#######################
		#save user to database#
		#######################
		return True

	