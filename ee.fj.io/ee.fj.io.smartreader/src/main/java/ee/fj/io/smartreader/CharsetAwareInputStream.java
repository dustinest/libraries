package ee.fj.io.smartreader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class CharsetAwareInputStream extends InputStream {
	private final InputStream in;
	private final Charset charset;
	private final byte[] bom;

	public CharsetAwareInputStream(byte[] bom, InputStream in, Charset charset) {
		this.in = in;
		this.charset = charset;
		this.bom = bom;
	}

	public byte[] getBom() {
		return bom;
	}

	public Charset getCharset() {
		return charset;
	}
	
	@Override
	public int read() throws IOException {
		return in.read();
	}

    @Override
    public void close() throws IOException {
        in.close();
    }

    /**
     * Similar to Investigate tries to find out encoding but instead of throwing an Exception tries to figure out
     * encoding using statistics. If encoding is unknown UTF8 is used.
     * @param in
     * @return
     * @throws IOException
     */
    public static CharsetAwareInputStream predict(InputStream in) throws IOException {
    	return Encoding.predict(in);
    }

    /**
     * Investigate incoming file. If file encoding is unknown exception is thrown.
     * @param in
     * @return
     */
    public static CharsetAwareInputStream investigate(InputStream in) throws IOException {
    	return Encoding.investigate(in);
    }
}
