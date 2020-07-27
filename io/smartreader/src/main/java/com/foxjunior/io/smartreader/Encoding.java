package com.foxjunior.io.smartreader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public enum Encoding {
    UTF8(StandardCharsets.UTF_8, 0x02, 1,4, null),
	UTF16le(StandardCharsets.UTF_16LE, 0x03, 2,2, new byte[]{-1, -2}), // 0xFF, 0xFE
	UTF16be(StandardCharsets.UTF_16BE, 0x04, 2,2, new byte[]{-2, -1}), // 0xFE, 0xFF
	UTF16(StandardCharsets.UTF_16, 0x05, 2,2, null); //NB! in Java it fails back to is UTF-16BE with BOM

    public final Charset charset;
    final byte type;
    final byte[] BOM;
    final int minLength;
    final int maxLength;
    final byte[] charBuffer;
    public static final Charset SYSTEM_CHARSET = Charset.defaultCharset();

    Encoding(Charset charset, int type, int minLength, int maxLength, byte[] bom) {
        this.charset = charset;
        this.type = (byte)type;
        this.BOM = bom;
        this.minLength = minLength;
        this.maxLength = maxLength;
        charBuffer = new byte[minLength];
    }

    public static final Encoding DEFAULT = Encoding.UTF8;

    /**
     * Convert byte array from encoding specified
     * @param from - encoding to encode from
     * @param data - data to convert
     */
    public String convertToString(Charset from, byte[] data) {
        return from == null ? new String(data, DEFAULT.charset) : new String(data, from);
    }

    /**
     * Convert String from encoding specified
     * @param from - encoding to encode from
     * @param data - data to convert
     */
    public String convert(Charset from, CharSequence data) {
        return convertToString(from, data.toString().getBytes(from == null ? DEFAULT.charset : from));
    }

    /**
     * Convert byte array from default encoding
     * @param data - data to convert
     */
    public String convertToString(byte[] data) {
        return convertToString(null, data);
    }


    /**
     * Convert byte array from encoding specified
     * @param from - encoding to encode from
     * @param data - data to convert
     */
    public byte[] convert(Charset from, byte[] data) {
        return convertToString(from, data).getBytes(this.charset);
    }


    /**
     * Convert byte array from default encoding
     * @param data - data to convert
     */
    public byte[] convert(byte[] data) {
        return convert(null, data);
    }

    /**
     * Convert String from encoding specified
     * @param from - encoding to encode from
     * @param data - data to convert
     */
    public byte[] convertToBytes(Charset from, CharSequence data) {
        return convert(from, data.toString()).getBytes(this.charset);
    }


    /**
     * Convert String from default encoding
     * @param data - data to convert
     */
    public byte[] convertToBytes(CharSequence data) {
        return convertToBytes(null, data);
    }

    /**
     * Investigates BOM and returns true if it matches. Returns false if BOM is not found
     */
    private boolean isBOM(byte[] bytes) {
        if (this.BOM == null || this.BOM.length > bytes.length)
            return false;
        for (int i = 0; i < this.BOM.length; i++) {
            if (bytes[i] != this.BOM[i])
                return false;
        }
        return true;
    }

    /**
     * removes BOM (if exists) from the beginning
     * @return stripped bytes
     */
    public byte[] removeBOM(byte[] bytes) {
        if (this == UTF16) {
            if (UTF16le.isBOM(bytes))
                return UTF16le.removeBOM(bytes);
            if (UTF16be.isBOM(bytes))
                return UTF16be.removeBOM(bytes);
            return bytes;
        }
        if (this.BOM == null || !isBOM(bytes))
            return bytes;

        byte[] rv = new byte[bytes.length - BOM.length];
        for (int i = BOM.length; i < bytes.length; i++) {
            rv[i - BOM.length] = bytes[i];
        }
        return rv;
    }

    /**
     * Reads first 500 bytes from inputstream. and returns byte array.
     * If less bytes is read the array size is reduced.
     * @throws IOException if inpustream read fails or is empty stream
     */
    private static byte[] read(InputStream in) throws IOException {
        byte[] data = new byte[500];
        int len = in.read(data);
        if (len == -1) {
            throw new IOException("Inputstream is empty!");
        } else if (len < data.length) {
            byte[] c = new byte[len];
			System.arraycopy(data, 0, c, 0, len);
            return c;
        }
        return data;
    }


    /**
     * Investigate incoming file. If file encoding is unknown exception is thrown.
     */
    public static CharsetAwareInputStream investigate(InputStream in) throws IOException {
		return investigateWithArray(in, read(in));
    }

    private static CharsetAwareInputStream investigateWithArray(InputStream in, byte[] data) {
		Encoding e = getEncoding(data);
		InputStream inp = new PreReadInputStream(in, e.removeBOM(data));

		byte[] bom = e.BOM != null && e.BOM.length > 0 ? new byte[e.BOM.length] : null;
		if (bom != null) {
			System.arraycopy(e.BOM, 0, bom, 0, e.BOM.length);
		}

		return new CharsetAwareInputStream(bom, inp, e.charset);
	}

    public static CharsetAwareInputStream predict(Path filePath) throws IOException {
    	return predict(filePath, Charset.defaultCharset());
    }

    public static CharsetAwareInputStream predict(Path filePath, Charset preferred) throws IOException {
    	Charset rv;
		int meaningfulDefaults = 0;
		int meaningfulPreferrd = 0;
    	try (CharsetAwareInputStream in = predict(Files.newInputStream(filePath))) {
    		rv = in.getCharset();
    		if (preferred == null || rv.name().equals(preferred.name())) {
    			return in;
    		}
    		try (InputStreamReader reader = new InputStreamReader(in, rv)) {
    			int _char = reader.read();
    			while(_char != -1) {
	    			if (Character.isLetterOrDigit(_char)) {
	    				meaningfulDefaults++;
	    			}
	    			_char = reader.read();
    			}
    		}
    	}
		try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(filePath), preferred)) {
			int _char = reader.read();
			while(_char != -1) {
    			if (Character.isLetterOrDigit(_char)) {
    				meaningfulPreferrd++;
    			}
    			_char = reader.read();
			}
		}
		rv = meaningfulPreferrd > meaningfulDefaults ? preferred : rv;
		return new CharsetAwareInputStream(null, Files.newInputStream(filePath), rv);
    }

    /**
     * Similar to Investigate tries to find out encoding but instead of throwing an Exception tries to figure out
     * encoding using statistics. If encoding is unknown UTF8 is used.
     */
    public static CharsetAwareInputStream predict(InputStream in) throws IOException {
    	byte[] data = read(in);
        try {
        	return investigateWithArray(in, data);
        } catch (IllegalArgumentException e) {
            // ignore - BOM not found!
        }
        float be = 0;
        float le = 0;
        for (int i = 0; i < data.length-1; i+=2) {
            if (data[i] == 0)
                be += 1;
            else if (data[i+1] == 0)
                le += 1;
        }

        if (be > le) {
            InputStream inp = new PreReadInputStream(in, Encoding.UTF16.removeBOM(data));
            return new CharsetAwareInputStream(null, inp,  Encoding.UTF16.charset);
        } else if (le > be) {
            InputStream inp = new PreReadInputStream(in, Encoding.UTF16le.removeBOM(data));
            return new CharsetAwareInputStream(null, inp,  Encoding.UTF16le.charset);
        }
        if (!SYSTEM_CHARSET.equals(StandardCharsets.UTF_8)) {
        	int errorsInUtf8Charset = countErrorsInCharset(data, StandardCharsets.UTF_8);
        	if (errorsInUtf8Charset > 0) {
        		int errorsInSystemCharset = countErrorsInCharset(data, SYSTEM_CHARSET);
            	if (errorsInSystemCharset < errorsInUtf8Charset) {
            		return new CharsetAwareInputStream(null, new PreReadInputStream(in, data),  SYSTEM_CHARSET);
            	}
        	}
        }
        InputStream inp = new PreReadInputStream(in, Encoding.UTF8.removeBOM(data));
        return new CharsetAwareInputStream(null, inp,  Encoding.UTF8.charset);
    }

    private static int countErrorsInCharset(byte[] data, Charset charset) {
		String valueInCharset = new String(data, 0, data.length, charset);
		int otherValueCount = 0;
		for (int i = 0; i < valueInCharset.length(); i++) {
			int type =  Character.getType(valueInCharset.charAt(i));
			if (type == Character.OTHER_PUNCTUATION || type == Character.OTHER_SYMBOL) {
				otherValueCount++;
			}
		}
		return otherValueCount;
	}

    /**
     * investigate bytes and predict what encoding it may have.
     * @throws IllegalArgumentException if BOM is not found
     */
    public static Encoding getEncoding(byte[] data) {
        for (Encoding e : Encoding.values()) {
            if (e.isBOM(data))
                return e;
        }
        throw new IllegalArgumentException("Charset not supported");
    }

    /**
     * Get encoding by identifier byte
     */
    protected static Encoding getEncoding(byte b) {
        for (Encoding e : Encoding.values()) {
            if (e.type == b)
                return e;
        }
        throw new IllegalArgumentException("Charset not supported");
    }

    public static Encoding getEncoding(Charset encoding) {
        for (Encoding e : Encoding.values()) {
            if (e.charset.equals(encoding))
                return e;
        }
        throw new IllegalArgumentException("Charset " + encoding + "not supported");
    }

    public static Encoding getEncoding(String encoding) {
        return getEncoding(Charset.forName(encoding));
    }
}
