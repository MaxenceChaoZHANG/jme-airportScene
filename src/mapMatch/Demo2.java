package mapMatch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import dataEntity.Point;

public class Demo2 {
	/**二维高斯分布，0均值，x，y方向方差相等
	 */
	static double sigma=10;
	public static double BiDimensionGauss(double x,double y) {
		return 1.0/( 2.0*Math.PI*Math.pow(sigma,2))*Math.exp(  -(   Math.pow(x, 2)+Math.pow(y, 2)  ) /  (2.0*Math.pow(sigma, 2))  );
	}
	

	/**直线外一点的垂足
	 * @param p1,p2 直线两点 p3 线外一点
	 * @return 垂足
	 * 首先，求一系数 k： 设直线的起点和终点分别为A（x1， y1）、B（x2， y2），直线外一点为C（x0， y0），垂足为D；并设k = |AD| / |AB。
	 * 则，k * AB = AD = AC + CD，又 AB * CD= 0；所以，k * AB* AB = AC *AB，故 k =AC * AB / （AB * AB）。
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
	
	/**以给定位置为圆心，半径10米内，生成一个随机点（模拟报文位置）
	 * @param 圆心
	 * @return 随机点
	 */
	public static Point getRandomPoint(Point p1){
		Random random = new Random();
		double a,b;		
		a=random.nextGaussian()*10;
		b=random.nextGaussian()*10;
		while(Math.abs(a)>10||Math.abs(b)>10) {
			a=random.nextGaussian()*10;
			b=random.nextGaussian()*10;
		}
		Point point = new Point( a+p1.x, b+p1.y);
//		System.out.println(point);
	    return point;
	}
	
