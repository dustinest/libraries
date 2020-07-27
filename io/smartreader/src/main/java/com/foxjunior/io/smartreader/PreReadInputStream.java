package com.foxjunior.io.smartreader;

import java.io.IOException;
import java.io.InputStream;

class PreReadInputStream extends InputStream {
    private final InputStream in;
    private byte[] buffer;
    private int pointer = 0;

    PreReadInputStream(InputStream in, byte[] buffer) {
        this.in = in;
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        if (pointer < buffer.length) {
            pointer ++;
            return buffer[pointer-1];
        }
        return in.read();
    }

    /**
     * Extend buiffer which were pre read.
     */
    /*
    public void extend(byte[] extension) {
        byte[] sv = new byte[extension.length + buffer.length - pointer];
        for (int i = pointer; i < buffer.length; i++) {
            sv[i-pointer] = buffer[i];
        }
        for (int i = 0; i < extension.length; i++) {
            sv[buffer.length - pointer + i] = extension[i];
        }
        this.buffer = sv;
        pointer = 0;
    }
     */

    @Override
    public void close() throws IOException {
        in.close();
    }
}
