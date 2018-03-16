package com.yufanstudio.jffmpeg.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class FfmpegVideoUtil { //查看视频图片相关信息
	static String fFMPEG_PATH = FfmpegConstant.FFMPEG_PATH;
	
	
	
	//1 以json格式得到视频信息
	public static String getVideoMsg(String filePath){
		//拼接命令
		 List<String> commend = new java.util.ArrayList<String>();
	        commend.add(fFMPEG_PATH+"/ffprobe");
	        commend.add("-v");
	        commend.add("quiet");
	        commend.add("-print_format");
	        commend.add("json");
	        commend.add("-show_format");
	        commend.add("-show_streams");
	        
	        commend.add(filePath);
	        
	        try {
	            ProcessBuilder builder = new ProcessBuilder();
	            builder.command(commend);
	            builder.redirectErrorStream(true);
	            Process p = builder.start();
	            // 1. start
	            BufferedReader buf = null; // 保存ffmpeg的输出结果流
	            String line = null;
	            // read the standard output

	            buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
	            StringBuffer sb = new StringBuffer();
	            while ((line = buf.readLine()) != null) {
	                //System.out.println(line);
	                sb.append(line);
	                continue;
	            }
	            p.waitFor();// 这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行
	            // 1. end
	            return sb.toString();
	        } catch (Exception e) {
	            // System.out.println(e);
	            return null;
	      }
		
	}//以json格式得到视频详细信息
	
	//2得到视频的宽 width 高height 码率bite
	public static String getVideoWHB(String videoMsg){
		String result=null;
		if(null==videoMsg){
			return null;
		}
		JsonObject jsonObject = new JsonParser().parse(videoMsg).getAsJsonObject();
		JsonArray array = jsonObject.getAsJsonArray("streams");
		
		JsonObject joBit = jsonObject.getAsJsonObject("format");
		String bitRate = joBit.get("bit_rate").getAsString();
		
		
		
		String width = "";
		String height = "";
		int times=0;
		if(array.size()>0){
			JsonObject jd =  array.get(0).getAsJsonObject();
			if(jd.has("width")){
				width = jd.getAsJsonPrimitive("width").getAsString();
			}
			
			if(jd.has("height")){
				height = jd.getAsJsonPrimitive("height").getAsString();
			}
			if(jd.has("duration")){
				BigDecimal setScale = jd.getAsJsonPrimitive("duration").getAsBigDecimal().setScale(0, RoundingMode.UP);
				times=setScale.intValue();
			}
		}
		
		result=" 宽:"+width+" 高:"+height+" 码率:"+bitRate+" 时长:"+times;
		return result;
	}
	
	
	
	//3 得到视频时长 码率
	  public static String getVideoTime(String video_path) {  
		  
		    String result=null;
		    int timeS; //单位秒
		    String malv;
		  
	        List<String> commands = new java.util.ArrayList<String>();  
	        commands.add(fFMPEG_PATH+"/ffmpeg");  
	        commands.add("-i");  
	        commands.add(video_path);  
	        try {  
	            ProcessBuilder builder = new ProcessBuilder();  
	            builder.command(commands);  
	            final Process p = builder.start();  
	              
	            //从输入流中读取视频信息  
	            BufferedReader br = new BufferedReader(new InputStreamReader(p.getErrorStream()));  
	            StringBuffer sb = new StringBuffer();  
	            String line = "";  
	            while ((line = br.readLine()) != null) {  
	                sb.append(line);  
	            }  
	            br.close();  
	              
	            //从视频信息中解析时长  
	            String regexDuration = "Duration: (.*?), start: (.*?), bitrate: (\\d*) kb\\/s";  
	            Pattern pattern = Pattern.compile(regexDuration);  
	            Matcher m = pattern.matcher(sb.toString());  
	            if (m.find()) {  
	            	timeS = getTimelen(m.group(1));  
	            	malv=  m.group(3);
	            	result=" 时长:"+timeS+" 码率"+malv+" kb/s";
	            }  
	           
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	          
	        return result;  
	    }  
	  
	  	//格式:"00:00:10.68"  
	    private static int getTimelen(String timelen){  
	        int min=0;  
	        String strs[] = timelen.split(":");  
	        if (strs[0].compareTo("0") > 0) {  
	            min+=Integer.valueOf(strs[0])*60*60;//秒  
	        }  
	        if(strs[1].compareTo("0")>0){  
	            min+=Integer.valueOf(strs[1])*60;  
	        }  
	        if(strs[2].compareTo("0")>0){  
	            min+=Math.round(Float.valueOf(strs[2]));  
	        }  
	        return min;  
	    }  
}
