package cn.rong.wechat.audio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.SPUtils;

import cn.rong.wechat.R;
import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.base.RCRTCParamsType;
import cn.rongcloud.rtc.core.audio.AudioResample;

public class AudioMainActivity extends AppCompatActivity implements View.OnClickListener {
    private AudioRecord mAudioRecoder;
    private AudioTrack mAudioTrack;
    private Button mAudioData, mTrackPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_main);
        SPUtils.getInstance().put("AAA", "sss");
        SPUtils.getInstance().getString("AAA");
        initView();
    }

    private void initView() {
        mAudioData = findViewById(R.id.get_audio_data);
        mTrackPlay = findViewById(R.id.track_play);
        mAudioData.setOnClickListener(this);
        mTrackPlay.setOnClickListener(this);
        /// RCRTCEngine.getInstance().getDefaultAudioStream().setAudioQuality(RCRTCParamsType.AudioQuality.SPEECH, RCRTCParamsType.AudioScenario.GAMING_CHATROOM);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_audio_data:

                break;
            case R.id.track_play:
                break;
        }
    }

    private void startAudioRecoder() {
        int minButtferSize = AudioRecord.getMinBufferSize(AudioConfig.audiorecoderSample, AudioConfig.audiorecoderChannel, AudioConfig.encodeBit);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            mAudioRecoder = new AudioRecord(MediaRecorder.AudioSource.MIC, AudioConfig.audiorecoderSample,
                    AudioConfig.audiorecoderChannel, AudioConfig.encodeBit, minButtferSize);
        }
        Byte[] bytes = new Byte[minButtferSize];

    }
}