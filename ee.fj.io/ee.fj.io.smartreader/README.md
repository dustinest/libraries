# Automatically detect text file encodings.

Work still on progress.

Use CharsetAwareInputStream for an entry point:

	CharsetAwareInputStream in = CharsetAwareInputStream.predict(InputSream in , Charset failbackCharset;

or

	CharsetAwareInputStream in = CharsetAwareInputStream.predict(InputStream in)

Recognizes UTF_8, UTF8 Little Endian and Big Endian, fails back to system default or preferred charset.