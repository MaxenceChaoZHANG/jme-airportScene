package mapMatch;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class Demo4 {

	Road road;// 读取路网数据
	ArrayList<RoadPoint> roadPoints;
	ArrayList<RoadWay> roadWays;
	ArrayList<RoadEdge> roadEdges;

	ArrayList<RoadPoint> trackRoadPoints = new ArrayList<RoadPoint>();
	ArrayList<RoadEdge> trackRoadEdges = new ArrayList<RoadEdge>();

	ArrayList<Track> tracks = null;// 读取轨迹数据
	ArrayList<TrackPoint> trackPoints;

	Floyd globalFloydUtil;
	Floyd trackFloydUtil;

	public static void main(String[] args) {
		Demo4 demo = new Demo4();
		demo.search();
//		demo.MapMatch();
	}

	class SearchResult {
		RoadPoint start;
		RoadPoint end;
		Point point;
	}

	public Demo4() {
		// --------------------------初始化数据--------------------------
		// 读取轨迹数据
		tracks = null;
		try {
			tracks = pathSmooth.ReadData.readData("track/Track_de_1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trackPoints = tracks.get(0).getTrackPoints();
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
		// 初始化Floyd工具
		globalFloydUtil = new Floyd(roadPoints, roadEdges);
		globalFloydUtil.floyd();

	}

	public void MapMatch() {

		ArrayList<TrackPoint> copy = new ArrayList<TrackPoint>();
		try {
			copy = deepCopy(trackPoints);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		copy = pathSmooth.DouglasPeuckerUtil.DouglasPeucker(copy,30);
//--------------------------定义画板和颜色--------------------------	
		Color red = new Color(255, 0, 0);// 定义颜色
		Color black = new Color(0, 0, 0);
		Color blue = new Color(0, 0, 255);
		Color green = new Color(0, 255, 0);
		Plot2DPanel plot = new Plot2DPanel();// 定义画图面板
		JFrame frame = new JFrame("A plot test");

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

//		for (int i = 0; i < copy.size() - 1; i++) {
//			TrackPoint Point1 = copy.get(i);
//			TrackPoint Point2 = copy.get(i + 1);
//			double[] point1 = { Point1.getLongitude(), Point1.getLatitude() };
//			double[] point2 = { Point2.getLongitude(), Point2.getLatitude() };
//
//			plot.addLinePlot("", blue, point1, point2);
//		}
//--------------------------地图匹配算法--------------------------	
		// 创建geoHashHepler对象
		MyGeoHashHelper geoHash = new MyGeoHashHelper();
		List<String> geoHashCode = null;

		// 遍历每一个轨迹点进行地图匹配
		int trackPointNumber = copy.size();
		TrackPoint temp1 = null;
		for (int i = 0; i < trackPointNumber; i++) {
			// 1.获取当前轨迹点附近的9个geohashCode
			temp1 = copy.get(i);
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
//			double thetaFactor = 0.53;
//			double distanceFactor = 0.47;
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
//			double[] point3 = { temp1.getLongitude(), temp1.getLatitude() };
//			TrackPoint matchResult = MatchUtility.getFoot(temp5.getP1(), temp5.getP2(), temp1);
//			double[] point4 = { temp1.getLongitude(), temp1.getLatitude() };
//			plot.addLinePlot("", green, point3, point4);

			// 绘制最佳路径段
			r1 = temp5.getP1();
			r2 = temp5.getP2();
//			double[] point1= {r1.getLongitude(),r1.getLatitude()};
//			double[] point2= {r2.getLongitude(),r2.getLatitude()};	
//			plot.addLinePlot("", red, point1, point2);	

//			if(!trackRoadPoints.contains(r1)) {
//				trackRoadPoints.add(r1);
//			}
//			if(!trackRoadPoints.contains(r2)) {
//				trackRoadPoints.add(r2);
//			}
			if (!trackRoadEdges.contains(temp5)) {
				if ((roadPoints.indexOf(r1) == 2181 && roadPoints.indexOf(r2) == 2182)
						|| (roadPoints.indexOf(r1) == 2182 && roadPoints.indexOf(r2) == 2181)) {
					System.out.println("????");
					continue;
				} else {
					trackRoadEdges.add(temp5);
				}
			}
		} // 结束地图匹配
			// =======================开始填充空隙===============================
		RoadPoint r1;
		RoadPoint r2;
		long id1;
		long id2;
		RoadEdge edge;
		ArrayList<Integer> path;
		ArrayList<RoadEdge> addEdge = new ArrayList<RoadEdge>();

		for (int j = 0; j < trackRoadEdges.size() - 1; j++) {
			// 取出连续两个个路径段，并各取出一点，寻找路径
			r1 = trackRoadEdges.get(j).getP1();
			r2 = trackRoadEdges.get(j + 1).getP1();
			path = globalFloydUtil.findTheRoad(roadPoints.indexOf(r1), roadPoints.indexOf(r2));
			// 根据路径结果，判断两个路径段是否连续，视情况进行补充
			for (int m = 0; m < path.size() - 1; m++) {
				r1 = roadPoints.get(path.get(m));
				r2 = roadPoints.get(path.get(m + 1));
				id1 = r1.getId();
				id2 = r2.getId();
				// 根据两个id找对应的路径段
				for (int n = 0; n < roadEdges.size(); n++) {
					edge = roadEdges.get(n);
					if ((edge.getId1() == id1 && edge.getId2() == id2)
							|| (edge.getId1() == id2 && edge.getId2() == id1)) {

						if (!trackRoadEdges.contains(edge)) {// 是否需要补充
							addEdge.add(edge);
//							if(!trackRoadPoints.contains(r1)) {
//								trackRoadPoints.add(r1);
//							}
//							if(!trackRoadPoints.contains(r2)) {
//								trackRoadPoints.add(r2);
//							}
						}
						break;
					} // 补充完毕
				} // 结束内层循环
			}
		} // 结束外层循环

		trackRoadEdges.addAll(addEdge);

		// 绘制最佳路径段
		for(int i=0;i<trackRoadEdges.size();i++) {
			edge=trackRoadEdges.get(i);
			r1 = edge.getP1();
			r2 = edge.getP2();		
			double[] point1= {r1.getLongitude(),r1.getLatitude()};
			double[] point2= {r2.getLongitude(),r2.getLatitude()};	
			plot.addLinePlot("", red, point1, point2);	
		}
 
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

	public SearchResult MapMatchOnePoint(TrackPoint trackPoint) {

		// --------------------------地图匹配算法--------------------------
		// 创建geoHashHepler对象
		MyGeoHashHelper geoHash = new MyGeoHashHelper();
		List<String> geoHashCode = null;
		// 1.获取当前轨迹点附近的9个geohashCode
		geoHashCode = geoHash.aroundWith7Char(trackPoint.getLatitude(), trackPoint.getLongitude());
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
//					System.out.println(i+"当前匹配路径点数目："+neighborPointNumber);
		if (neighborPointNumber == 0) {
			System.out.println("匹配失败： " + trackPoint.getLongitude() + "," + trackPoint.getLatitude());
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
//					System.out.println(i+"当前匹配路径段数目："+neighborRoadEdgeNumber);

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
		} // 得到最匹配路近段

		// 5.根据最优路径段，计算垂足，得到匹配点
		Point point = MatchUtility.getFootNoChange(temp5.getP1(), temp5.getP2(), trackPoint);
		SearchResult matchResult = new SearchResult();
		matchResult.start = temp5.getP1();
		matchResult.end = temp5.getP2();
		matchResult.point = point;

		return matchResult;

	}

	public void search() {
		MapMatch();
		// 初始化Floyd工具
		globalFloydUtil = new Floyd(roadPoints, trackRoadEdges);
		globalFloydUtil.floyd();

		int NN = 210;
		// --------------------------定义画板和颜色--------------------------
		Color red = new Color(255, 0, 0);// 定义颜色
		Color black = new Color(0, 0, 0);
		Color blue = new Color(0, 0, 255);
		Color green = new Color(0, 255, 0);
		Plot2DPanel plot = new Plot2DPanel();// 定义画图面板
		JFrame frame = new JFrame("A plot test");
		// --------------------------绘制路网（黑色）--------------------------
		// 遍历路径边，绘制路径边（黑色线条）
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
		// --------------------------绘制报文位置（蓝色）--------------------------
		int trackPointsNumber = trackPoints.size();
//		for (int i = 0; i < NN; i++) {
//			point1 = new double[] { trackPoints.get(i).getLongitude() };
//			point2 = new double[] { trackPoints.get(i).getLatitude() };
//			plot.addScatterPlot("", blue, point1, point2);
//		}
//		point1 = new double[] { roadPoints.get(2181).getLongitude() };
//		point2 = new double[] { roadPoints.get(2181).getLatitude() };
//		plot.addScatterPlot("", blue, point1, point2);

		TrackPoint first = trackPoints.get(0);
		SearchResult matchResult = MapMatchOnePoint(first);
		Point firstMapMatch = matchResult.point;
		RoadPoint r1 = matchResult.start;
		RoadPoint r2 = matchResult.end;
		// 计算报文位置点之间的总距离s2
		double s2 = 0;
		double s1 = 0, s3 = 0;
		double averageV = 0;
		double v1, v2;
		long t1, t2;
		double deltaT;
		double lon1, lon2, lat1, lat2;
		ArrayList<Double> segments = new ArrayList<Double>();
		for (int i = 0; i < trackPointsNumber - 1; i++) {
			v1 = trackPoints.get(i).getGroundSpeed();
			v2 = trackPoints.get(i + 1).getGroundSpeed();
			averageV = (v1 + v2) / 2.0 * 0.5144444;// 单位转换
			t1 = trackPoints.get(i).getDate().getTime();
			t2 = trackPoints.get(i + 1).getDate().getTime();
			deltaT = (t2 - t1) / 1000.0f;// 秒
			s2 = (deltaT * averageV) / (Math.PI * 6371393 / 180);// 单位转换
//			System.out.println(deltaT +"*" +averageV+"="+deltaT * averageV);
			segments.add(s2);
//			System.out.println(s2);
			s1 = s1 + s2;
//			lon1=trackPoints.get(i).getLongitude();
//			lon2=trackPoints.get(i+1).getLongitude();
//			lat1=trackPoints.get(i).getLongitude();
//			lat2=trackPoints.get(i+1).getLongitude();	
//			s3=s3+Math.sqrt(Math.pow(lon1-lon2,2)+Math.pow(lat1-lat2,2));
		}
//		System.out.println("计算的总距离：s1="+s1);
//		System.out.println("大致距离：s3="+s3);
		int N = 50;
		double l = 0.001;// 移动范围
		double s = 0;
		double delta = 0.0003 / N;

		ArrayList<SearchResult> resultPoints = new ArrayList<SearchResult>();
		double maxProbability = -1.0 / 0.0;
		ArrayList<SearchResult> tempPoints = new ArrayList<SearchResult>();
		// 循环中用到的临时变量
		SearchResult pre = null;
		SearchResult prepre = null;
		SearchResult current = null;
		SearchResult next = null;
		for (int i = 1; i <5; i++) {
			s = i * delta;
			SearchResult[] ps = getPointsByDistance(firstMapMatch, r1, r2, s);
			// 右移
			tempPoints.add(ps[0]);
			for (int n = 1; n < NN; n++) {
//				public Point getPointByStartPoint(Point start,RoadPoint r1,RoadPoint r2,double s,TrackPoint trackPoint) {
				if (segments.get(n - 1) != 0 && n >= 2) {
					if (pre == null) {
						pre = tempPoints.get(n - 1);
					} else {
						prepre = pre;
						pre = tempPoints.get(n - 1);
					}
				}
				current = tempPoints.get(n - 1);
				//根据prepre是否存在传入不同参数
				if (prepre == null) {
					next = getPointByStartPoint(null, current.point, current.end, current.start, segments.get(n - 1),
							trackPoints.get(n));
				} else {
					next = getPointByStartPoint(prepre.point, current.point, current.end, current.start,
							segments.get(n - 1), trackPoints.get(n));
				}
				tempPoints.add(next);
			}
			// 计算右移概率
			double tempPro = 0;
			Point p1, p2;
			for (int m = 0; m < trackPointsNumber; m++) {
				p1 = tempPoints.get(m).point;
				p2 = new Point(trackPoints.get(m).getLongitude(), trackPoints.get(m).getLatitude());
//				System.out.println(BiDimensionGauss(p1.x - p2.x, p1.y - p2.y));
//				System.out.println(Math.log((BiDimensionGauss(p1.x - p2.x, p1.y - p2.y))));
				tempPro = tempPro+Math.log((BiDimensionGauss(p1.x - p2.x, p1.y - p2.y)));
			}

//			if (i == 1) {
//				System.out.println("-----" + tempPoints.size());
//				for (int q = 0; q < NN; q++) {
//					point1 = new double[] { tempPoints.get(q).point.x };
//					point2 = new double[] { tempPoints.get(q).point.y };
//					plot.addScatterPlot("", red, point1, point2);
//				}
//			}

			System.out.println("tempro:"+tempPro);
			if (tempPro > maxProbability) {
				maxProbability = tempPro;
				resultPoints.clear();
				resultPoints.addAll(tempPoints);
//				System.out.println(maxProbability);
			}
			// 重置变量
			tempPoints.clear();
			tempPro = 0;

			// 左移
			tempPoints.add(ps[1]);
			for (int n = 1; n < trackPointsNumber; n++) {
				if (segments.get(n - 1) != 0 && n >= 2) {
					if (pre == null) {
						pre = tempPoints.get(n - 1);
					} else {
						prepre = pre;
						pre = tempPoints.get(n - 1);
					}
				}
				current = tempPoints.get(n - 1);
				//根据prepre是否存在传入不同参数
				if (prepre == null) {
					next = getPointByStartPoint(null, current.point, current.end, current.start, segments.get(n - 1),
							trackPoints.get(n));
				} else {
					next = getPointByStartPoint(prepre.point, current.point, current.end, current.start,
							segments.get(n - 1), trackPoints.get(n));
				}
				tempPoints.add(next);
			}
			// 计算左移概率
			for (int m = 0; m < trackPointsNumber; m++) {
				p1 = tempPoints.get(m).point;
				p2 = new Point(trackPoints.get(m).getLongitude(), trackPoints.get(m).getLatitude());
//				System.out.println(Math.log((BiDimensionGauss(p1.x - p2.x, p1.y - p2.y))));
				tempPro = tempPro+Math.log( BiDimensionGauss(p1.x - p2.x, p1.y - p2.y));
			}
			System.out.println("tempro2:"+tempPro);
			if (tempPro > maxProbability) {
				maxProbability = tempPro;
				resultPoints.clear();
				resultPoints.addAll(tempPoints);
//				System.out.println(maxProbability);
			}

			// 重置变量
			tempPoints.clear();
		}
		System.out.println(resultPoints.size());
		// 绘制地图匹配点位置（紅色）
		for (int i = 0; i < resultPoints.size(); i++) {
			point1 = new double[] { resultPoints.get(i).point.x };
			point2 = new double[] { resultPoints.get(i).point.y };
			plot.addScatterPlot("", red, point1, point2);
		}

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

	/**
	 * 根据起始路径段和起始点，在路网中寻找其他轨迹点的位置（距离s）
	 * 
	 * @param current    路径上一点
	 * @param r1         给定路径端点
	 * @param r2         给定路径端点
	 * @param s          距离
	 * @param trackPoint 参保报文位置点
	 * @return 结果
	 */
	public SearchResult getPointByStartPoint(Point pre, Point current, RoadPoint r1, RoadPoint r2, double s,
			TrackPoint trackPoint) {

		// 在当前路径段上试图找到移动后的两个初始点
		SearchResult[] PointInSegment = getPointsInSegment(s, r1, r2, current);
		// 判断是否找到了两个移动后的初始点
		int nullNumber = 0;
		for (int j = 0; j < 2; j++) {
			if (PointInSegment[j] == null)
				nullNumber++;
		}

//		System.out.println("nullnumber "+nullNumber);
		// 如果不足两个，则在当前路径段之外寻找
		if (nullNumber == 1) {
			// 通过floyd寻找另一点
			// 判断使用当前路径段左端点还是右端点
			double dis1 = Math.pow(r1.getLatitude() - current.y, 2) + Math.pow(r1.getLongitude() - current.x, 2);
			double dis2 = Math.pow(r2.getLatitude() - current.y, 2) + Math.pow(r2.getLongitude() - current.x, 2);
			RoadPoint temp = null;// 当前路径段段端点
			double ss = s;
			if (dis1 > dis2) {
				temp = r2;
				ss = ss - Math.sqrt(dis2);
			} else {
				temp = r1;
				ss = ss - Math.sqrt(dis1);
			}
			// 在此情况下只返回一个点
			SearchResult[] anotherPointInSegment = getPointOutSegment(temp, r1, r2, ss);
			// 将该点加入到外层数组的null
			for (int m = 0; m < 2; m++) {
				if (PointInSegment[m] == null) {
					for (int n = 0; n < 2; n++) {
						if (anotherPointInSegment[n] != null) {
							PointInSegment[m] = anotherPointInSegment[n];
						}
					}
				}
			}

			// 结束nullNumber==1
		} else if (nullNumber == 2) {
			// 通过floyd寻找另一点
			// 判断使用当前路径段左端点还是右端点
			double dis1 = Math
					.sqrt(Math.pow(r1.getLatitude() - current.y, 2) + Math.pow(r1.getLongitude() - current.x, 2));
			double dis2 = Math
					.sqrt(Math.pow(r2.getLatitude() - current.y, 2) + Math.pow(r2.getLongitude() - current.x, 2));
			double ss = s;
//			System.out.println("ss:"+ss);
			// 在此情况下只返回一个点
			SearchResult[] anotherPointInSegment1 = getPointOutSegment(r1, r1, r2, ss - dis1);// 向一边找点
			SearchResult[] anotherPointInSegment2 = getPointOutSegment(r2, r1, r2, ss - dis2);// 向另一边找点

			// 将该点加入到外层数组的null
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
		} // 结束nullNumber==2
			// 此时PointInSegment数组中一定包含两个移动后的点

		double bestDis = 0;
		SearchResult bestPoint;
		double dis1, dis2;
		double preDis = 0;
		if (pre == null) {// 不存在前一点时，利用报文位置点经行取舍
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

		} else {// 存在前一点时，利用前一点位置点经行取舍

			dis1 = Math.pow(PointInSegment[0].point.x - pre.x, 2) + Math.pow(PointInSegment[0].point.y - pre.y, 2);
			dis2 = Math.pow(PointInSegment[1].point.x - pre.x, 2) + Math.pow(PointInSegment[1].point.y - pre.y, 2);
			if (dis1 > dis2) {
				bestDis = dis1;
				bestPoint = PointInSegment[0];
			} else {
				bestDis = dis2;
				bestPoint = PointInSegment[1];
			}
		}
		return bestPoint;

	}

	/**
	 * 根据路径段和路径上一点，在路网中寻找距离s的另外点
	 * 
	 * @param start 路径上一点
	 * @param r1    给定路径端点
	 * @param r2    给定路径端点
	 * @param s     距离
	 * @return 结果
	 */
	public SearchResult[] getPointsByDistance(Point start, RoadPoint r1, RoadPoint r2, double s) {

		// 在当前路径段上试图找到移动后的两个初始点
		SearchResult[] PointInSegment = getPointsInSegment(s, r1, r2, start);
		// 判断是否找到了两个移动后的初始点
		int nullNumber = 0;
		for (int j = 0; j < 2; j++) {
			if (PointInSegment[j] == null)
				nullNumber++;
		}
//		System.out.println("nullnumber:"+nullNumber);
		// 如果不足两个，则在当前路径段之外寻找
		if (nullNumber == 1) {
			// 通过floyd寻找另一点
			// 判断使用当前路径段左端点还是右端点
			double dis1 = Math.pow(r1.getLatitude() - start.y, 2) + Math.pow(r1.getLongitude() - start.x, 2);
			double dis2 = Math.pow(r2.getLatitude() - start.y, 2) + Math.pow(r2.getLongitude() - start.x, 2);
			RoadPoint temp = null;// 当前路径段段端点
			double ss = s;
			if (dis1 > dis2) {
				temp = r2;
				ss = ss - Math.sqrt(dis2);
			} else {
				temp = r1;
				ss = ss - Math.sqrt(dis1);
			}
			// 在此情况下只返回一个点
			SearchResult[] anotherPointInSegment = getPointOutSegment(temp, r1, r2, ss);

			// 将该点加入到外层数组的null
			for (int m = 0; m < 2; m++) {
				if (PointInSegment[m] == null) {
					for (int n = 0; n < 2; n++) {
						if (anotherPointInSegment[n] != null) {
							PointInSegment[m] = anotherPointInSegment[n];
						}
					}
				}
			}
			// 结束nullNumber==1
		} else if (nullNumber == 2) {
			// 通过floyd寻找另一点
			// 判断使用当前路径段左端点还是右端点
			double dis1 = Math.sqrt(Math.pow(r1.getLatitude() - start.y, 2) + Math.pow(r1.getLongitude() - start.x, 2));
			double dis2 = Math.sqrt(Math.pow(r2.getLatitude() - start.y, 2) + Math.pow(r2.getLongitude() - start.x, 2));
			double ss = s;
			// 在此情况下只返回一个点
			SearchResult[] anotherPointInSegment1 = getPointOutSegment(r1, r1, r2, ss - dis1);// 向一边找点
			SearchResult[] anotherPointInSegment2 = getPointOutSegment(r2, r1, r2, ss - dis2);// 向另一边找点

			// 将该点加入到外层数组的null
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
		} // 结束nullNumber==2
			// 此时PointInSegment数组中一定包含两个移动后的起始点

		return PointInSegment;

	}

	/**
	 * 从给定路径端点出发寻找距离接近s的路径端点
	 * 
	 * @param r 给定路径端点
	 * @param s 距离
	 * @return 路径端点数组
	 */
	public SearchResult[] getPointOutSegment(RoadPoint r, RoadPoint r1, RoadPoint r2, double s) {

		// 通过floyd寻找另一点
		ArrayList<Double> dis = getPointDistanceByFloyd(r, s);// 记录folyd得到点的到temp端点的距离(排好序）

		ArrayList<RoadPoint> PointByFloyd = getPointsByFloyd(r, s);// 记录folyd得到点(排好序）

		RoadPoint temp = r;// 当前路径段段端点
		// 判断使用floyd返回的哪一个点
		int r_index = roadPoints.indexOf(r);
		int r1_index = roadPoints.indexOf(r1);
		int r2_index = roadPoints.indexOf(r2);

		RoadPoint temp2 = null;// 记录folyd得到的点中较好的一个
		double min = 0;// 记录folyd较好的一个点的到端点的距离

		int floyd_index;
		ArrayList<Integer> path1;
		for (int i = 0; i < PointByFloyd.size(); i++) {
			floyd_index = roadPoints.indexOf(PointByFloyd.get(i));
			path1 = globalFloydUtil.findTheRoad(floyd_index, r_index);

			if (path1.contains(r1_index) && path1.contains(r2_index)) {
				continue;
			} else {
				temp2 = PointByFloyd.get(i);
				min = dis.get(i);
				break;

			}
		}
		// 找到folyd点对应的两个路径段（特殊情况会多于2个）
		ArrayList<RoadEdge> deuxEdge = new ArrayList<RoadEdge>();
		int roadEdgeNumber = roadEdges.size();
		RoadEdge tempEdge = null;
		for (int m = 0; m < roadEdgeNumber; m++) {
			tempEdge = roadEdges.get(m);
//			System.out.println("调试"+m);
			if (tempEdge.getId1() == temp2.getId()) {
				deuxEdge.add(tempEdge);
			} else if (tempEdge.getId2() == temp2.getId()) {
				deuxEdge.add(tempEdge);
			}
		}

		// 筛选folyd点对应的两个路径段
		roadEdgeNumber = deuxEdge.size();

		RoadEdge bestEdge = null;
		double tempdis = 100;// 设置一个较大的值
		double dis1 = 0;
		for (int m = 0; m < roadEdgeNumber; m++) {
			tempEdge = deuxEdge.get(m);
			// 判断路径端点1
			if (tempEdge.getId1() != temp2.getId()) {
				// 两路径段相邻
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
			// 判断路径端点2
			if (tempEdge.getId2() != temp2.getId()) {
				// 两路径段相邻
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
		// 在此情况下只返回一个点,因为point是线段端点
		Point point = new Point(temp2.getLongitude(), temp2.getLatitude());
		return getPointsInSegment(min, bestEdge.getP1(), bestEdge.getP2(), point);
	}

	/**
	 * 已知路径段和路径段上一点，在路径段上寻找距离该点距离为s的另一点 找到返回Point数组,否则返回null
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
		// 下面判断P1，P2是否在线段上（两向量乘积小于等于零，则在线段上）
		// 判断p1
		Point v1 = new Point(start.x - p1.x, start.y - p1.y);
		Point v2 = new Point(end.x - p1.x, end.y - p1.y);
		if (v1.x * v2.x + v1.y * v2.y > 0)
			p1 = null;
		// 判断p2
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
	 * 已知路径段,上寻找距离路径段接近距离为s的点 （每个路近段端点对应两个） 找到返回Point数组,否则返回null
	 * 
	 * @return
	 */
	public ArrayList<RoadPoint> getPointsByFloyd(RoadPoint p1, double s) {
		int start = roadPoints.indexOf(p1);
		ArrayList<Integer> result1 = globalFloydUtil.findNearstPoint(start, s);

		ArrayList<RoadPoint> result = new ArrayList<RoadPoint>();

		for (int i = 0; i < result1.size(); i++) {

			result.add(roadPoints.get(result1.get(i)));
		}

		return result;
	}

	/**
	 * 已知路径段,上寻找距离路径段接近距离为s的点的距离值 （每个路近段端点对应两个） 找到返回double距离值数组,否则返回null
	 * 
	 * @return
	 */
	public ArrayList<Double> getPointDistanceByFloyd(RoadPoint p1, double s) {
		int start = roadPoints.indexOf(p1);
		return globalFloydUtil.findNearstDis(start, s);
	}

	public double getDisBeyweenTwoPointByFloyd(RoadPoint p1, RoadPoint p2) {
		int start = roadPoints.indexOf(p1);
		int end = roadPoints.indexOf(p2);
		return globalFloydUtil.findDis(start, end);
	}

	/**
	 * 二维高斯分布，0均值，x，y方向方差相等
	 */
//	double sigma = 10 / (Math.PI * 6371393 / 180);
	double sigma = 10;

	public double BiDimensionGauss(double x, double y) {
		x=x*111000;
		y=y*111000;
		
		return 1.0 / (2.0 * Math.PI * Math.pow(sigma, 2))
				* Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2.0 * Math.pow(sigma, 2)));
		
//		return 1.0 / (2.0 * Math.PI * Math.pow(sigma, 2))
//				* Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2.0 * Math.pow(sigma, 2)));
	}

	/**
	 * 使用序列化手段，完成轨迹点的复制
	 */
	public static <T> ArrayList<T> deepCopy(ArrayList<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);

		ArrayList<T> copy_list = (ArrayList<T>) in.readObject();
		return copy_list;
	}
}
