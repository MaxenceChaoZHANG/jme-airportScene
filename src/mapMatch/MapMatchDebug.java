package mapMatch;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.dom4j.DocumentException;
import org.math.plot.Plot2DPanel;

import dataEntity.Road;
import dataEntity.RoadEdge;
import dataEntity.RoadPoint;
import dataEntity.RoadWay;
import dataEntity.Track;
import dataEntity.TrackPoint;
import mapMatch.geoHash.GeoHashHelper;
import mapMatch.geoHash.MyGeoHashHelper;
import mapMatch.road.RoadXMLUtility;

public class MapMatchDebug {

	public static void main(String[] args) {
		
//		double s=0;
		// ��ȡ�켣����
		ArrayList<Track> tracks = null;
		try {
			tracks = pathSmooth.ReadData.readData("track/Track_de_1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ��ȡ·������
		Road road = null;
		try {
			road = RoadXMLUtility.ReadXML();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<RoadPoint> roadPoints = road.getRoadPoints();
		ArrayList<RoadWay> roadWays = road.getRoadWays();

		// ��RoadWay�г�ȡ���е�RoadEdge
		ArrayList<RoadEdge> roadEdges = new ArrayList<RoadEdge>();
		int wayNumber = roadWays.size();
		RoadWay tempWay = null;
		for (int i = 0; i < wayNumber; i++) {
			roadEdges.addAll(roadWays.get(i).getRoadEdges());
		}

		// ������ɫ
		Color red = new Color(255, 0, 0);
		Color black = new Color(0, 0, 0);
		Color blue = new Color(0, 0, 255);
		Color green = new Color(0, 255, 0);

		// ���廭ͼ���
		Plot2DPanel plot = new Plot2DPanel();

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
//		for (int i = 0; i < trackPoints.size() - 1; i++) {
//			TrackPoint Point1 = trackPoints.get(i);
//			TrackPoint Point2 = trackPoints.get(i + 1);
//			double[] point1 = { Point1.getLongitude(), Point1.getLatitude() };
//			double[] point2 = { Point2.getLongitude(), Point2.getLatitude() };
//
//			plot.addLinePlot("", blue, point1, point2);
//		}

		// ����geoHashHepler����
//		GeoHashHelper geoHash = new GeoHashHelper();
		MyGeoHashHelper geoHash = new MyGeoHashHelper();
		List<String> geoHashCode = null;

		// ����ÿһ���켣����е�ͼƥ��
		int trackPointNumber = trackPoints.size();
		TrackPoint temp1 = null;
		for (int i = 30; i < trackPointNumber; i++) {
			// 1.��ȡ��ǰ�켣�㸽����9��geohashCode
			temp1 = trackPoints.get(i);
			geoHashCode = geoHash.aroundWith7Char(temp1.getLatitude(), temp1.getLongitude());
//			geoHashCode=geoHash.around(temp1.getLatitude(), temp1.getLongitude()) ;

			// 2.����ÿһ��·���㣬����geoHashѰ�Ҿ��뵱ǰ�켣��ȽϽ��ĵ�,���浽������
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

//				System.out.println(distance);
				double deltaTheta = MatchUtility.getDeltaTheta(r1, r2, temp1.getHeading());
//				Math.toDegrees(deltaTheta)*thetaFactor
//				System.out.println(Math.toDegrees(deltaTheta));
				double matchProbability = distance * distanceFactor + Math.toDegrees(deltaTheta) * thetaFactor;
//                System.out.println(Math.sin(deltaTheta));

				if (matchProbability < probability) {
					probability = matchProbability;
					temp5 = neighborEdges.get(n);
//					System.out.println(n);
				}
			} // �õ���ƥ��·����

			// 5.��������·���Σ����㴹�㣬�õ�ƥ���
			// ���ƹ켣��仯���
//			double[] point3 = { temp1.getLongitude(), temp1.getLatitude() };
//			TrackPoint matchResult = MatchUtility.getFoot(temp5.getP1(), temp5.getP2(), temp1);
//			double[] point4 = { temp1.getLongitude(), temp1.getLatitude() };
//			plot.addLinePlot("", green, point3, point4);

			// �������·����
			r1=temp5.getP1();
			r2 = temp5.getP2();		
//			s=s+Math.sqrt(Math.pow(r1.getLatitude() -r2.getLatitude(), 2) + Math.pow(r1.getLongitude() - r2.getLongitude(), 2));
//			System.out.println(Math.sqrt(Math.pow(r1.getLatitude() -r2.getLatitude(), 2) + Math.pow(r1.getLongitude() - r2.getLongitude(), 2)));
			double[] point1= {r1.getLongitude(),r1.getLatitude()};
			double[] point2= {r2.getLongitude(),r2.getLatitude()};	
			plot.addLinePlot("", red, point1, point2);	

		}
//		System.out.println("s:"+s);

//		for (int i = 0; i < trackPoints.size() - 1; i++) {
//			TrackPoint Point1 = trackPoints.get(i);
//			TrackPoint Point2 = trackPoints.get(i + 1);
//			double[] point1 = { Point1.getLongitude(), Point1.getLatitude() };
//			double[] point2 = { Point2.getLongitude(), Point2.getLatitude() };
//
//			plot.addLinePlot("", red, point1, point2);
//		}

		// set the label of the plot panel
		plot.setAxisLabel(0, " longitude");
		plot.setAxisLabel(1, "latuitude");

		// set the boundary of the plot panel
//	    double boundMin1[]  = {0, 0};
//	    double boundMax1[]  = {200, 200};
//	    plot.setFixedBounds(boundMin1, boundMax1);	
		plot.setSize(1200, 1200);
		// create a frame
		JFrame frame = new JFrame("A plot test");

		// set the size of the frame
		frame.setSize(1200, 1200);

		// set the content of the frame as the plot panel
		frame.setContentPane(plot);
		frame.setVisible(true);

	}

}
