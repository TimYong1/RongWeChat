package cn.rong.wechat.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;

public class AudioConfig {
    public final static int audiorecoderSample = 16000;
    public final static int audiorecoderChannel = AudioFormat.CHANNEL_IN_MONO;
    public final static int encodeBit = AudioFormat.ENCODING_PCM_16BIT;
}
