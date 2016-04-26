package org.miniserver.core.http;

import java.io.IOException;
import java.io.InputStream;

public class ServletSocketInputStream extends InputStream{

    /**
     * Internal buffer.
     */
    protected byte buf[];


    /**
     * Last valid byte.
     */
    protected int count;


    /**
     * Position in the buffer.
     */
    protected int pos;


    /**
     * Underlying input stream.
     */
    protected InputStream is;


    // ----------------------------------------------------------- Constructors


    /**
     * Construct a servlet input stream associated with the specified socket
     * input.
     *
     * @param is socket input stream
     * @param bufferSize size of the internal buffer
     */
    public ServletSocketInputStream(InputStream is, int bufferSize) {

        this.is = is;
        buf = new byte[bufferSize];

    }

    /**
     * Read byte.
     */
    @Override
    public int read()
        throws IOException {
        if (pos >= count) {
            fill();
            if (pos >= count)
                return -1;
        }
        return buf[pos++] & 0xff;
    }

    /**
     * Returns the number of bytes that can be read from this input
     * stream without blocking.
     */
    @Override
    public int available()
        throws IOException {
        return (count - pos) + is.available();
    }


    /**
     * Close the input stream.
     */
    @Override
    public void close()
        throws IOException {
        if (is == null)
            return;
        is.close();
        is = null;
        buf = null;
    }


    // ------------------------------------------------------ Protected Methods
    /**
     * Fill the internal buffer using data from the undelying input stream.
     */
    protected void fill()
        throws IOException {
        pos = 0;
        count = 0;
        int nRead = is.read(buf, 0, buf.length);
        if (nRead > 0) {
            count = nRead;
        }
    }


}
