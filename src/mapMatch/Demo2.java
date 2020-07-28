package mapMatch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import dataEntity.Point;

public class Demo2 {
	/**��ά��˹�ֲ���0��ֵ��x��y���򷽲����
	 */
	static double sigma=10;
	public static double BiDimensionGauss(double x,double y) {
		return 1.0/( 2.0*Math.PI*Math.pow(sigma,2))*Math.exp(  -(   Math.pow(x, 2)+Math.pow(y, 2)  ) /  (2.0*Math.pow(sigma, 2))  );
	}
	

	/**ֱ����һ��Ĵ���
	 * @param p1,p2 ֱ������ p3 ����һ��
	 * @return ����
	 * ���ȣ���һϵ�� k�� ��ֱ�ߵ������յ�ֱ�ΪA��x1�� y1����B��x2�� y2����ֱ����һ��ΪC��x0�� y0��������ΪD������k = |AD| / |AB��
	 * ��k * AB = AD = AC + CD���� AB * CD= 0�����ԣ�k * AB* AB = AC *AB���� k =AC * AB / ��AB * AB����
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
	
	/**�Ը���λ��ΪԲ�ģ��뾶10���ڣ�����һ������㣨ģ�ⱨ��λ�ã�
	 * @param Բ��
	 * @return �����
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
	
	/**�Ը����ٶȣ����x%����������ٶȣ�ģ���ٶȣ�
	 * @param ��ʵ�ٶȣ����ٷֱ�
	 * @return �����
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
		
		// ���廭ͼ������ɫ
		Plot2DPanel plot = new Plot2DPanel();
		Color red = new Color(255, 0, 0);
		Color black = new Color(0, 0, 0);
		Color blue = new Color(0, 0, 255);
		Color green = new Color(0, 255, 0);

		
		//�����ٶ�37km/h��Լ20�ڣ������ļ��ʱ��2s��λ�ü��ԼΪ20m
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
		//������ʵλ�ã���ɫ��
		double[] point1 ;
		double[] point2 ;
		for(int i=0;i<realPoints.size();i++) {
			point1 = new double[] { realPoints.get(i).x };
			point2 = new double[] { realPoints.get(i).y };
			plot.addScatterPlot("", black,point1, point2);
		}
		
		//���ɱ���λ�õ�
		ArrayList<Point> reportPoints=new ArrayList<Point>();
		for(int i=0;i<realPoints.size();i++) {
			reportPoints.add(getRandomPoint(realPoints.get(i)));
		}
		//���Ʊ���λ�ã���ɫ��
		for(int i=0;i<reportPoints.size();i++) {
			point1 = new double[] { reportPoints.get(i).x };
			point2 = new double[] { reportPoints.get(i).y };
			plot.addScatterPlot("", blue,point1, point2);
		}
		
		//�����ͼƥ���(����λ�õ�)
		ArrayList<Point> mapMatchPoints=new ArrayList<Point>();
		for(int i=0;i<reportPoints.size();i++) {
			Point point = reportPoints.get(i);
			Point mapMatch=new Point(point.x,0);
			mapMatchPoints.add(mapMatch);
		}
		//���Ƶ�ͼƥ���λ�ã���ɫ��
		for(int i=0;i<mapMatchPoints.size();i++) {
			point1 = new double[] { mapMatchPoints.get(i).x };
			point2 = new double[] { mapMatchPoints.get(i).y };
			plot.addScatterPlot("", green,point1, point2);
		}
		
		//�����ͼƥ���֮����ܾ���s1
		double s1=0;
		Point p1=mapMatchPoints.get(0);
		Point p2=mapMatchPoints.get(mapMatchPoints.size()-1);
		s1=p2.x-p1.x;
		System.out.println(s1);
		
		//���ɱ����ٶ�
		ArrayList<Double> velocity=new ArrayList<Double>();
		for(int i=0;i<reportPoints.size();i++) {
			velocity.add(getRandomVelocity(10,0.06));
		}
		System.out.println(velocity);
		//���㱨��λ�õ�֮����ܾ���s2
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
		//��������
		double deltaS=s2-s1;
		System.out.println(deltaS);
		
		//���Ǳ���λ�õ�֮��ľ��벻�䣬���ƶ�λ��
		ArrayList<Point> resultPoints=new ArrayList<Point>();
		double maxProbability=0;
		
		ArrayList<Point> tempPoints=new ArrayList<Point>();
		
		int N=100;//��������
		double divide=deltaS/N;
		
		for(int i=0;i<=N;i++) {
			//����
			tempPoints.add(new Point(mapMatchPoints.get(0).x+divide*i,0));
			for(int n=1;n<mapMatchPoints.size();n++) {
				tempPoints.add(new Point(tempPoints.get(n-1).x+segments.get(n-1),0));			
			}
			//�������Ƹ���
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
			//���ñ���
			tempPoints.clear();
			tempPro=1;
			
			//����
			tempPoints.add(new Point(mapMatchPoints.get(0).x-divide*i,0));
			for(int n=1;n<mapMatchPoints.size();n++) {
				tempPoints.add(new Point(tempPoints.get(n-1).x+segments.get(n-1),0));			
			}
			//�������Ƹ���
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
			
			//���ñ���
			tempPoints.clear();

		}
		System.out.println("max"+maxProbability);
		//���Ƶ�ͼƥ���λ�ã��tɫ��
		for(int i=0;i<resultPoints.size();i++) {
			point1 = new double[] { resultPoints.get(i).x };
			point2 = new double[] { resultPoints.get(i).y };
			plot.addScatterPlot("", red,point1, point2);
		}		
		
		
		
		
		// ��������
		plot.setAxisLabel(0, "X");
		plot.setAxisLabel(1, "Y");
		plot.setSize(1200, 1200);
		// set the boundary of the plot panel
	    double boundMin1[]  = {-10, -60};
	    double boundMax1[]  = {110,60};
	    plot.setFixedBounds(boundMin1, boundMax1);	
		// ���ڶ��弰����
		JFrame frame = new JFrame("A plot test");
		frame.setSize(1200, 1200);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}

}
