package pathSmooth;

import java.io.IOException;
import java.util.ArrayList;
import javax.vecmath.Vector2d;
import com.jme3.math.FastMath;

import dataEntity.TrackPoint;



	public class AngleSimplifyUtil {
		
	     /**
	      * 取出连续三点，判断计算的heading与报文中的heading之差是否超过给定阈值
	      * 达到平滑轨迹，减少轨迹点的目的
	      */
	     public static ArrayList<TrackPoint> smooothLatLon(ArrayList<TrackPoint> trackPoints) {
	    	 
	    	 if(trackPoints.size() ==0) {
	   		  System.out.println("RotationSimplify:传入路径点数组为空");
	   		  return null;
	   		  }

			float angleToleration=10;
			
			
			for(int i=1;i<trackPoints.size()-1;i++) {
				
			  double lon1,lon2,lat1,lat2,lon,lat; 
			  double direction1,direction2;
			  double heading1,heading2;
			  //取出判断点经纬度
			  lon=trackPoints.get(i).getLongitude();
			  lat=trackPoints.get(i).getLatitude();
			  //取出判断点前后的经纬度
			  lon1=trackPoints.get(i-1).getLongitude();
			  lon2=trackPoints.get(i+1).getLongitude();
			  lat1=trackPoints.get(i-1).getLatitude();
			  lat2=trackPoints.get(i+1).getLatitude();
			  //取出判断点前后航向
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
	      * 取出连续两点，判断计算的heading与报文中的heading之差是否超过给定阈值
	      * 达到平滑轨迹，减少轨迹点的目的
	      */
	     public static ArrayList<TrackPoint> smooothByHeading(ArrayList<TrackPoint> trackPoints) {

	    	 double angleToleration=20;
			
			for(int i=0;i<trackPoints.size()-1;i++) {
				double lon1,lat1,lon,lat; 
				double direction1 = 0;
				double heading1 = 0;

				  //取出判断点经纬度
				  lon=trackPoints.get(i).getLongitude();
				  lat=trackPoints.get(i).getLatitude();
				  //取出判断点后的经纬度
				  lon1=trackPoints.get(i+1).getLongitude();
				  lat1=trackPoints.get(i+1).getLatitude();
				  //取出判断点后航向
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
	      * 取出连续三点，判断两向量的夹角是否超过给定阈值
	      * 达到平滑轨迹，减少轨迹点的目的
	      */
	     public static ArrayList<TrackPoint> DeleteErrorPoint(ArrayList<TrackPoint> trackPoints,double angleToleration) {
	    	 
	    	 if(trackPoints.size() ==0) {
		   		  System.out.println("DeleteErrorPoint:传入路径点数组为空");
		   		  return null;
		   		  }
	    	 

//	    	ArrayList<TrackPoint> result=new ArrayList<TrackPoint>();
//	    	result.add(trackPoints.get(0));
//	    	System.out.println(trackPoints);
//	    	System.out.println("------------");
			for(int i=1;i<trackPoints.size()-1;i++) {
				double lon1,lat1,lon,lat,lon2,lat2; 
				double direction1 = 0,direction2=0;

				  //取出判断点经纬度
				  lon=trackPoints.get(i).getLongitude();
				  lat=trackPoints.get(i).getLatitude();
				  //取出判断点后的经纬度
				  lon1=trackPoints.get(i+1).getLongitude();
				  lat1=trackPoints.get(i+1).getLatitude();
				 //取出判断点前的经纬度
				  lon2=trackPoints.get(i-1).getLongitude();
				  lat2=trackPoints.get(i-1).getLatitude();
				  //取出判断点前置向量和后置向量
				  direction1=direction(lon2,lat2,lon,lat);
				  direction2=direction(lon,lat,lon1,lat1);

	          if(Math.abs(direction1-direction2)<=angleToleration)
	          {continue;}
//	        	  result.add(trackPoints.get(i));
	          else {
//	        	  System.out.println("前一点："+lon2+" "+lat2);
//	        	  System.out.println("中间点："+lon+" "+lat);
//	        	  System.out.println("后一点："+lon1+" "+lat1);
//	        	  System.out.println(i+" "+direction1+" "+direction2+" " +Math.abs(direction1-direction2));
	        	  trackPoints.remove(i);
	        	  i--;
	          }

			}
			
//			result.add(trackPoints.get(trackPoints.size()-1));
			
			return trackPoints;
			
		}

	     /**
	      * 计算两经纬度之间的距离
	      */
	      public static double distance(double lon1,double lat1,double lon2,double lat2) {
	    	  double[] xy1=Mercator.lonLat2Mercator(lon1, lat1);
	    	  double[] xy2=Mercator.lonLat2Mercator(lon1, lat1);
	    	  double distance=Math.sqrt(Math.pow(xy1[0]-xy2[0],2)+Math.pow(xy1[1]-xy2[1],2));
				return distance;    	
		   }

		    
	      /**
		   * 计算两经纬度形成的向量与正北方向的夹角（heading）
		   */ 
	      public static double direction(double lon1,double lat1,double lon2,double lat2) {

	        	Vector2d vector = new Vector2d(lon2-lon1, lat2-lat1);
	        	Vector2d north   = new Vector2d(0.0,1.0);
	        	double angle= (FastMath.RAD_TO_DEG*(vector.angle(north)));//angle方法返回弧度，范围0~pi
	        	double heading;
	        	
	        	if((lon2-lon1)>0)
	        	 {heading=angle;}
	        	else {
	             heading=360-angle;}
	        	
				return heading;
	        }
	        
	      /**
		   * 主函数测试
		   */ 
	     public static void main(String[] args) {
	        	//测试航向角计算
//				System.out.println(direction(1.0,1.0,-2.0,-2.0));
	        	
//	    		ArrayList<Track> tracks=new ArrayList<Track>();
//	    		try {
//	    		    tracks=ReadData.readData("C:\\Users\\HP\\Desktop\\工作报告\\Track_de_1.txt");
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
