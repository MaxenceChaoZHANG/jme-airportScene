package pathSmooth;

import java.io.IOException;
import java.util.ArrayList;

import dataEntity.Track;
import dataEntity.TrackPoint;


public class FiveSmoothUtil {
	
	
	/**
	 * 取点进行-五点平滑法
	 */
	public static ArrayList<TrackPoint> smooothLatLon(ArrayList<TrackPoint> trackPoints) {
		
		int totalpoints=trackPoints.size();
		for(int i=2;i<totalpoints-2-1;i++) {
			double pi_2,pi_1,pi1,pi2;
			double lon,lat;
		  
		  //获取前后各2个点的经度，进行5点光滑
		  pi_2=trackPoints.get(i-2).getLongitude();
		  pi_1=trackPoints.get(i-1).getLongitude();
		  pi1=trackPoints.get(i+1).getLongitude();
		  pi2=trackPoints.get(i+2).getLongitude();
		  lon=Mean5_3(new double[] {pi_2,pi_1,pi1,pi2});
		  lon=(lon+trackPoints.get(i).getLongitude())/2.0f;
		  System.out.println("原始经度 "+trackPoints.get(i).getLongitude()+" 现在经度 "+lon);
		  trackPoints.get(i).setLongitude(lon);
		  
		  //获取前后各2个点的纬度，进行5点光滑
		  pi_2=trackPoints.get(i-2).getLatitude();
		  pi_1=trackPoints.get(i-1).getLatitude();
		  pi1=trackPoints.get(i+1).getLatitude();
		  pi2=trackPoints.get(i+2).getLatitude();
		  lat=Mean5_3(new double[] {pi_2,pi_1,pi1,pi2});	
		  lat=(lat+trackPoints.get(i).getLatitude())/2.0f;
		  System.out.println("原始纬度 "+trackPoints.get(i).getLatitude()+" 现在纬度 "+lat);
		  trackPoints.get(i).setLatitude(lat);	
		}
		
		return trackPoints;
		
	}
	
	/**
	 * 五点平滑法--以前后各两个点对中间点进行平滑处理
	 */
	public static double Mean5_3(double[] p) {
	    //p(u)=au^3+bu^2+cu+d
		double a,b,c,d,result;
        d=p[0];
        a=16.0f/3.0f*p[3]-32.0f/3.0f*(p[2]-p[1])-16.0f/3.0f*p[0];
        c=(24.0f*p[3]-64.0f*p[2]+192.0f*p[1]-152.0f*p[0])/24.0f;
        b=p[3]-a-d-c;
        result=a/8.0f+b/4.0f+c/2.0f+d;
//        System.out.println("A值："+a+" "+"B值："+b+" "+"C值："+c+" "+"D值："+d);
	    return result;
	}
	
	
	/**
	 * 主函数测试-五点平滑法-
	 */
	public static void main(String[] args) {
		//测试MEAN5-3方法
//		double[] p= {1.0,4.0,40.0,85.0};
//		System.out.println(Mean5_3(p));
		
		
		ArrayList<Track> tracks=new ArrayList<Track>();
		try {
		    tracks=ReadData.readData("C:\\Users\\HP\\Desktop\\工作报告\\Track_de_1.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		for(int i=0;i<tracks.size();i++) {
			
			ArrayList<TrackPoint> trackPoints =smooothLatLon(tracks.get(i).getTrackPoints());
			tracks.get(i).setTrackPoints(trackPoints);
		}
		for(int i=0;i<tracks.size();i++) {
			System.out.println(tracks.get(i));	
		}

	}
}
