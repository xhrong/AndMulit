package com.xhr.andmulti.encoder;

import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;


import com.xhr.andmulti.encoder.glec.EGLRender;

import java.io.IOException;
import java.util.Objects;


public class VideoEncoder extends BaseEncoder {
    private static final boolean VERBOSE = false;
    private VideoEncodeConfig mConfig;
    private Surface mSurface;

    private EGLRender eglRender;
    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    public VideoEncoder(VideoEncodeConfig config) {
        super(config.codecName);
        this.mConfig = config;

    }

    @Override
    protected void onEncoderConfigured(MediaCodec encoder) {
        mSurface = encoder.createInputSurface();
        if (VERBOSE) Log.i("@@", "VideoEncoder create input surface: " + mSurface);

        eglRender = new EGLRender(mSurface, mConfig.width, mConfig.height, mConfig.framerate);
        eglRender.setCallBack(new EGLRender.OnFrameCallBack() {
            @Override
            public void onUpdate() {
                if (getEncoder() == null) return;
                int index = getEncoder().dequeueOutputBuffer(mBufferInfo, 0);
                if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    MediaFormat newFormat = getEncoder().getOutputFormat();
                    mCallback.onOutputFormatChanged(VideoEncoder.this, newFormat);
                } else if (index == MediaCodec.INFO_TRY_AGAIN_LATER) {

                } else if (index >= 0) {
                    mCallback.onOutputBufferAvailable(VideoEncoder.this, index, mBufferInfo);
                }
            }

            @Override
            public void onCutScreen(Bitmap bitmap) {

            }
        });

    }

    @Override
    protected MediaFormat createMediaFormat() {
        return mConfig.toFormat();
    }

    /**
     * @throws NullPointerException if prepare() not call
     */
    public Surface getInputSurface() {
        if (eglRender == null) {
            return Objects.requireNonNull(mSurface, "doesn't prepare()");
        } else {
            return eglRender.getDecodeSurface();
        }
    }

    @Override
    public void release() {

        if (eglRender != null) {
            eglRender.release();
            eglRender = null;
        }

        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        super.release();
    }


    @Override
    public void prepare() throws IOException {
        super.prepare();
    }


    @Override
    public void stop() {

        if (eglRender != null) {
            eglRender.stop();
        }
        super.stop();
    }
}
