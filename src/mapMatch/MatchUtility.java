package mapMatch;

import java.util.Random;

import javax.vecmath.Vector2d;

import com.jme3.math.FastMath;

import dataEntity.*;

public class MatchUtility {
	
	public static void main(String[] args) {
		//测试本类中的方法

//	     Point p1=new Point(39.128599,117.367862);
//	     Point p2=new Point( 39.128692,117.367819);
//	     Point p3=new Point( 39.128292,117.368943);
//	    System.out.println(getPerpendicularDistance(p1,p2,p3));
//	    System.out.println(getFoot(p1,p2,p3));
	     
//	     Point p1=new Point(0.0,0.0);
//	     Point p2=new Point(-1,-1);
//	     System.out.println(getDeltaTheta(p1, p2,45));
		Random random = new Random();
		for(int i=0;i<100;i++) {
	     System.out.println(getGauss(random.nextGaussian()*10));
	     }
		
	}
	
	/**GPS定位误差--正太分布
	 * 
	 */
	public static double sigma=10;
	public static double getGauss(double d) {
		return 1.0/( Math.sqrt(2.0*Math.PI)*sigma)*Math.exp(  -Math.pow(d, 2) /  (2.0*Math.pow(sigma, 2))  );
	}
	
	
	
	/**路径段的朝向(类似飞机的朝向heading)
	 * @param r1,r2 路网点
	 * @return 路径段的朝向
	 * 
	 */
	public static double getDeltaTheta(RoadPoint r1,RoadPoint r2,double heading) {
		
		Point p1=new Point(r1.getLongitude(),r1.getLatitude());
        Point p2=new Point(r2.getLongitude(),r2.getLatitude());
        
        return getDeltaTheta(p1,p2,heading);
		
	}
	/**路径段与飞机heading的夹角（即两直线的夹角
	 * @param r1,r2 路网点
	 * @return 路径段的朝向
	 * 
	 */
	public static double getDeltaTheta(Point r1,Point r2,double heading) {
		
		Vector2d roadVector = new Vector2d(r2.x-r1.x, r2.y-r1.y);
    	Vector2d headingVector  = new Vector2d(  Math.sin(Math.toRadians(heading))  ,  Math.cos(Math.toRadians(heading))  );
    	double angle =   roadVector.angle(headingVector) ;//angle方法返回弧度，范围0~pi    
    	
    	if(angle>Math.PI/2.0)
    	 {angle=Math.PI-angle;} 	
    	return angle;
		
	}	
	
	
	
	
	
	
	/**直线外一点的垂足
	 * @param r1,r2 路网点 t 轨迹点
	 * @return 垂足（匹配点）
	 */
	public static TrackPoint getFoot(RoadPoint r1,RoadPoint r2,TrackPoint t){
		
        Point p1=new Point(r1.getLongitude(),r1.getLatitude());
        Point p2=new Point(r2.getLongitude(),r2.getLatitude());
        Point p3=new Point(t.getLongitude(),t.getLatitude());
        
        Point foot=getFoot(p1,p2,p3);
        t.setLongitude(foot.x);
        t.setLatitude(foot.y);
        
        return t;
	}
	public static Point getFootNoChange(RoadPoint r1,RoadPoint r2,TrackPoint t){
		
        Point p1=new Point(r1.getLongitude(),r1.getLatitude());
        Point p2=new Point(r2.getLongitude(),r2.getLatitude());
        Point p3=new Point(t.getLongitude(),t.getLatitude());
        
        Point foot=getFoot(p1,p2,p3);
        
        return foot;
	}
	/**直线外一点的垂足
	 * @param p1,p2 直线两点 p3 线外一点
	 * @return 垂足
	 * 首先，求一系数 k： 设直线的起点和终点分别为A（x1， y1）、B（x2， y2），直线外一点为C（x0， y0），垂足为D；并设k = |AD| / |AB。
         则，k * AB = AD = AC + CD，又 AB * CD= 0；所以，k * AB* AB = AC *AB，故 k =AC * AB / （AB * AB）。
	 */
	public static Point getFoot(Point p1,Point p2,Point p3){
        Point foot=new Point();
        
        double dx=p1.x-p2.x;
        double dy=p1.y-p2.y;
        
        double u=(p3.x-p2.x)*dx+(p3.y-p2.y)*dy;
        u/=dx*dx+dy*dy;
        
        foot.x=p2.x+u*dx;
        foot.y=p2.y+u*dy;
        
        return foot;
	}
	
	
	
	
	
	/**点到直线距离
	 * @param r1,r2 路网点,t 轨迹点
	 * @return 垂足（匹配点）
	 */
	public static double getPerpendicularDistance(RoadPoint r1,RoadPoint r2,TrackPoint t){
		
        Point p1=new Point(r1.getLongitude(),r1.getLatitude());
        Point p2=new Point(r2.getLongitude(),r2.getLatitude());
        Point p3=new Point(t.getLongitude(),t.getLatitude());
        
        return getPerpendicularDistance(p1,p2,p3);
	}
	/**点到直线的距离
	 * @param p1,p2 直线两点 p3 线外一点
	 * @return 垂足
	 *  设直线的起点和终点分别为A（x1， y1）、B（x2， y2），直线外一点为C（x0， y0），垂足为D；
	 *  得到垂足后计算点到直线的距离
	 */
	public static double getPerpendicularDistance(Point p1,Point p2,Point p3){
        Point foot=new Point();        
        double dx=p1.x-p2.x;
        double dy=p1.y-p2.y;   
        
        double u=(p3.x-p2.x)*dx+(p3.y-p2.y)*dy;
        u/=dx*dx+dy*dy; 
        
        foot.x=p2.x+u*dx;
        foot.y=p2.y+u*dy; 
        double result=Math.sqrt( Math.pow(foot.x-p3.x, 2) + Math.pow(foot.y-p3.y, 2) );
        return result;
	}
	
	
	
	
	
	/**点到线段的距离
	 * @param r1,r2 路网点,t 轨迹点
	 * @return 点到线段的距离
	 * 
	 */
	public static double getPointToSegDistance(RoadPoint r1,RoadPoint r2,TrackPoint t) {
		Point p1=new Point(r1.getLongitude(),r1.getLatitude());
        Point p2=new Point(r2.getLongitude(),r2.getLatitude());
        Point p3=new Point(t.getLongitude(),t.getLatitude());
        return getPointToSegDistance(p1,p2,p3);
		
	}
	/**点到线段的距离
	 * @param p1,p2 线段端点,p3
	 * @return 点到线段的距离
	 */
	public static double getPointToSegDistance(Point p1,Point p2,Point p3){
        Point foot=new Point();
        double dx=p1.x-p2.x;
        double dy=p1.y-p2.y;
        
        double u=(p3.x-p2.x)*dx+(p3.y-p2.y)*dy;
        u/=dx*dx+dy*dy;
        
        foot.x=p2.x+u*dx;
        foot.y=p2.y+u*dy;
        
        if(u<0) {
          return Math.sqrt( Math.pow(p2.x-p3.x, 2) + Math.pow(p2.y-p3.y, 2) );
        }
        if(u>1) {
          return Math.sqrt( Math.pow(p1.x-p3.x, 2) + Math.pow(p1.y-p3.y, 2) );
        }

        return Math.sqrt( Math.pow(foot.x-p3.x, 2) + Math.pow(foot.y-p3.y, 2) );
	}


}



