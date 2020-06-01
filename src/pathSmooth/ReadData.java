package pathSmooth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
/*
 * 从文本中读取ADS-B轨迹
 */

import dataEntity.Track;
import dataEntity.TrackPoint;

public class ReadData {

	public static ArrayList<Track> readData(String filePath) throws IOException {
		
		File file =new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis,"UTF-8"); //指定以UTF-8编码读入
		BufferedReader br = new BufferedReader(isr);
		
		ArrayList<Track> tracks=new ArrayList<Track>();
		Track trackTemp;
		TrackPoint trackPointTemp;
		
		String line = null;
		int lineNumber=0;
		while ((line=br.readLine())!= null) {
			lineNumber++;
//			System.out.println(line);
			if(lineNumber!=1)
			{
				trackTemp=createTrack(line,lineNumber);
				System.out.println(lineNumber);
				trackPointTemp=createTrackPoint(line,lineNumber);
				//判断是否数据为空
				if(trackTemp!=null && trackPointTemp!=null) {
					//判断是否为已有轨迹
					if(tracks.contains(trackTemp)) {
					//获取当前数据行应答机ID，更新已有轨迹中的应答机数据数据
					   String transponderID=trackTemp.getTransponderId();
					 //获取在数组中已有轨迹的序号
					   int index = tracks.indexOf(trackTemp);
					   trackTemp = tracks.get(index);
					   trackTemp.setTransponderId(transponderID);
					   trackTemp.addTrackpoint(trackPointTemp);
					} else
					{   
						trackTemp.addTrackpoint(trackPointTemp);
						tracks.add(trackTemp);	
					}
				}
	            
			}
			
		}
		br.close(); 
//		System.out.println(lineNumber);
		return tracks;
    }
 
	
	public static Track createTrack(String str,int lineNumber) {
		String[] data=str.split("\\s+");
		Track track=new Track();
		
		if(data.length==11) {
		  track.setAircraftAddress(data[2]);
		  track.setFlightNumber(data[8]);
		  track.setTransponderId(data[10]);
		return track;
		}else
		{
//		  System.out.println("第"+lineNumber+"行数据缺失，创建航迹失败"+str);
		  return null;
		}

	}
	
	public static TrackPoint createTrackPoint(String str,int lineNumber) {
		String[] data=str.split("\\s+");
		TrackPoint trackPoint=new TrackPoint();
		
		if(data.length==11) {
			trackPoint.setDate(data[0]+" "+data[1]);
			trackPoint.setLatitude(Double.parseDouble(data[3]));
			trackPoint.setLongitude(Double.parseDouble(data[4]));
			trackPoint.setAltitude(Double.parseDouble(data[5]));
			trackPoint.setGroundSpeed(Double.parseDouble(data[6]));
			trackPoint.setHeading(Double.parseDouble(data[7]));
			trackPoint.setClimbRate(Double.parseDouble(data[9]));
			return trackPoint;
		}else
		{
//			System.out.println("第"+lineNumber+"行数据缺失，创建航迹点失败");
			return null;
		}
		
	}
	public static void main(String[] args) throws IOException {
		ArrayList<Track> tracks=readData("track/Track_1_taxi_780342.txt");
		System.out.println(tracks.size());
		for(int i=0;i<tracks.size();i++) {
			System.out.println(tracks.get(i).toString());
		}
		
//		String[] data="2017-10-10 13:29:42 780342 38.13153862953186 115.59539794921875 9067.80 373.54 203.74 1018.75 0021".split(" ");
	}

}


