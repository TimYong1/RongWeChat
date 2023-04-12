package cn.rong.wechat.utils;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MediaCodecHelper {
    private MediaCodec mAudioMediaCodec, mVideoCodec;
    private MediaFormat mAudioMediaFormat, mVideoMediaFormat;
    private AudioParameter mAudioParameter;
    private VideoParameter mVideoParameter;
    private int mAudioTrackIndex = -1;
    private int mVideoTrackIndex = -1;
    private MediaMuxer mediaMuxer;
    private long audioPts = 0;
    private long pts = 0;

    private static final int WAIT_TIME = 0;
    private long nonaTime;
    private boolean isStart;



    private String filePath;

    public MediaCodecHelper(String filePath, AudioParameter audioParameter, VideoParameter videoParameter) {
        this.filePath = filePath;
        this.mAudioParameter = audioParameter;
        this.mVideoParameter = videoParameter;
    }

    private void initVideoCodec(VideoParameter videoParameter) {
        this.mVideoParameter = videoParameter;
        this.mVideoMediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, this.mVideoParameter.width, this.mVideoParameter.height);
        this.mVideoMediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
        this.mVideoMediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);//30帧
        this.mVideoMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, this.mVideoParameter.width * this.mVideoParameter.height * 4);//RGBA
        this.mVideoMediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
