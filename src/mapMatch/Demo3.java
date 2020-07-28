
package mapMatch;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.dom4j.DocumentException;
import org.math.plot.Plot2DPanel;
import dataEntity.Point;
import dataEntity.Road;
import dataEntity.RoadEdge;
import dataEntity.RoadPoint;
import dataEntity.RoadWay;
import dataEntity.Track;
import dataEntity.TrackPoint;
import mapMatch.geoHash.MyGeoHashHelper;
import mapMatch.road.RoadXMLUtility;

public class Demo3 {

	Road road;// ��ȡ·������
	ArrayList<RoadPoint> roadPoints;
	ArrayList<RoadWay> roadWays;
	ArrayList<RoadEdge> roadEdges;
	ArrayList<Track> tracks = null;// ��ȡ�켣����
	Floyd floydUtil;

	public static void main(String[] args) {
		Demo3 demo = new Demo3();
		demo.search();;
		
//		System.out.println(demo.BiDimensionGauss(10,10));
	}

	class SearchResult {
		RoadPoint start;
		RoadPoint end;
		Point point;
	}


	public Demo3() {
		// --------------------------��ʼ������--------------------------
		// ��ȡ�켣����
		tracks = null;
		try {
			tracks = pathSmooth.ReadData.readData("track/Track_de_1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ��ȡ·������
		road = null;
		try {
			road = RoadXMLUtility.ReadXML();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		roadPoints = road.getRoadPoints();
		roadWays = road.getRoadWays();

		// ��RoadWay�г�ȡ���е�RoadEdge
		roadEdges = new ArrayList<RoadEdge>();
		int wayNumber = roadWays.size();
		for (int i = 0; i < wayNumber; i++) {
			roadEdges.addAll(roadWays.get(i).getRoadEdges());
		}
		// ��ʼ��Floyd����
		floydUtil = new Floyd(roadPoints, roadEdges);
		floydUtil.floyd();
		
	}

	public void MapMatch() {
//--------------------------���廭�����ɫ--------------------------	
		Color red = new Color(255, 0, 0);// ������ɫ
		Color black = new Color(0, 0, 0);
		Color blue = new Color(0, 0, 255);
		Color green = new Color(0, 255, 0);
		Plot2DPanel plot = new Plot2DPanel();// ���廭ͼ���
		JFrame frame = new JFrame("A plot test");

		// set the label of the plot panel
		plot.setAxisLabel(0, " longitude");
		plot.setAxisLabel(1, "latuitude");
		plot.setSize(1200, 1200);
		// create a frame
		frame = new JFrame("A plot test");
		// set the size of the frame
		frame.setSize(1200, 1200);
		// set the content of the frame as the plot panel
		frame.setContentPane(plot);
		frame.setVisible(true);

//--------------------------����·����ԭʼ�켣--------------------------	
		// ����·���ߣ�����·���ߣ���ɫ������
		int edgeNumber = roadEdges.size();
		RoadEdge tempEdge = null;
		RoadPoint roadPoint1 = null;
		RoadPoint roadPoint2 = null;
		for (int j = 0; j < edgeNumber; j++) {
			tempEdge = roadEdges.get(j);
			roadPoint1 = tempEdge.getP1();
			roadPoint2 = tempEdge.getP2();
			double[] point1 = { roadPoint1.getLongitude(), roadPoint1.getLatitude() };
			double[] point2 = { roadPoint2.getLongitude(), roadPoint2.getLatitude() };
			plot.addLinePlot("", black, point1, point2);
		}

		// ����ԭʼ�켣����ɫ������
		ArrayList<TrackPoint> trackPoints = tracks.get(0).getTrackPoints();
		for (int i = 0; i < trackPoints.size() - 1; i++) {
			TrackPoint Point1 = trackPoints.get(i);
			TrackPoint Point2 = trackPoints.get(i + 1);
			double[] point1 = { Point1.getLongitude(), Point1.getLatitude() };
			double[] point2 = { Point2.getLongitude(), Point2.getLatitude() };

			plot.addLinePlot("", blue, point1, point2);
		}
//--------------------------��ͼƥ���㷨--------------------------	
		// ����geoHashHepler����
		MyGeoHashHelper geoHash = new MyGeoHashHelper();
		List<String> geoHashCode = null;

		// ����ÿһ���켣����е�ͼƥ��
		int trackPointNumber = trackPoints.size();
		TrackPoint temp1 = null;
		for (int i = 0; i < trackPointNumber; i++) {
			// 1.��ȡ��ǰ�켣�㸽����9��geohashCode
			temp1 = trackPoints.get(i);
			geoHashCode = geoHash.aroundWith7Char(temp1.getLatitude(), temp1.getLongitude());
			// 2.�������㣬����geoHashѰ�Ҿ��뵱ǰ�켣��ȽϽ���9����
			ArrayList<RoadPoint> neighborPoints = new ArrayList<RoadPoint>();
			int roadPointNumber = roadPoints.size();
			RoadPoint temp2 = null;
			for (int k = 0; k < roadPointNumber; k++) {
				temp2 = roadPoints.get(k);

				if (temp2.getGeoHash().equals(geoHashCode.get(0))) {
					neighborPoints.add(temp2);
					continue;
				}
				if (temp2.getGeoHash().equals(geoHashCode.get(1))) {
					neighborPoints.add(temp2);
					continue;
				}
				if (temp2.getGeoHash().equals(geoHashCode.get(2))) {
					neighborPoints.add(temp2);
					continue;
				}
				if (temp2.getGeoHash().equals(geoHashCode.get(3))) {
					neighborPoints.add(temp2);
					continue;
				}
				if (temp2.getGeoHash().equals(geoHashCode.get(4))) {
					neighborPoints.add(temp2);
					continue;
				}
				if (temp2.getGeoHash().equals(geoHashCode.get(5))) {
					neighborPoints.add(temp2);
					continue;
				}
				if (temp2.getGeoHash().equals(geoHashCode.get(6))) {
					neighborPoints.add(temp2);
					continue;
				}
				if (temp2.getGeoHash().equals(geoHashCode.get(7))) {
					neighborPoints.add(temp2);
					continue;
				}
				if (temp2.getGeoHash().equals(geoHashCode.get(8))) {
					neighborPoints.add(temp2);
					continue;
				}

			} // �õ����뵱ǰ�켣��ȽϽ��ĵ�ļ�����

			// 3.���ݱȽϽ���·����Ѱ�Ҹ�����·���Σ����浽������
			ArrayList<RoadEdge> neighborEdges = new ArrayList<RoadEdge>();

			int neighborPointNumber = neighborPoints.size();
//			System.out.println(i+"��ǰƥ��·������Ŀ��"+neighborPointNumber);
			if (neighborPointNumber == 0) {
				System.out.println("ƥ��ʧ�ܣ�" + i + " " + temp1.getLongitude() + "," + temp1.getLatitude());
				continue;
			}

			RoadPoint temp3 = null;
			for (int j = 0; j < neighborPointNumber; j++) {
				temp3 = neighborPoints.get(j);

				int roadEdgeNumber = roadEdges.size();
				RoadEdge temp4 = null;
				for (int m = 0; m < roadEdgeNumber; m++) {
					temp4 = roadEdges.get(m);
					if (temp4.getId1() == temp3.getId())
						neighborEdges.add(temp4);
					if (temp4.getId2() == temp3.getId())
						neighborEdges.add(temp4);
				}

			} // �õ�������·����

			// 4.���㸽��·�ε�Ȩ�أ�Ѱ������·����
			int neighborRoadEdgeNumber = neighborEdges.size();
			double thetaFactor = 0.47;
			double distanceFactor = 0.53;
			double probability = 100000;
			RoadEdge temp5 = null;
			RoadPoint r1 = null;
			RoadPoint r2 = null;
//			System.out.println(i+"��ǰƥ��·������Ŀ��"+neighborRoadEdgeNumber);

			for (int n = 0; n < neighborRoadEdgeNumber; n++) {
				r1 = neighborEdges.get(n).getP1();
				r2 = neighborEdges.get(n).getP2();
				double distance = MatchUtility.getPointToSegDistance(r1, r2, temp1) * 111000;
				double deltaTheta = MatchUtility.getDeltaTheta(r1, r2, temp1.getHeading());
				double matchProbability = distance * distanceFactor + Math.toDegrees(deltaTheta) * thetaFactor;
				if (matchProbability < probability) {
					probability = matchProbability;
					temp5 = neighborEdges.get(n);
				}
			} // �õ���ƥ��·����

			// 5.��������·���Σ����㴹�㣬�õ�ƥ���
			// ���ƹ켣��仯���
			double[] point3 = { temp1.getLongitude(), temp1.getLatitude() };
			TrackPoint matchResult = MatchUtility.getFoot(temp5.getP1(), temp5.getP2(), temp1);
			double[] point4 = { temp1.getLongitude(), temp1.getLatitude() };
			plot.addLinePlot("", green, point3, point4);

			// �������·����
//			r1 = roadPoints.get(temp5.getIndexINlist1());
//			r2 = roadPoints.get(temp5.getIndexINlist2());		
//			double[] point1= {r1.getLongitude(),r1.getLatitude()};
//			double[] point2= {r2.getLongitude(),r2.getLatitude()};	
//			plot.addLinePlot("", red, point1, point2);	

		}
//--------------------------���Ƶ�ͼƥ���Ľ��--------------------------	
		for (int i = 0; i < trackPoints.size() - 1; i++) {
			TrackPoint Point1 = trackPoints.get(i);
			TrackPoint Point2 = trackPoints.get(i + 1);
			double[] point1 = { Point1.getLongitude(), Point1.getLatitude() };
			double[] point2 = { Point2.getLongitude(), Point2.getLatitude() };

			plot.addLinePlot("", red, point1, point2);
		}

	}

	public SearchResult MapMatchOnePoint(TrackPoint trackPoint) {

		// --------------------------��ͼƥ���㷨--------------------------
		// ����geoHashHepler����
		MyGeoHashHelper geoHash = new MyGeoHashHelper();
		List<String> geoHashCode = null;
		// 1.��ȡ��ǰ�켣�㸽����9��geohashCode
		geoHashCode = geoHash.aroundWith7Char(trackPoint.getLatitude(), trackPoint.getLongitude());
		// 2.�������㣬����geoHashѰ�Ҿ��뵱ǰ�켣��ȽϽ���9����
		ArrayList<RoadPoint> neighborPoints = new ArrayList<RoadPoint>();
		int roadPointNumber = roadPoints.size();
		RoadPoint temp2 = null;
		for (int k = 0; k < roadPointNumber; k++) {
			temp2 = roadPoints.get(k);

			if (temp2.getGeoHash().equals(geoHashCode.get(0))) {
				neighborPoints.add(temp2);
				continue;
			}
			if (temp2.getGeoHash().equals(geoHashCode.get(1))) {
				neighborPoints.add(temp2);
				continue;
			}
			if (temp2.getGeoHash().equals(geoHashCode.get(2))) {
				neighborPoints.add(temp2);
				continue;
			}
			if (temp2.getGeoHash().equals(geoHashCode.get(3))) {
				neighborPoints.add(temp2);
				continue;
			}
			if (temp2.getGeoHash().equals(geoHashCode.get(4))) {
				neighborPoints.add(temp2);
				continue;
			}
			if (temp2.getGeoHash().equals(geoHashCode.get(5))) {
				neighborPoints.add(temp2);
				continue;
			}
			if (temp2.getGeoHash().equals(geoHashCode.get(6))) {
				neighborPoints.add(temp2);
				continue;
			}
			if (temp2.getGeoHash().equals(geoHashCode.get(7))) {
				neighborPoints.add(temp2);
				continue;
			}
			if (temp2.getGeoHash().equals(geoHashCode.get(8))) {
				neighborPoints.add(temp2);
				continue;
			}

		} // �õ����뵱ǰ�켣��ȽϽ��ĵ�ļ�����

		// 3.���ݱȽϽ���·����Ѱ�Ҹ�����·���Σ����浽������
		ArrayList<RoadEdge> neighborEdges = new ArrayList<RoadEdge>();

		int neighborPointNumber = neighborPoints.size();
//					System.out.println(i+"��ǰƥ��·������Ŀ��"+neighborPointNumber);
		if (neighborPointNumber == 0) {
			System.out.println("ƥ��ʧ�ܣ� " + trackPoint.getLongitude() + "," + trackPoint.getLatitude());
		}

		RoadPoint temp3 = null;
		for (int j = 0; j < neighborPointNumber; j++) {
			temp3 = neighborPoints.get(j);

			int roadEdgeNumber = roadEdges.size();
			RoadEdge temp4 = null;
			for (int m = 0; m < roadEdgeNumber; m++) {
				temp4 = roadEdges.get(m);
				if (temp4.getId1() == temp3.getId())
					neighborEdges.add(temp4);
				if (temp4.getId2() == temp3.getId())
					neighborEdges.add(temp4);
			}

		} // �õ�������·����

		// 4.���㸽��·�ε�Ȩ�أ�Ѱ������·����
		int neighborRoadEdgeNumber = neighborEdges.size();
		double thetaFactor = 0.47;
		double distanceFactor = 0.53;
		double probability = 100000;
		RoadEdge temp5 = null;
		RoadPoint r1 = null;
		RoadPoint r2 = null;
//					System.out.println(i+"��ǰƥ��·������Ŀ��"+neighborRoadEdgeNumber);

		for (int n = 0; n < neighborRoadEdgeNumber; n++) {
			r1 = neighborEdges.get(n).getP1();
			r2 = neighborEdges.get(n).getP2();
			double distance = MatchUtility.getPointToSegDistance(r1, r2, trackPoint) * 111000;
			double deltaTheta = MatchUtility.getDeltaTheta(r1, r2, trackPoint.getHeading());
			double matchProbability = distance * distanceFactor + Math.toDegrees(deltaTheta) * thetaFactor;
			if (matchProbability < probability) {
				probability = matchProbability;
				temp5 = neighborEdges.get(n);
			}
		} // �õ���ƥ��·����

		// 5.��������·���Σ����㴹�㣬�õ�ƥ���
		Point point = MatchUtility.getFootNoChange(temp5.getP1(), temp5.getP2(), trackPoint);
		SearchResult matchResult = new SearchResult();
		matchResult.start = temp5.getP1();
		matchResult.end = temp5.getP2();
		matchResult.point = point;

		return matchResult;

	}

	public void search() {
		ArrayList<TrackPoint> trackPoints=tracks.get(0).getTrackPoints();
		// --------------------------���廭�����ɫ--------------------------
		Color red = new Color(255, 0, 0);// ������ɫ
		Color black = new Color(0, 0, 0);
		Color blue = new Color(0, 0, 255);
		Color green = new Color(0, 255, 0);
		Plot2DPanel plot = new Plot2DPanel();// ���廭ͼ���
		JFrame frame = new JFrame("A plot test");
		// --------------------------����·������ɫ��--------------------------
		// ����·���ߣ�����·���ߣ���ɫ������
		double[] point1;
		double[] point2;
		int edgeNumber = roadEdges.size();
		RoadEdge tempEdge = null;
		RoadPoint roadPoint1 = null;
		RoadPoint roadPoint2 = null;
		for (int j = 0; j < edgeNumber; j++) {
			tempEdge = roadEdges.get(j);
			roadPoint1 = tempEdge.getP1();
			roadPoint2 = tempEdge.getP2();
			point1 = new double[] { roadPoint1.getLongitude(), roadPoint1.getLatitude() };
			point2 = new double[] { roadPoint2.getLongitude(), roadPoint2.getLatitude() };
			plot.addLinePlot("", black, point1, point2);
		}
		// --------------------------���Ʊ���λ�ã���ɫ��--------------------------
		int trackPointsNumber = trackPoints.size();
		for (int i = 0; i < trackPointsNumber; i++) {
			point1 = new double[] { trackPoints.get(i).getLongitude() };
			point2 = new double[] { trackPoints.get(i).getLatitude() };
			plot.addScatterPlot("", blue, point1, point2);
		}
//		int ll = roadPoints.size();
//		for (int i = 0; i < ll; i++) {
//			point1 = new double[] { roadPoints.get(i).getLongitude() };
//			point2 = new double[] { roadPoints.get(i).getLatitude() };
//			plot.addScatterPlot("", red, point1, point2);
//		}

//		plot.addScatterPlot("",red, point1, point2);
//		point1 = new double[] { roadPoints.get(2128).getLongitude() };
//		point2 = new double[] { roadPoints.get(2128).getLatitude() };
//		plot.addScatterPlot("", green, point1, point2);
//		point1 = new double[] { roadPoints.get(2129).getLongitude() };
//		point2 = new double[] { roadPoints.get(2129).getLatitude() };
//		plot.addScatterPlot("", red, point1, point2);
		
		// set the label of the plot panel
		plot.setAxisLabel(0, " longitude");
		plot.setAxisLabel(1, "latuitude");
		plot.setSize(1200, 1200);
		// create a frame
		frame = new JFrame("A plot test");
		// set the size of the frame
		frame.setSize(1200, 1200);
		// set the content of the frame as the plot panel
		frame.setContentPane(plot);
		frame.setVisible(true);
		

		TrackPoint first = trackPoints.get(0);
		SearchResult matchResult = MapMatchOnePoint(first);
		Point firstMapMatch = matchResult.point;
		RoadPoint r1 = matchResult.start;
		RoadPoint r2 = matchResult.end;
//        System.out.println("��һ���㣺"+roadPoints.indexOf(r1)+" "+roadPoints.indexOf(r2));
		// ���㱨��λ�õ�֮����ܾ���s2
		double s2 = 0;
//		double s1=0,s3=0;
		double averageV = 0;
		double v1, v2;
		long t1, t2;
		double deltaT;
//		double lon1,lon2,lat1,lat2;
		ArrayList<Double> segments = new ArrayList<Double>();
		for (int i = 0; i < trackPointsNumber - 1; i++) {
			v1 = trackPoints.get(i).getGroundSpeed();
			v2 = trackPoints.get(i + 1).getGroundSpeed();
			averageV = (v1 + v2) / 2.0 * 0.5144444;// ��λת��
			t1 = trackPoints.get(i).getDate().getTime();
			t2 = trackPoints.get(i + 1).getDate().getTime();
			deltaT = (t2 - t1) / 1000.0f;// ��
			s2 = (deltaT * averageV) / (Math.PI * 6371393 /180);// ��λת��
//			System.out.println(deltaT +"*" +averageV+"="+deltaT * averageV);
			segments.add(s2);
			System.out.println(s2);
//			System.out.println(s2);
//			s1=s1+s2;
//			lon1=trackPoints.get(i).getLongitude();
//			lon2=trackPoints.get(i+1).getLongitude();
//			lat1=trackPoints.get(i).getLongitude();
//			lat2=trackPoints.get(i+1).getLongitude();
//			
//			s3=s3+Math.sqrt(Math.pow(lon1-lon2,2)+Math.pow(lat1-lat2,2));
		}
//		System.out.println("������ܾ��룺s1="+s1);
//		System.out.println("���¾��룺s3="+s3);
		int N = 50;
		double l = 0.001;// �ƶ���Χ
		double s = 0;
		double delta = 0.001 / N;

		ArrayList<SearchResult> resultPoints = new ArrayList<SearchResult>();
		double maxProbability = java.lang.Double.MIN_VALUE;
		ArrayList<SearchResult> tempPoints = new ArrayList<SearchResult>();

		
		//ѭ�����õ�����ʱ����
		SearchResult pre=null;
		SearchResult current=null;
		SearchResult next=null;
		for (int i = 1; i < 2; i++) {
			s = i * delta;
//			System.out.println(i+"************");
//			System.out.println("getPointsByDistance");
			SearchResult[] ps = getPointsByDistance(firstMapMatch, r1, r2, s);

			// ����
			tempPoints.add(ps[0]);
			for (int n = 1; n < trackPointsNumber; n++) {
				
//				public Point getPointByStartPoint(Point start,RoadPoint r1,RoadPoint r2,double s,TrackPoint trackPoint) {
//				System.out.println("getPointByStartPoint"+" "+n);
				pre=null;
				if(n>=2) {
				   pre=tempPoints.get(n-2);
				}
				current=tempPoints.get(n-1);
				if(pre==null) {
					next=getPointByStartPoint(null,current.point, current.end, current.start, segments.get(n - 1),trackPoints.get(n));
				}else {
					next=getPointByStartPoint(pre.point,current.point, current.end, current.start, segments.get(n - 1),trackPoints.get(n));	
				}
//				System.out.println("tset:"+test);
				tempPoints.add(next);
			}
			// �������Ƹ���
			double tempPro = 0;
//			Point p1, p2;
//			for (int m = 0; m < trackPointsNumber; m++) {
//				p1 = tempPoints.get(m).point;
//				p2 = new Point(trackPoints.get(m).getLongitude(), trackPoints.get(m).getLatitude());
////				System.out.println(Math.log((BiDimensionGauss(p1.x - p2.x, p1.y - p2.y))));
//				tempPro = tempPro+Math.log((BiDimensionGauss(p1.x - p2.x, p1.y - p2.y)));
//			}
			
			if(i==1) {
				System.out.println("-----"+tempPoints.size());
				for (int q = 0; q <tempPoints.size(); q++) {
					point1 = new double[] { tempPoints.get(q).point.x };
					point2 = new double[] { tempPoints.get(q).point.y };
					plot.addScatterPlot("", red, point1, point2);
				}
			}
			
//			System.out.println("tempro:"+tempPro);
//			if (tempPro > maxProbability) {
//				maxProbability = tempPro;
//				resultPoints.clear();
//				resultPoints.addAll(tempPoints);
////				System.out.println(maxProbability);
//			}
			// ���ñ���
			tempPoints.clear();
			tempPro = 0;

			// ����
			tempPoints.add(ps[1]);
			for (int n = 1; n < trackPointsNumber; n++) {
				pre=null;
				if(n>=2) {
				   pre=tempPoints.get(n-2);
				}
				current=tempPoints.get(n-1);
				if(pre==null) {
				    next=getPointByStartPoint(null,current.point, current.end, current.start, segments.get(n - 1),
						trackPoints.get(n));
				}else {
					next=getPointByStartPoint(pre.point,current.point, current.end, current.start, segments.get(n - 1),
							trackPoints.get(n));
				}
				
//				System.out.println("tset2:"+test);
				tempPoints.add(next);
			}
			// �������Ƹ���
//			for (int m = 0; m < trackPointsNumber; m++) {
//				p1 = tempPoints.get(m).point;
//				p2 = new Point(trackPoints.get(m).getLongitude(), trackPoints.get(m).getLatitude());
////				System.out.println(Math.log((BiDimensionGauss(p1.x - p2.x, p1.y - p2.y))));
//				tempPro = tempPro+Math.log( BiDimensionGauss(p1.x - p2.x, p1.y - p2.y));
//			}
//			System.out.println("tempro2:"+tempPro);
//			if (tempPro > maxProbability) {
//				maxProbability = tempPro;
//				resultPoints.clear();
//				resultPoints.addAll(tempPoints);
////				System.out.println(maxProbability);
//
//			}

			// ���ñ���
			tempPoints.clear();

		}
        System.out.println(resultPoints.size());
		// ���Ƶ�ͼƥ���λ�ã��tɫ��
//		for (int i = 0; i < resultPoints.size(); i++) {
//			System.out.println("���ƽ��");
//			point1 = new double[] { resultPoints.get(i).point.x };
//			point2 = new double[] { resultPoints.get(i).point.y };
//			plot.addScatterPlot("", red, point1, point2);
//		}
		

	}

	/**
	 * ������ʼ·���κ���ʼ�㣬��·����Ѱ�������켣���λ�ã�����s��
	 * 
	 * @param current      ·����һ��
	 * @param r1         ����·���˵�
	 * @param r2         ����·���˵�
	 * @param s          ����
	 * @param trackPoint �α�����λ�õ�
	 * @return ���
	 */
	public SearchResult getPointByStartPoint(Point pre,Point current, RoadPoint r1, RoadPoint r2, double s, TrackPoint trackPoint) {

		// �ڵ�ǰ·��������ͼ�ҵ��ƶ����������ʼ��
		SearchResult[] PointInSegment = getPointsInSegment(s, r1, r2, current);
		// �ж��Ƿ��ҵ��������ƶ���ĳ�ʼ��
		int nullNumber = 0;
		for (int j = 0; j < 2; j++) {
			if (PointInSegment[j] == null)
				nullNumber++;
		}
//		System.out.println("nullnumber "+nullNumber);
		// ����������������ڵ�ǰ·����֮��Ѱ��
		if (nullNumber == 1) {
			// ͨ��floydѰ����һ��
			// �ж�ʹ�õ�ǰ·������˵㻹���Ҷ˵�
			double dis1 = Math.pow(r1.getLatitude() - current.y, 2) + Math.pow(r1.getLongitude() - current.x, 2);
			double dis2 = Math.pow(r2.getLatitude() - current.y, 2) + Math.pow(r2.getLongitude() - current.x, 2);
			RoadPoint temp = null;// ��ǰ·���ζζ˵�
			double ss = s;
			if (dis1 > dis2) {
				temp = r2;
				ss = ss - dis2;
			} else {
				temp = r1;
				ss = ss - dis1;
			}
			// �ڴ������ֻ����һ����
			SearchResult[] anotherPointInSegment = getPointOutSegment(temp, r1, r2, ss);
			// ���õ���뵽��������null
			for (int m = 0; m < 2; m++) {
				if (PointInSegment[m] == null) {
					for (int n = 0; n < 2; n++) {
						if (anotherPointInSegment[n] != null) {
							PointInSegment[m] = anotherPointInSegment[n];
						}
					}
				}
			}
			// ����nullNumber==1
		} else if (nullNumber == 2) {
			// ͨ��floydѰ����һ��
			// �ж�ʹ�õ�ǰ·������˵㻹���Ҷ˵�
			double dis1 = Math.sqrt(Math.pow(r1.getLatitude() - current.y, 2) + Math.pow(r1.getLongitude() - current.x, 2));
			double dis2 = Math.sqrt(Math.pow(r2.getLatitude() - current.y, 2) + Math.pow(r2.getLongitude() - current.x, 2));
			double ss = s;
//			System.out.println("ss:"+ss);
			// �ڴ������ֻ����һ����
			SearchResult[] anotherPointInSegment1 = getPointOutSegment(r1, r1, r2, ss - dis1);// ��һ���ҵ�
			SearchResult[] anotherPointInSegment2 = getPointOutSegment(r2, r1, r2, ss - dis2);// ����һ���ҵ�
			// ���õ���뵽��������null
			for (int n = 0; n < 2; n++) {
				if (anotherPointInSegment1[n] != null) {
					PointInSegment[0] = anotherPointInSegment1[n];
				}
			}
			for (int n = 0; n < 2; n++) {
				if (anotherPointInSegment2[n] != null) {
					PointInSegment[1] = anotherPointInSegment2[n];
				}
			}
		} // ����nullNumber==2
			// ��ʱPointInSegment������һ�����������ƶ���ĵ�
		double bestDis = 0;
		SearchResult bestPoint;
		double dis1,dis2;
		if(pre==null) {//������ǰһ��ʱ�����ñ���λ�õ㾭��ȡ��
			dis1 = Math.pow(PointInSegment[0].point.x - trackPoint.getLongitude(), 2)
					+ Math.pow(PointInSegment[0].point.y - trackPoint.getLatitude(), 2);
			dis2 = Math.pow(PointInSegment[1].point.x - trackPoint.getLongitude(), 2)
					+ Math.pow(PointInSegment[1].point.y - trackPoint.getLatitude(), 2);
			if (dis1 > dis2) {
				bestDis = dis2;
				bestPoint = PointInSegment[1];
			} else {
				bestDis = dis1;
				bestPoint = PointInSegment[0];
			}
			return bestPoint;
		}else {//����ǰһ��ʱ������ǰһ��λ�õ㾭��ȡ��
			dis1 = Math.pow(PointInSegment[0].point.x - pre.x, 2)
					+ Math.pow(PointInSegment[0].point.y - pre.y, 2);
			dis2 = Math.pow(PointInSegment[1].point.x - pre.x, 2)
					+ Math.pow(PointInSegment[1].point.y -pre.y, 2);
			if (dis1 > dis2) {
				bestDis = dis1;
				bestPoint = PointInSegment[0];
			} else {
				bestDis = dis2;
				bestPoint = PointInSegment[1];
			}
			return bestPoint;
		}

	}

	/**
	 * ����·���κ�·����һ�㣬��·����Ѱ�Ҿ���s�������
	 * 
	 * @param start ·����һ��
	 * @param r1    ����·���˵�
	 * @param r2    ����·���˵�
	 * @param s     ����
	 * @return ���
	 */
	public SearchResult[] getPointsByDistance(Point start, RoadPoint r1, RoadPoint r2, double s) {

		// �ڵ�ǰ·��������ͼ�ҵ��ƶ����������ʼ��
		SearchResult[] PointInSegment = getPointsInSegment(s, r1, r2, start);
		// �ж��Ƿ��ҵ��������ƶ���ĳ�ʼ��
		int nullNumber = 0;
		for (int j = 0; j < 2; j++) {
			if (PointInSegment[j] == null)
				nullNumber++;
		}
//		System.out.println("nullnumber:"+nullNumber);
		// ����������������ڵ�ǰ·����֮��Ѱ��
		if (nullNumber == 1) {
			// ͨ��floydѰ����һ��
			// �ж�ʹ�õ�ǰ·������˵㻹���Ҷ˵�
			double dis1 = Math.pow(r1.getLatitude() - start.y, 2) + Math.pow(r1.getLongitude() - start.x, 2);
			double dis2 = Math.pow(r2.getLatitude() - start.y, 2) + Math.pow(r2.getLongitude() - start.x, 2);
			RoadPoint temp = null;// ��ǰ·���ζζ˵�
			double ss = s;
			if (dis1 > dis2) {
				temp = r2;
				ss = ss - dis2;
			} else {
				temp = r1;
				ss = ss - dis1;
			}
			// �ڴ������ֻ����һ����
			SearchResult[] anotherPointInSegment = getPointOutSegment(temp, r1, r2, ss);
			// ���õ���뵽��������null
			for (int m = 0; m < 2; m++) {
				if (PointInSegment[m] == null) {
					for (int n = 0; n < 2; n++) {
						if (anotherPointInSegment[n] != null) {
							PointInSegment[m] = anotherPointInSegment[n];
						}
					}
				}
			}
			// ����nullNumber==1
		} else if (nullNumber == 2) {
			// ͨ��floydѰ����һ��
			// �ж�ʹ�õ�ǰ·������˵㻹���Ҷ˵�
			double dis1 = Math.sqrt(Math.pow(r1.getLatitude() - start.y, 2) + Math.pow(r1.getLongitude() - start.x, 2));
			double dis2 = Math.sqrt(Math.pow(r2.getLatitude() - start.y, 2) + Math.pow(r2.getLongitude() - start.x, 2));
			double ss = s;
			// �ڴ������ֻ����һ����
			SearchResult[] anotherPointInSegment1 = getPointOutSegment(r1, r1, r2, ss - dis1);// ��һ���ҵ�
			SearchResult[] anotherPointInSegment2 = getPointOutSegment(r2, r1, r2, ss - dis2);// ����һ���ҵ�
			// ���õ���뵽��������null
			for (int n = 0; n < 2; n++) {
				if (anotherPointInSegment1[n] != null) {
					PointInSegment[0] = anotherPointInSegment1[n];
				}
			}
			for (int n = 0; n < 2; n++) {
				if (anotherPointInSegment2[n] != null) {
					PointInSegment[1] = anotherPointInSegment2[n];
				}
			}
		} // ����nullNumber==2
			// ��ʱPointInSegment������һ�����������ƶ������ʼ��

		return PointInSegment;

	}

	/**
	 * �Ӹ���·���˵����Ѱ�Ҿ���ӽ�s��·���˵�
	 * 
	 * @param r ����·���˵�
	 * @param s ����
	 * @return ·���˵�����
	 */
	public SearchResult[] getPointOutSegment(RoadPoint r, RoadPoint r1, RoadPoint r2, double s) {
		
		// ͨ��floydѰ����һ��
//		System.out.println("getPointOutSegment---------------");
		double[] dis = getPointDistanceByFloyd(r, s);// ��¼folyd�õ�������ĵ�temp�˵�ľ���
//		System.out.println("�õ��ľ��룺 "+dis[0]+" "+dis[1]);
		RoadPoint[] PointByFloyd = getPointsByFloyd(r, s);// ��¼folyd�õ�������
//		System.out.println("���룺"+s);

		RoadPoint temp = r;// ��ǰ·���ζζ˵�
		// �ж�ʹ��floyd���ص���һ����
		int r_index = roadPoints.indexOf(r);
		int r1_index = roadPoints.indexOf(r1);
		int r2_index = roadPoints.indexOf(r2);

//		System.out.println("��ʼ�˵㣺 "+r_index);
//		System.out.println("��ʼ·���˵㣺"+r1_index+"  "+r2_index);
		int floyd_index_0 = roadPoints.indexOf(PointByFloyd[0]);
		int floyd_index_1 = roadPoints.indexOf(PointByFloyd[1]);
//		System.out.println("���·���˵㣺"+floyd_index_0+"  "+floyd_index_1);
		ArrayList<Integer> path1 = floydUtil.findTheRoad(floyd_index_0, r_index);
//		System.out.println("·��1��"+path1.toString());
		ArrayList<Integer> path2 = floydUtil.findTheRoad(floyd_index_1, r_index);
//		System.out.println("·��2��"+path2.toString());
		RoadPoint temp2 = null;// ��¼folyd�õ����������нϺõ�һ��
		double min = 0;// ��¼folyd�Ϻõ�һ����ĵ��˵�ľ���
		if (path1.contains(r1_index) && path1.contains(r2_index)) {
			temp2 = PointByFloyd[1];
			min = dis[1];
		} else if (path2.contains(r1_index) && path2.contains(r2_index)) {
			temp2 = PointByFloyd[0];
			min = dis[0];
		}else {//����������õ�����������һ��������
			temp2=PointByFloyd[1];
					min = dis[1];
		}
			
//		System.out.println(temp2);
		// �ҵ�folyd���Ӧ������·���Σ�������������2����
		ArrayList<RoadEdge> deuxEdge = new ArrayList<RoadEdge>();
		int roadEdgeNumber = roadEdges.size();
		RoadEdge tempEdge = null;
		for (int m = 0; m < roadEdgeNumber; m++) {
			tempEdge = roadEdges.get(m);
//			System.out.println("����"+m);
			if (tempEdge.getId1() == temp2.getId()) {
				deuxEdge.add(tempEdge);
			} else if (tempEdge.getId2() == temp2.getId()) {
				deuxEdge.add(tempEdge);
			}
		}
		// ɸѡfolyd���Ӧ������·����
		roadEdgeNumber = deuxEdge.size();
		RoadEdge bestEdge = null;
		double tempdis = 100;// ����һ���ϴ��ֵ
		double dis1 = 0;
		for (int m = 0; m < roadEdgeNumber; m++) {
			tempEdge = deuxEdge.get(m);
			// �ж�·���˵�1
			if (tempEdge.getId1() != temp2.getId()) {
				// ��·��������
				if (tempEdge.getId1() == temp.getId()) {
					bestEdge = tempEdge;
					break;
				} else {
					dis1 = getDisBeyweenTwoPointByFloyd(temp, tempEdge.getP1());
					if (dis1 < tempdis) {
						bestEdge = tempEdge;
						tempdis = dis1;
					}
				}
			}
			// �ж�·���˵�2
			if (tempEdge.getId2() != temp2.getId()) {
				// ��·��������
				if (tempEdge.getId2() == temp.getId()) {
					bestEdge = tempEdge;
					break;
				} else {
					dis1 = getDisBeyweenTwoPointByFloyd(temp, tempEdge.getP2());
					if (dis1 < tempdis) {
						bestEdge = tempEdge;
						tempdis = dis1;
					}
				}
			}
		}
		min = min - s;
		// �ڴ������ֻ����һ����,��Ϊpoint���߶ζ˵�
		Point point = new Point(temp2.getLongitude(), temp2.getLatitude());
		return getPointsInSegment(min, bestEdge.getP1(), bestEdge.getP2(), point);
	}

	/**
	 * ��֪·���κ�·������һ�㣬��·������Ѱ�Ҿ���õ����Ϊs����һ�� �ҵ�����Point����,���򷵻�null
	 */
	public SearchResult[] getPointsInSegment(double s, RoadPoint r1, RoadPoint r2, Point point) {
		Point start = new Point(r1.getLongitude(), r1.getLatitude());
		Point end = new Point(r2.getLongitude(), r2.getLatitude());
		Point p = new Point(point.x, point.y);
		double dx = end.x - start.x;
		double dy = end.y - start.y;
		double d2 = dx * dx + dy * dy;
		double k = s / Math.sqrt(d2);
		Point p1 = new Point(p.x + k * dx, p.y + k * dy);
		Point p2 = new Point(p.x - k * dx, p.y - k * dy);
		// �����ж�P1��P2�Ƿ����߶��ϣ��������˻�С�ڵ����㣬�����߶��ϣ�
		// �ж�p1
		Point v1 = new Point(start.x - p1.x, start.y - p1.y);
		Point v2 = new Point(end.x - p1.x, end.y - p1.y);
		if (v1.x * v2.x + v1.y * v2.y > 0)
			p1 = null;
		// �ж�p2
		v1 = new Point(start.x - p2.x, start.y - p2.y);
		v2 = new Point(end.x - p2.x, end.y - p2.y);
		if (v1.x * v2.x + v1.y * v2.y > 0)
			p2 = null;
		SearchResult temp1 = null;
		SearchResult temp2 = null;
		if (p1 != null) {
			temp1 = new SearchResult();
			temp1.start = r1;
			temp1.end = r2;
			temp1.point = p1;
		}
		if (p2 != null) {
			temp2 = new SearchResult();
			temp2.start = r1;
			temp2.end = r2;
			temp2.point = p2;
		}

		SearchResult[] result = new SearchResult[] { temp1, temp2 };
		return result;
	}

	/**
	 * ��֪·����,��Ѱ�Ҿ���·���νӽ�����Ϊs�ĵ� ��ÿ��·���ζ˵��Ӧ������ �ҵ�����Point����,���򷵻�null
	 */
	public RoadPoint[] getPointsByFloyd(RoadPoint p1, double s) {
		int start = roadPoints.indexOf(p1);
		int[] result1 = floydUtil.findNearstPoint(start, s);
		return new RoadPoint[] { roadPoints.get(result1[0]), roadPoints.get(result1[1]) };
	}

	/**
	 * ��֪·����,��Ѱ�Ҿ���·���νӽ�����Ϊs�ĵ�ľ���ֵ ��ÿ��·���ζ˵��Ӧ������ �ҵ�����double����ֵ����,���򷵻�null
	 */
	public double[] getPointDistanceByFloyd(RoadPoint p1, double s) {
		int start = roadPoints.indexOf(p1);
		double[] result1 = floydUtil.findNearstDis(start, s);
		return result1;
	}

	public double getDisBeyweenTwoPointByFloyd(RoadPoint p1, RoadPoint p2) {
		int start = roadPoints.indexOf(p1);
		int end = roadPoints.indexOf(p2);
		return floydUtil.findDis(start, end);
	}

	/**
	 * ��ά��˹�ֲ���0��ֵ��x��y���򷽲����
	 */
	double sigma = 10/(Math.PI * 6371393 /180);

	public double BiDimensionGauss(double x, double y) {
		return 1.0 / (2.0 * Math.PI * Math.pow(sigma, 2))
				* Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2.0 * Math.pow(sigma, 2)));
	}
}