	/**以给定速度，误差x%内生成随机速度（模拟速度）
	 * @param 真实速度，误差百分比
	 * @return 随机点
	 */
	public static double getRandomVelocity(double v,double x){
		Random random = new Random();
		double a;		
		a=random.nextGaussian()*v*x;
		
		while(Math.abs(a)>v*x) {
			a=random.nextGaussian()*v*x;
		}
//		System.out.println(a);
	    return a+v;
	}
	
	
	public static void main(String[] args) {
		
		// 定义画图面板和颜色
		Plot2DPanel plot = new Plot2DPanel();
		Color red = new Color(255, 0, 0);
		Color black = new Color(0, 0, 0);
		Color blue = new Color(0, 0, 255);
		Color green = new Color(0, 255, 0);

		
		//考虑速度37km/h（约20节），报文间隔时间2s，位置间隔约为20m
		ArrayList<Point> realPoints=new ArrayList<Point>();
		realPoints.add(new Point(0,0));
		realPoints.add(new Point(10,0));
		realPoints.add(new Point(20,0));
		realPoints.add(new Point(30,0));
		realPoints.add(new Point(40,0));
		realPoints.add(new Point(50,0));
		realPoints.add(new Point(60,0));
		realPoints.add(new Point(70,0));
		realPoints.add(new Point(80,0));		
		realPoints.add(new Point(90,0));
		realPoints.add(new Point(100,0));
		//绘制真实位置（黑色）
		double[] point1 ;
		double[] point2 ;
		for(int i=0;i<realPoints.size();i++) {
			point1 = new double[] { realPoints.get(i).x };
			point2 = new double[] { realPoints.get(i).y };
			plot.addScatterPlot("", black,point1, point2);
		}
		
		//生成报文位置点
		ArrayList<Point> reportPoints=new ArrayList<Point>();
		for(int i=0;i<realPoints.size();i++) {
			reportPoints.add(getRandomPoint(realPoints.get(i)));
		}
		//绘制报文位置（蓝色）
		for(int i=0;i<reportPoints.size();i++) {
			point1 = new double[] { reportPoints.get(i).x };
			point2 = new double[] { reportPoints.get(i).y };
			plot.addScatterPlot("", blue,point1, point2);
		}
		
		//计算地图匹配点(报文位置点)
		ArrayList<Point> mapMatchPoints=new ArrayList<Point>();
		for(int i=0;i<reportPoints.size();i++) {
			Point point = reportPoints.get(i);
			Point mapMatch=new Point(point.x,0);
			mapMatchPoints.add(mapMatch);
		}
		//绘制地图匹配点位置（绿色）
		for(int i=0;i<mapMatchPoints.size();i++) {
			point1 = new double[] { mapMatchPoints.get(i).x };
			point2 = new double[] { mapMatchPoints.get(i).y };
			plot.addScatterPlot("", green,point1, point2);
		}
		
		//计算地图匹配点之间的总距离s1
		double s1=0;
		Point p1=mapMatchPoints.get(0);
		Point p2=mapMatchPoints.get(mapMatchPoints.size()-1);
		s1=p2.x-p1.x;
		System.out.println(s1);
		
		//生成报文速度
		ArrayList<Double> velocity=new ArrayList<Double>();
		for(int i=0;i<reportPoints.size();i++) {
			velocity.add(getRandomVelocity(10,0.06));
		}
		System.out.println(velocity);
		//计算报文位置点之间的总距离s2
		double s2=0;
		double temp=0;
		double v1,v2;
		ArrayList<Double> segments=new ArrayList<Double>();
		for(int i=0;i<velocity.size()-1;i++) {
			 v1 = velocity.get(i);
			 v2 = velocity.get(i+1);
			 temp=(v1+v2)/2.0*1;
			 segments.add(temp);
			 s2=s2+temp;
		}
		System.out.println(s2);
		//计算距离差
		double deltaS=s2-s1;
		System.out.println(deltaS);
		
		//考虑报文位置点之间的距离不变，仅移动位置
		ArrayList<Point> resultPoints=new ArrayList<Point>();
		double maxProbability=0;
		
		ArrayList<Point> tempPoints=new ArrayList<Point>();
		
		int N=100;//迭代次数
		double divide=deltaS/N;
		
		for(int i=0;i<=N;i++) {
			//右移
			tempPoints.add(new Point(mapMatchPoints.get(0).x+divide*i,0));
			for(int n=1;n<mapMatchPoints.size();n++) {
				tempPoints.add(new Point(tempPoints.get(n-1).x+segments.get(n-1),0));			
			}
			//计算右移概率
			double tempPro=1;
			for(int m=0;m<tempPoints.size();m++) {
				p1=tempPoints.get(m);
				p2=reportPoints.get(m);
				tempPro=tempPro*BiDimensionGauss(p1.x-p2.x,p1.y-p2.y);

			}
			if(tempPro>maxProbability) {
				maxProbability=tempPro;
				resultPoints.clear();
				resultPoints.addAll(tempPoints);
//				System.out.println(maxProbability);

			}
			//重置变量
			tempPoints.clear();
			tempPro=1;
			
			//左移
			tempPoints.add(new Point(mapMatchPoints.get(0).x-divide*i,0));
			for(int n=1;n<mapMatchPoints.size();n++) {
				tempPoints.add(new Point(tempPoints.get(n-1).x+segments.get(n-1),0));			
			}
			//计算左移概率
			for(int m=0;m<tempPoints.size();m++) {
				p1=tempPoints.get(m);
				p2=reportPoints.get(m);
				tempPro=tempPro*BiDimensionGauss(p1.x-p2.x,p1.y-p2.y);
				
			}
			if(tempPro>maxProbability) {
				maxProbability=tempPro;
				resultPoints.clear();
				resultPoints.addAll(tempPoints);
//				System.out.println(maxProbability);

			}
			
			//重置变量
			tempPoints.clear();

		}
		System.out.println("max"+maxProbability);
		//绘制地图匹配点位置（紅色）
		for(int i=0;i<resultPoints.size();i++) {
			point1 = new double[] { resultPoints.get(i).x };
			point2 = new double[] { resultPoints.get(i).y };
			plot.addScatterPlot("", red,point1, point2);
		}		
		
		
		
		
		// 画板设置
		plot.setAxisLabel(0, "X");
		plot.setAxisLabel(1, "Y");
		plot.setSize(1200, 1200);
		// set the boundary of the plot panel
	    double boundMin1[]  = {-10, -60};
	    double boundMax1[]  = {110,60};
	    plot.setFixedBounds(boundMin1, boundMax1);	
		// 窗口定义及设置
		JFrame frame = new JFrame("A plot test");
		frame.setSize(1200, 1200);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}

}