//        设置压缩等级  默认是baseline
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.mVideoMediaFormat.setInteger(MediaFormat.KEY_PROFILE, MediaCodecInfo.CodecProfileLevel.AVCProfileMain);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.mVideoMediaFormat.setInteger(MediaFormat.KEY_LEVEL, MediaCodecInfo.CodecProfileLevel.AVCLevel3);
            }
        }
        try {
            this.mVideoCodec = MediaCodec.createEncoderByType(this.mVideoMediaFormat.getString(MediaFormat.KEY_MIME));
            this.mVideoCodec.configure(this.mVideoMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initAudioCodec(AudioParameter audioParameter) {
        this.mAudioParameter = audioParameter;
        this.mAudioMediaFormat = new MediaFormat();
        this.mAudioMediaFormat.setString(MediaFormat.KEY_MIME, this.mAudioParameter.mediaMime);
        this.mAudioMediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, this.mAudioParameter.channel);
        this.mAudioMediaFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, this.mAudioParameter.sampleRate);
        this.mAudioMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, this.mAudioParameter.bitrate);
        this.mAudioMediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, this.mAudioParameter.profile);
        this.mAudioMediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, Integer.MAX_VALUE);

        try {
            this.mAudioMediaCodec = MediaCodec.createEncoderByType(this.mAudioMediaFormat.getString(MediaFormat.KEY_MIME));
            this.mAudioMediaCodec.configure(this.mAudioMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void encodeVideo(byte[] nv12, long nanoTime) {
        if (this.mVideoCodec == null) return;
        try {
            int index = this.mVideoCodec.dequeueInputBuffer(WAIT_TIME);
            if (index >= 0) {
                ByteBuffer inputBuffer = this.mVideoCodec.getInputBuffer(index);
                inputBuffer.clear();
                inputBuffer.put(nv12);
                this.mVideoCodec.queueInputBuffer(index, 0, nv12.length, (System.nanoTime() - this.nonaTime) / 1000, 0);
            }
            encodeVideoH264();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encodeVideoH264() {
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = this.mVideoCodec.dequeueOutputBuffer(bufferInfo, WAIT_TIME);
        if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            this.mVideoTrackIndex = mediaMuxer.addTrack(this.mVideoCodec.getOutputFormat());
            if (this.mAudioTrackIndex != -1 && this.mVideoTrackIndex != -1) {
                this.mediaMuxer.start();
            }
        }
        while (outputBufferIndex >= 0) {
            ByteBuffer outputBuffer = this.mVideoCodec.getOutputBuffer(outputBufferIndex);
            if (this.mVideoTrackIndex != -1) {
                mediaMuxer.writeSampleData(this.mVideoTrackIndex, outputBuffer, bufferInfo);
            }
            this.mVideoCodec.releaseOutputBuffer(outputBufferIndex, false);
            outputBufferIndex = this.mVideoCodec.dequeueOutputBuffer(bufferInfo, WAIT_TIME);
            if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                break;
            }
        }
    }

    public void encodeAudioToAAC(byte[] data, int len) {
        try {
            int index = this.mAudioMediaCodec.dequeueInputBuffer(WAIT_TIME);
            if (index >= 0) {
                ByteBuffer inputBuffer = this.mAudioMediaCodec.getInputBuffer(index);
                inputBuffer.clear();
                inputBuffer.put(data);
                long pts = getAudioPts(len, this.mAudioParameter.sampleRate, this.mAudioParameter.channel, this.mAudioParameter.bitrate);
                //数据缓冲送入解码器
                this.mAudioMediaCodec.queueInputBuffer(index, 0, data.length, (System.nanoTime() - this.nonaTime) / 1000, 0);
            }
            getEncodeData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEncodeData() {
        MediaCodec.BufferInfo outputBuffer = new MediaCodec.BufferInfo();
        int flag = this.mAudioMediaCodec.dequeueOutputBuffer(outputBuffer, WAIT_TIME);
        if (flag == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            this.mAudioTrackIndex = this.mediaMuxer.addTrack(this.mAudioMediaCodec.getOutputFormat());
            if (this.mAudioTrackIndex != -1 && this.mVideoTrackIndex != -1) {
                this.mediaMuxer.start();
            }
        } else {
            while (flag >= 0) {
                ByteBuffer data = this.mAudioMediaCodec.getOutputBuffer(flag);
                if (data != null) {

                    if (this.mAudioTrackIndex != -1) {
                        this.mediaMuxer.writeSampleData(this.mAudioTrackIndex, data, outputBuffer);
                    }
                }
                this.mAudioMediaCodec.releaseOutputBuffer(flag, false);
                flag = this.mAudioMediaCodec.dequeueOutputBuffer(outputBuffer, WAIT_TIME);
            }
        }

    }

    public void stop() {
        this.mAudioMediaCodec.stop();
        this.mAudioMediaCodec.release();
        this.mVideoCodec.stop();
        this.mVideoCodec.release();
        this.mediaMuxer.stop();
        this.mediaMuxer.release();
        this.isStart = false;
    }

    public void start() {
        this.isStart = true;
        init();
        this.mAudioTrackIndex = -1;
        this.mVideoTrackIndex = -1;
        nonaTime = System.nanoTime();
        this.mVideoCodec.start();
        this.mAudioMediaCodec.start();
    }

    private void init() {
        initAudioCodec(this.mAudioParameter);
        initVideoCodec(this.mVideoParameter);
        initMediaEncoder(filePath);
    }


    private void initMediaEncoder(String savePath) {
        this.filePath = savePath;
        try {
            this.mediaMuxer = new MediaMuxer(savePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            this.mediaMuxer.setOrientationHint(90);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //176400
    private long getAudioPts(int size, int sampleRate, int channel, int sampleBit) {
        audioPts += (long) (1.0 * size / (sampleRate * channel * (sampleBit / 8)) * 1000000.0);
        return audioPts;
    }


    public static class VideoParameter {
        int width;
        int height;

        public VideoParameter(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static class AudioParameter {
        String mediaMime;
        int channel;
        int sampleRate;
        int bitrate;
        int profile = MediaCodecInfo.CodecProfileLevel.AACObjectLC;


        public AudioParameter(String mediaMime, int channel, int sampleRate, int bitrate, int profile) {
            this.mediaMime = mediaMime;
            this.channel = channel;
            this.sampleRate = sampleRate;
            this.bitrate = bitrate;
            this.profile = profile;
        }

        public AudioParameter(String mediaMime, int channel, int sampleRate, int bitrate) {
            this.mediaMime = mediaMime;
            this.channel = channel;
            this.sampleRate = sampleRate;
            this.bitrate = bitrate;
        }

        public static class Builder {
            String mediaMime;
            int channel;
            int sampleRate;
            int bitrate;
            int profile = -1;

            static Builder instence = new Builder();

            public static Builder getInstence() {
                return instence;
            }

            public Builder setMediaMime(String mediaMime) {
                this.mediaMime = mediaMime;
                return this;
            }

            public Builder setChannel(int channel) {
                this.channel = channel;
                return this;
            }

            public Builder setSampleRate(int sampleRate) {
                this.sampleRate = sampleRate;
                return this;
            }

            public Builder setBitrate(int bitrate) {
                this.bitrate = bitrate;
                return this;
            }

            public Builder setProfile(int profile) {
                this.profile = profile;
                return this;
            }

            public AudioParameter build() {
                if (profile == -1) {
                    return new AudioParameter(this.mediaMime, this.channel, this.sampleRate, this.bitrate);
                } else {
                    return new AudioParameter(this.mediaMime, this.channel, this.sampleRate, this.bitrate, this.profile);
                }
            }
        }
    }
}