package com.xhr.andmulti.encoder;

import java.io.IOException;


public interface Encoder {
    public void prepare() throws IOException;

    public  void stop();

    public void release();

    public void setCallback(Callback callback);

    public  interface Callback {
        void onError(Encoder encoder, Exception exception);

    }
}
