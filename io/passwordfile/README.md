#Generate and read password protected files

## Usage

NB! There is a known problem with reading / writing larger files. You might consider using byte array on reading and writing

Simple usage:

		try (OutputStream out = new PasswordProtectedOutputStream(outputstream, password)) {
			// write as your normal outputstream
		}

		try (InputStream in = new PasswordProtectedInputStream(inputstream, password) ) {
				//read as a normal inputstream
		}

The input/output streams uses default salt and most best encryption algorithm. It is possible to specify your own

Also see [SupportedAlgorithm.java](src/main/java/com/foxjunior/io/passwordfile/SupportedAlgorithm.java) for supported algorithm list. It eases up the usage. You can either use the best encoding from the list of select one on your own.
