package cn.rong.wechat.mediacodec;

public interface MediaCodecable
{
	int initCodec();
	
	boolean isEncoder();
	
	int process(MediaCodecData inputData, MediaCodecData outputData);
	
	void release();
	
}
