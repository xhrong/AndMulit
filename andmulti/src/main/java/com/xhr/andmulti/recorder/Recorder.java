package com.xhr.andmulti.recorder;

import android.media.MediaCodec;
import android.media.MediaFormat;

import com.xhr.andmulti.encoder.BaseEncoder;

import java.io.IOException;

/**
 * Created by xhrong on 2017/12/21.
 */

public interface Recorder {
    public void prepare() throws IOException;

    public void stop();

    public void release();

    public void setCallback(Callback callback);

    public interface Callback {
        public void onError(Recorder recorder, Exception exception);

        public void onInputBufferAvailable(Recorder recorder, int index);
        public  void onOutputFormatChanged(Recorder recorder, MediaFormat format);

        public  void onOutputBufferAvailable(Recorder recorder, int index, MediaCodec.BufferInfo info) ;
    }
}
