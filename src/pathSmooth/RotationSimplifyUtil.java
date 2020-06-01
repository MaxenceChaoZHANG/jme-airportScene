package pathSmooth;

import java.io.IOException;
import java.util.ArrayList;

import dataEntity.Track;
import dataEntity.TrackPoint;

public class RotationSimplifyUtil {
	
	

	/**
	 * ȡ�����-�Ƕȼ�
	 */
	public static ArrayList<TrackPoint> RotationSimplify(ArrayList<TrackPoint> trackPoints,double angleToleration ) {
		
		if(trackPoints.size() ==0) {
			  System.out.println("RotationSimplify:����·��������Ϊ��");
			  return null;
			  }
		
//		float angleToleration=angleToleration;
		int totalpoints=trackPoints.size();
		ArrayList<TrackPoint> result=new ArrayList<TrackPoint>();
			
		result.add(trackPoints.get(0));
		for(int i=0;i<totalpoints-1;i++) {
			
			double heading1,heading2;	 	  
		  //��ȡǰ���2�����γ�ȣ�����5��⻬
		  heading1=trackPoints.get(i).getHeading();
		  heading2=trackPoints.get(i+1).getHeading();
		  
		  if(Math.abs(heading1-heading2)<angleToleration)
          {continue;}
		  else {
		    result.add(trackPoints.get(i+1));
          }

		}		
		return result;	
	}
	
	
	/**
	 * ����������-�Ƕȼ�
	 */
	public static void main(String[] args) {
	
		ArrayList<Track> tracks=new ArrayList<Track>();
		try {
		    tracks=ReadData.readData("C:\\Users\\HP\\Desktop\\��������\\Track_de_1.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		for(int i=0;i<tracks.size();i++) {
			
			ArrayList<TrackPoint> trackPoints =RotationSimplify(tracks.get(i).getTrackPoints(),0);
			tracks.get(i).setTrackPoints(trackPoints);
		}
		for(int i=0;i<tracks.size();i++) {
			System.out.println(tracks.get(i));	
		}

	}
}
