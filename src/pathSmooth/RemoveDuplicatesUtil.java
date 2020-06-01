package pathSmooth;

import java.io.IOException;
import java.util.ArrayList;

import dataEntity.Track;
import dataEntity.TrackPoint;

public class RemoveDuplicatesUtil {
	
	public static ArrayList<TrackPoint> RemoveDuplicates(ArrayList<TrackPoint> trackPoints){
		 
		double lon1,lon2; 
		double lat1,lat2;

		for(int i=0;i<trackPoints.size()-2;i++) {

			  //取出判断点前后的经纬度
			  lon1=trackPoints.get(i).getLongitude();
			  lon2=trackPoints.get(i+1).getLongitude();
			  lat1=trackPoints.get(i).getLatitude();
			  lat2=trackPoints.get(i+1).getLatitude();
			  //取出判断点前后航向
	          if(  (lon1==lon2)  &&  (lat1==lat2)  )
	          {trackPoints.remove(i);
        	  i--;}

		 }
			
			return trackPoints;

	}
	
    public static void main(String[] args) {

    	
		ArrayList<Track> tracks=new ArrayList<Track>();
		try {
		    tracks=ReadData.readData("C:\\Users\\HP\\Desktop\\工作报告\\Track_de_1.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		for(int i=0;i<tracks.size();i++) {
			System.out.println(tracks.get(i));	
			ArrayList<TrackPoint> trackPoints =RemoveDuplicates(tracks.get(i).getTrackPoints());
//			tracks.get(i).setTrackPoints(trackPoints);
		}
		for(int i=0;i<tracks.size();i++) {
			System.out.println("-----------------");
			System.out.println(tracks.get(i));	
		}
    	
//    	float lon=117.350099184f;
//    	float lat=39.129502365f;
//    	double lon=117.350099184; 
//    	double lat=39.129502365;
//    	System.out.println(lon);
//    	System.out.println(lat);

	}

}
