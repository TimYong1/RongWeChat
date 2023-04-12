package cn.rong.wechat.utils;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;

public class AudioHelper implements Runnable {
    private static final int SAMPLE_RATE = 44100;
    private AudioRecord mAudioRecord;
    private Thread mAudioThread;
    private int mAudioRecordBufferSize;
    private boolean isStartRecording = false;
    private AudioDataCallback mAudioDataCallback;

    public AudioHelper() {
        this.mAudioThread = new Thread(this, "AudioThread");
        initAudioRecord();
    }

    private void initAudioRecord() {
        this.mAudioRecordBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.mAudioRecord = new AudioRecord.Builder()
                    .setAudioSource(MediaRecorder.AudioSource.MIC)
                    .setAudioFormat(new AudioFormat.Builder()
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_IN_MONO)
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .build())
                    .setBufferSizeInBytes(this.mAudioRecordBufferSize)
                    .build();
        } else {
            this.mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, this.mAudioRecordBufferSize);
        }
    }

    public void start() {
        if (!this.isStartRecording) {
            this.mAudioThread = new Thread(this, "AudioThread");
            this.mAudioThread.start();
        }
    }

    public void stop() {
        this.isStartRecording = false;
        this.mAudioRecord.stop();
    }

    public void setAudioDataCallback(AudioDataCallback callback) {
        this.mAudioDataCallback = callback;
    }


    @Override
    public void run() {
        this.mAudioRecord.startRecording();
        this.isStartRecording = true;
        byte[] data = new byte[this.mAudioRecordBufferSize];
        while (this.isStartRecording) {
            int res = this.mAudioRecord.read(data, 0, this.mAudioRecordBufferSize);
            if (res != AudioRecord.ERROR_INVALID_OPERATION) {
                if (this.mAudioDataCallback != null) {
                    this.mAudioDataCallback.onData(data, res);
                }
            }
        }
    }

    public interface AudioDataCallback {
        void onData(byte[] data, int length);
    }
}
