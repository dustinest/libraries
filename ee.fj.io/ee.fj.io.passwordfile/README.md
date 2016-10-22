#Generate and read password protected files

See tests for examples

NB! There is a known problem with reading / writing larger files. You might consider using bytearray on reading and writing

Simple usage:

		try (OutputStream out = new PasswordProtectedOutputStream(outputstream, password)) {
			// write as your normal outputstream
		}

		try (InputStream in = new PasswordProtectedInputStream(inputstream, password) ) {
				//read as a normal inputstream
		}

The input/output streams uses default salt and most best encryption algorithm. It is possible to specify your own
