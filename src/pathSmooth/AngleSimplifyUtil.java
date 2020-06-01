package pathSmooth;

import java.io.IOException;
import java.util.ArrayList;
import javax.vecmath.Vector2d;
import com.jme3.math.FastMath;

import dataEntity.TrackPoint;



	public class AngleSimplifyUtil {
		
	     /**
	      * ȡ���������㣬�жϼ����heading�뱨���е�heading֮���Ƿ񳬹�������ֵ
	      * �ﵽƽ���켣�����ٹ켣���Ŀ��
	      */
	     public static ArrayList<TrackPoint> smooothLatLon(ArrayList<TrackPoint> trackPoints) {
	    	 
	    	 if(trackPoints.size() ==0) {
	   		  System.out.println("RotationSimplify:����·��������Ϊ��");
	   		  return null;
	   		  }

			float angleToleration=10;
			
			
			for(int i=1;i<trackPoints.size()-1;i++) {
				
			  double lon1,lon2,lat1,lat2,lon,lat; 
			  double direction1,direction2;
			  double heading1,heading2;
			  //ȡ���жϵ㾭γ��
			  lon=trackPoints.get(i).getLongitude();
			  lat=trackPoints.get(i).getLatitude();
			  //ȡ���жϵ�ǰ��ľ�γ��
			  lon1=trackPoints.get(i-1).getLongitude();
			  lon2=trackPoints.get(i+1).getLongitude();
			  lat1=trackPoints.get(i-1).getLatitude();
			  lat2=trackPoints.get(i+1).getLatitude();
			  //ȡ���жϵ�ǰ����
			  heading1=trackPoints.get(i-1).getHeading();
			  heading2=trackPoints.get(i+1).getHeading();
	          
	          direction1=direction(lon1,lat1,lon,lat);
	          direction2=direction(lon,lat,lon2,lat2);
	          
	          System.out.print("headng1:"+heading1+" direction1:"+direction1);
	          System.out.println("headng2:"+heading2+" direction2:"+direction2);

	          if(  (Math.abs(direction1-heading1)<=angleToleration)  &&  (Math.abs(direction2-heading2)<=angleToleration)  )
	          {continue;}
	          else {
	        	  trackPoints.remove(i);
	        	  i--;
	          }
			}
			
			return trackPoints;
			
		}
	     /**
	      * ȡ���������㣬�жϼ����heading�뱨���е�heading֮���Ƿ񳬹�������ֵ
	      * �ﵽƽ���켣�����ٹ켣���Ŀ��
	      */
	     public static ArrayList<TrackPoint> smooothByHeading(ArrayList<TrackPoint> trackPoints) {

	    	 double angleToleration=20;
			
			for(int i=0;i<trackPoints.size()-1;i++) {
				double lon1,lat1,lon,lat; 
				double direction1 = 0;
				double heading1 = 0;

				  //ȡ���жϵ㾭γ��
				  lon=trackPoints.get(i).getLongitude();
				  lat=trackPoints.get(i).getLatitude();
				  //ȡ���жϵ��ľ�γ��
				  lon1=trackPoints.get(i+1).getLongitude();
				  lat1=trackPoints.get(i+1).getLatitude();
				  //ȡ���жϵ����
				  heading1=trackPoints.get(i).getHeading();
				  direction1=direction(lon,lat,lon1,lat1);


	          if(Math.abs(direction1-heading1)<=angleToleration)
	          {continue;}
	          else {
	        	  trackPoints.remove(i+1);
	        	  i--;
	          }
			}
			return trackPoints;
			
		}
		
	     /**
	      * ȡ���������㣬�ж��������ļн��Ƿ񳬹�������ֵ
	      * �ﵽƽ���켣�����ٹ켣���Ŀ��
	      */
	     public static ArrayList<TrackPoint> DeleteErrorPoint(ArrayList<TrackPoint> trackPoints,double angleToleration) {
	    	 
	    	 if(trackPoints.size() ==0) {
		   		  System.out.println("DeleteErrorPoint:����·��������Ϊ��");
		   		  return null;
		   		  }
	    	 

//	    	ArrayList<TrackPoint> result=new ArrayList<TrackPoint>();
//	    	result.add(trackPoints.get(0));
//	    	System.out.println(trackPoints);
//	    	System.out.println("------------");
			for(int i=1;i<trackPoints.size()-1;i++) {
				double lon1,lat1,lon,lat,lon2,lat2; 
				double direction1 = 0,direction2=0;

				  //ȡ���жϵ㾭γ��
				  lon=trackPoints.get(i).getLongitude();
				  lat=trackPoints.get(i).getLatitude();
				  //ȡ���жϵ��ľ�γ��
				  lon1=trackPoints.get(i+1).getLongitude();
				  lat1=trackPoints.get(i+1).getLatitude();
				 //ȡ���жϵ�ǰ�ľ�γ��
				  lon2=trackPoints.get(i-1).getLongitude();
				  lat2=trackPoints.get(i-1).getLatitude();
				  //ȡ���жϵ�ǰ�������ͺ�������
				  direction1=direction(lon2,lat2,lon,lat);
				  direction2=direction(lon,lat,lon1,lat1);

	          if(Math.abs(direction1-direction2)<=angleToleration)
	          {continue;}
//	        	  result.add(trackPoints.get(i));
	          else {
//	        	  System.out.println("ǰһ�㣺"+lon2+" "+lat2);
//	        	  System.out.println("�м�㣺"+lon+" "+lat);
//	        	  System.out.println("��һ�㣺"+lon1+" "+lat1);
//	        	  System.out.println(i+" "+direction1+" "+direction2+" " +Math.abs(direction1-direction2));
	        	  trackPoints.remove(i);
	        	  i--;
	          }

			}
			
//			result.add(trackPoints.get(trackPoints.size()-1));
			
			return trackPoints;
			
		}

	     /**
	      * ��������γ��֮��ľ���
	      */
	      public static double distance(double lon1,double lat1,double lon2,double lat2) {
	    	  double[] xy1=Mercator.lonLat2Mercator(lon1, lat1);
	    	  double[] xy2=Mercator.lonLat2Mercator(lon1, lat1);
	    	  double distance=Math.sqrt(Math.pow(xy1[0]-xy2[0],2)+Math.pow(xy1[1]-xy2[1],2));
				return distance;    	
		   }

		    
	      /**
		   * ��������γ���γɵ���������������ļнǣ�heading��
		   */ 
	      public static double direction(double lon1,double lat1,double lon2,double lat2) {

	        	Vector2d vector = new Vector2d(lon2-lon1, lat2-lat1);
	        	Vector2d north   = new Vector2d(0.0,1.0);
	        	double angle= (FastMath.RAD_TO_DEG*(vector.angle(north)));//angle�������ػ��ȣ���Χ0~pi
	        	double heading;
	        	
	        	if((lon2-lon1)>0)
	        	 {heading=angle;}
	        	else {
	             heading=360-angle;}
	        	
				return heading;
	        }
	        
	      /**
		   * ����������
		   */ 
	     public static void main(String[] args) {
	        	//���Ժ���Ǽ���
//				System.out.println(direction(1.0,1.0,-2.0,-2.0));
	        	
//	    		ArrayList<Track> tracks=new ArrayList<Track>();
//	    		try {
//	    		    tracks=ReadData.readData("C:\\Users\\HP\\Desktop\\��������\\Track_de_1.txt");
//	    		} catch (IOException e1) {
//	    			// TODO Auto-generated catch block
//	    			e1.printStackTrace();
//	    		} 
//	    		for(int i=0;i<tracks.size();i++) {
//	    			
//	    			ArrayList<TrackPoint> trackPoints =DeleteErrorPoint(tracks.get(i).getTrackPoints());
//	    			tracks.get(i).setTrackPoints(trackPoints);
//	    		}
//	    		for(int i=0;i<tracks.size();i++) {
//	    			System.out.println(tracks.get(i));	
//	    		}
//
	    	 double a=117.3504316806793;
	    	 System.out.println(a);
			}
	}
