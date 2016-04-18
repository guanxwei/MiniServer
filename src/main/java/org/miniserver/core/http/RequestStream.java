package org.miniserver.core.http;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class RequestStream extends ServletInputStream {

    private InputStream input;

    public RequestStream(InputStream input) {
        this.input = input;
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int read() throws IOException {
        return input.read();
    }

}
