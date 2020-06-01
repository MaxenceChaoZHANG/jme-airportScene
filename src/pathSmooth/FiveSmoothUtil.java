package pathSmooth;

import java.io.IOException;
import java.util.ArrayList;

import dataEntity.Track;
import dataEntity.TrackPoint;


public class FiveSmoothUtil {
	
	
	/**
	 * ȡ�����-���ƽ����
	 */
	public static ArrayList<TrackPoint> smooothLatLon(ArrayList<TrackPoint> trackPoints) {
		
		int totalpoints=trackPoints.size();
		for(int i=2;i<totalpoints-2-1;i++) {
			double pi_2,pi_1,pi1,pi2;
			double lon,lat;
		  
		  //��ȡǰ���2����ľ��ȣ�����5��⻬
		  pi_2=trackPoints.get(i-2).getLongitude();
		  pi_1=trackPoints.get(i-1).getLongitude();
		  pi1=trackPoints.get(i+1).getLongitude();
		  pi2=trackPoints.get(i+2).getLongitude();
		  lon=Mean5_3(new double[] {pi_2,pi_1,pi1,pi2});
		  lon=(lon+trackPoints.get(i).getLongitude())/2.0f;
		  System.out.println("ԭʼ���� "+trackPoints.get(i).getLongitude()+" ���ھ��� "+lon);
		  trackPoints.get(i).setLongitude(lon);
		  
		  //��ȡǰ���2�����γ�ȣ�����5��⻬
		  pi_2=trackPoints.get(i-2).getLatitude();
		  pi_1=trackPoints.get(i-1).getLatitude();
		  pi1=trackPoints.get(i+1).getLatitude();
		  pi2=trackPoints.get(i+2).getLatitude();
		  lat=Mean5_3(new double[] {pi_2,pi_1,pi1,pi2});	
		  lat=(lat+trackPoints.get(i).getLatitude())/2.0f;
		  System.out.println("ԭʼγ�� "+trackPoints.get(i).getLatitude()+" ����γ�� "+lat);
		  trackPoints.get(i).setLatitude(lat);	
		}
		
		return trackPoints;
		
	}
	
	/**
	 * ���ƽ����--��ǰ�����������м�����ƽ������
	 */
	public static double Mean5_3(double[] p) {
	    //p(u)=au^3+bu^2+cu+d
		double a,b,c,d,result;
        d=p[0];
        a=16.0f/3.0f*p[3]-32.0f/3.0f*(p[2]-p[1])-16.0f/3.0f*p[0];
        c=(24.0f*p[3]-64.0f*p[2]+192.0f*p[1]-152.0f*p[0])/24.0f;
        b=p[3]-a-d-c;
        result=a/8.0f+b/4.0f+c/2.0f+d;
//        System.out.println("Aֵ��"+a+" "+"Bֵ��"+b+" "+"Cֵ��"+c+" "+"Dֵ��"+d);
	    return result;
	}
	
	
	/**
	 * ����������-���ƽ����-
	 */
	public static void main(String[] args) {
		//����MEAN5-3����
//		double[] p= {1.0,4.0,40.0,85.0};
//		System.out.println(Mean5_3(p));
		
		
		ArrayList<Track> tracks=new ArrayList<Track>();
		try {
		    tracks=ReadData.readData("C:\\Users\\HP\\Desktop\\��������\\Track_de_1.txt");
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
