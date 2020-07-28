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
import mapMatch.geoHash.MyGeoHashHelper;
import mapMatch.road.RoadXMLUtility;

public class Demo {
	
	 Road road;// 读取路网数据
	 ArrayList<RoadPoint> roadPoints;
	 ArrayList<RoadWay> roadWays ;
	 ArrayList<RoadEdge> roadEdges ;
	
	 ArrayList<Track> tracks = null;// 读取轨迹数据
	
	 Color red = new Color(255, 0, 0);// 定义颜色
	 Color black = new Color(0, 0, 0);
	 Color blue = new Color(0, 0, 255);
	 Color green = new Color(0, 255, 0);
	 Plot2DPanel plot;	// 定义画图面板
	 JFrame frame;
	
	public static void main(String[] args) {
		Demo demo = new Demo();
		demo.MapMatch();
		
	}
	
	
	public Demo() {		
	    plot = new Plot2DPanel();	// 定义画图面板
		frame = new JFrame("A plot test");
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
	}




	public  void MapMatch() {
//--------------------------初始化数据--------------------------		
		// 读取轨迹数据
		tracks = null;
		try {
			tracks = pathSmooth.ReadData.readData("track/Track_de_1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 读取路网数据
		road = null;
		try {
			road = RoadXMLUtility.ReadXML();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		roadPoints = road.getRoadPoints();
		roadWays = road.getRoadWays();

		// 从RoadWay中抽取所有的RoadEdge
		roadEdges = new ArrayList<RoadEdge>();
		int wayNumber = roadWays.size();
		for (int i = 0; i < wayNumber; i++) {
			roadEdges.addAll(roadWays.get(i).getRoadEdges());
		}
//--------------------------绘制路网和原始轨迹--------------------------	
		// 遍历路径边，绘制路径边（黑色线条）
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

		// 绘制原始轨迹（蓝色线条）
		ArrayList<TrackPoint> trackPoints = tracks.get(0).getTrackPoints();
		for (int i = 0; i < trackPoints.size() - 1; i++) {
			TrackPoint Point1 = trackPoints.get(i);
			TrackPoint Point2 = trackPoints.get(i + 1);
			double[] point1 = { Point1.getLongitude(), Point1.getLatitude() };
			double[] point2 = { Point2.getLongitude(), Point2.getLatitude() };

			plot.addLinePlot("", blue, point1, point2);
		}
//--------------------------地图匹配算法--------------------------	
		// 创建geoHashHepler对象
		MyGeoHashHelper geoHash = new MyGeoHashHelper();
		List<String> geoHashCode = null;

		// 遍历每一个轨迹点进行地图匹配
		int trackPointNumber = trackPoints.size();
		TrackPoint temp1 = null;
		for (int i = 0; i < trackPointNumber; i++) {
			// 1.获取当前轨迹点附近的9个geohashCode
			temp1 = trackPoints.get(i);
			geoHashCode = geoHash.aroundWith7Char(temp1.getLatitude(), temp1.getLongitude());
			// 2.遍历网点，根据geoHash寻找距离当前轨迹点比较近的9个点
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

			} // 得到距离当前轨迹点比较近的点的集合中

			// 3.根据比较近的路径点寻找附近的路近段，并存到集合中
			ArrayList<RoadEdge> neighborEdges = new ArrayList<RoadEdge>();

			int neighborPointNumber = neighborPoints.size();
//			System.out.println(i+"当前匹配路径点数目："+neighborPointNumber);
			if (neighborPointNumber == 0) {
				System.out.println("匹配失败：" + i + " " + temp1.getLongitude() + "," + temp1.getLatitude());
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

			} // 得到附近的路近段

			// 4.计算附近路段的权重，寻找最优路径段
			int neighborRoadEdgeNumber = neighborEdges.size();
			double thetaFactor = 0.47;
			double distanceFactor = 0.53;
			double probability = 100000;
			RoadEdge temp5 = null;
			RoadPoint r1 = null;
			RoadPoint r2 = null;
//			System.out.println(i+"当前匹配路径段数目："+neighborRoadEdgeNumber);

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
			} // 得到最匹配路近段

			// 5.根据最优路径段，计算垂足，得到匹配点
			// 绘制轨迹点变化情况
			double[] point3 = { temp1.getLongitude(), temp1.getLatitude() };
			TrackPoint matchResult = MatchUtility.getFoot(temp5.getP1(), temp5.getP2(), temp1);
			double[] point4 = { temp1.getLongitude(), temp1.getLatitude() };
			plot.addLinePlot("", green, point3, point4);

			// 绘制最佳路径段
//			r1 = roadPoints.get(temp5.getIndexINlist1());
//			r2 = roadPoints.get(temp5.getIndexINlist2());		
//			double[] point1= {r1.getLongitude(),r1.getLatitude()};
//			double[] point2= {r2.getLongitude(),r2.getLatitude()};	
//			plot.addLinePlot("", red, point1, point2);	

		}
//--------------------------绘制地图匹配后的结果--------------------------	
		for (int i = 0; i < trackPoints.size() - 1; i++) {
			TrackPoint Point1 = trackPoints.get(i);
			TrackPoint Point2 = trackPoints.get(i + 1);
			double[] point1 = { Point1.getLongitude(), Point1.getLatitude() };
			double[] point2 = { Point2.getLongitude(), Point2.getLatitude() };

			plot.addLinePlot("", red, point1, point2);
		}

	}

	
	

}
