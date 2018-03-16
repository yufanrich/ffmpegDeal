package com.yufanstudio.jffmpeg;

import com.yufanstudio.jffmpeg.util.FfmpegVideoUtil;

/**
 * Hello world!
 *
 */
public class TestMain {
    public static void main( String[] args ){
    	String filePath="D:/ftp/test2.ts";
    	String videoMsg = FfmpegVideoUtil.getVideoMsg(filePath);
    	//System.out.println("json格式视频详细信息:"+videoMsg);
    	System.out.println("得到视频宽高码率"+FfmpegVideoUtil.getVideoWHB(videoMsg));
    	
    	System.out.println("得到视频时长码率:"+FfmpegVideoUtil.getVideoTime(filePath));
    }
}
