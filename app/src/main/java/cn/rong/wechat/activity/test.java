package cn.rong.wechat.activity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.rongcloud.rtc.api.RCRTCEngine;
import cn.rongcloud.rtc.api.RCRTCMixConfig;
import cn.rongcloud.rtc.api.stream.RCRTCVideoStreamConfig;
import cn.rongcloud.rtc.base.RCRTCStream;

public class test {

    public RCRTCMixConfig create_Custom_MixConfig(List<RCRTCStream> rtcStreams) {
        RCRTCMixConfig config = new RCRTCMixConfig();
        // 1. 设置自定义合流布局模式
        config.setLayoutMode(RCRTCMixConfig.MixLayoutMode.CUSTOM);

        // 2. 合流画布设置
        canvasConfiguration(config);

        // 3. 设置每个视频流小窗口的坐标及宽高
        ArrayList<RCRTCMixConfig.CustomLayoutList.CustomLayout> list = new ArrayList<>();
        RCRTCMixConfig.CustomLayoutList.CustomLayout videoLayout = null;

        RCRTCVideoStreamConfig defaultVideoConfig = RCRTCEngine.getInstance().getDefaultVideoStream().getVideoConfig();
        int width = defaultVideoConfig.getVideoResolution().getWidth();//默认 480
        int height = defaultVideoConfig.getVideoResolution().getHeight();//默认 640
        int size = rtcStreams.size();
        for (int i = 0; i < size; i++) {
            videoLayout = new RCRTCMixConfig.CustomLayoutList.CustomLayout();
            videoLayout.setVideoStream(rtcStreams.get(i));
            if (size == 1) {
                Log.d("RTCMixLayout", "  ---single");
                single(videoLayout, i, width, height);
            }
            list.add(videoLayout);
        }
        config.setCustomLayouts(list);
        return config;
    }

    /**
     * 画布设置
     */
    private RCRTCMixConfig canvasConfiguration(RCRTCMixConfig config) {
        // TODO RCRTCMixConfig API文档：https://www.rongcloud.cn/docs/api/android/rtclib_v4/cn/rongcloud/rtc/api/RCRTCMixConfig.html
        RCRTCMixConfig.MediaConfig mediaConfig = new RCRTCMixConfig.MediaConfig();
        config.setMediaConfig(mediaConfig);
        // 视频输出配置
        RCRTCMixConfig.MediaConfig.VideoConfig videoConfig = new RCRTCMixConfig.MediaConfig.VideoConfig();
        mediaConfig.setVideoConfig(videoConfig);
        // 大流视频的输出参数
        RCRTCMixConfig.MediaConfig.VideoConfig.VideoLayout normal = new RCRTCMixConfig.MediaConfig.VideoConfig.VideoLayout();
        videoConfig.setVideoLayout(normal);
        // TODO RCRTCVideoStreamConfig API文档：https://www.rongcloud.cn/docs/api/android/rtclib_v4/cn/rongcloud/rtc/api/stream/RCRTCVideoStreamConfig.html
        RCRTCVideoStreamConfig defaultVideoConfig = RCRTCEngine.getInstance().getDefaultVideoStream().getVideoConfig();
        int fps = defaultVideoConfig.getVideoFps().getFps();
        int width = defaultVideoConfig.getVideoResolution().getWidth();
        int height = defaultVideoConfig.getVideoResolution().getHeight();
        normal.setWidth(width);   //视频宽
        normal.setHeight(height); //视频高
        normal.setFps(fps); //视频帧率
        videoConfig.setExtend(new RCRTCMixConfig.MediaConfig.VideoConfig.VideoExtend(RCRTCMixConfig.VideoRenderMode.WHOLE));
        return config;
    }

    private RCRTCMixConfig.CustomLayoutList.CustomLayout single(RCRTCMixConfig.CustomLayoutList.CustomLayout videoLayout, int i, int width, int height) {
        videoLayout.setX(0);
        videoLayout.setY(0);
        videoLayout.setWidth(width);
        videoLayout.setHeight(height);
        return videoLayout;
    }
}
