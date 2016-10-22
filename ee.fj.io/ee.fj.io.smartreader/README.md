# Automatically detect text file encodings.

Work still on progress.

Use CharsetAwareInputStream for an entry point:

	CharsetAwareInputStream in = CharsetAwareInputStream.predict(InputSream in , Charset failbackCharset;

or

	CharsetAwareInputStream in = CharsetAwareInputStream.predict(InputStream in)

To get the charset:

		Charset charset_used = in.getCharset():
	
Recognizes UTF_8, UTF8 Little Endian and Big Endian, fails back to system default or preferred charset.

TODO: add more charsets to regognize