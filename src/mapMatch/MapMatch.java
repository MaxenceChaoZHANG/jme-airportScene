package mapMatch;

import java.util.ArrayList;
import java.util.List;
import dataEntity.RoadEdge;
import dataEntity.RoadPoint;
import dataEntity.RoadWay;
import dataEntity.TrackPoint;
import mapMatch.geoHash.GeoHashHelper;
import mapMatch.geoHash.MyGeoHashHelper;

public class MapMatch {
	
	public static ArrayList<TrackPoint> SimpleMapMatch(ArrayList<RoadPoint> roadPoints,ArrayList<RoadEdge> roadEdges,ArrayList<TrackPoint> trackPoints) {
		//创建geoHashHepler对象
		GeoHashHelper geoHash = new GeoHashHelper();
//		MyGeoHashHelper geoHash = new MyGeoHashHelper();
		List<String> geoHashCode=null;
		
		
		//遍历每一个轨迹点进行地图匹配
		int trackPointNumber=trackPoints.size();
		TrackPoint temp1=null;
		for(int i=0;i<trackPointNumber;i++) {
			//获取当前点附近的9个geohashCode
			temp1=trackPoints.get(i);
//			geoHashCode=geoHash.aroundWith7Char(temp1.getLatitude(), temp1.getLongitude()) ;
			geoHashCode=geoHash.around(temp1.getLatitude(), temp1.getLongitude()) ;
			
			//遍历每一个路网点，根据geoHash寻找距离当前轨迹点比较近的点,并存到集合中 
			ArrayList<RoadPoint> neighborPoints = new ArrayList<RoadPoint>();
			int roadPointNumber=roadPoints.size();
			RoadPoint temp2=null;
			for(int k=0;k<roadPointNumber;k++) {
				 temp2=roadPoints.get(k);
				
				if(temp2.getGeoHash().equals(geoHashCode.get(0))){
					neighborPoints.add(temp2);
					continue;
				}
				if(temp2.getGeoHash().equals(geoHashCode.get(1))){
					neighborPoints.add(temp2);
					continue;
				}
				if(temp2.getGeoHash().equals(geoHashCode.get(2))){
					neighborPoints.add(temp2);
					continue;
				}
				if(temp2.getGeoHash().equals(geoHashCode.get(3))){
					neighborPoints.add(temp2);
					continue;
				}
				if(temp2.getGeoHash().equals(geoHashCode.get(4))){
					neighborPoints.add(temp2);
					continue;
				}
				if(temp2.getGeoHash().equals(geoHashCode.get(5))){
					neighborPoints.add(temp2);
					continue;
				}
				if(temp2.getGeoHash().equals(geoHashCode.get(6))){
					neighborPoints.add(temp2);
					continue;
				}
				if(temp2.getGeoHash().equals(geoHashCode.get(7))){
					neighborPoints.add(temp2);
					continue;
				}
				if(temp2.getGeoHash().equals(geoHashCode.get(8))){
					neighborPoints.add(temp2);
					continue;
				}

			}//得到距离当前轨迹点比较近的点的集合中
			
			//根据比较近的路径点寻找附近的路近段，并存到集合中
			ArrayList<RoadEdge> neighborEdges = new ArrayList<RoadEdge>();
			
			int neighborPointNumber=neighborPoints.size();
//			System.out.println(i+"当前匹配路径点数目："+neighborPointNumber);
			if(neighborPointNumber==0) {
				System.out.println("匹配失败："+i+" "+temp1.getLongitude()+","+temp1.getLatitude());
				continue;
			}
				
			RoadPoint temp3=null;
			for(int j=0;j<neighborPointNumber;j++) {
			    temp3=neighborPoints.get(j);
				
				int roadEdgeNumber=roadEdges.size();
				RoadEdge temp4=null;
				for(int m=0;m<roadEdgeNumber;m++ ) {
					temp4 = roadEdges.get(m);
					if(temp4.getId1()==temp3.getId())
						neighborEdges.add(temp4);
					if(temp4.getId2()==temp3.getId())
						neighborEdges.add(temp4);
				}

			}//得到附近的路近段
			
			//根据附近路径段开始匹配
			int neighborRoadEdgeNumber = neighborEdges.size();
			double miniDistance=10000;
			RoadEdge temp5=null;
			RoadPoint r1 = null;
			RoadPoint r2 =null;
//			System.out.println(i+"当前匹配路径段数目："+neighborRoadEdgeNumber);
//			System.out.println(neighborRoadEdgeNumber);

			for(int n=0;n<neighborRoadEdgeNumber;n++) {
				r1 = neighborEdges.get(n).getP1();
				r2 = neighborEdges.get(n).getP2();
				double distance = MatchUtility.getPointToSegDistance(r1, r2, temp1);
				if(miniDistance > distance) {
					miniDistance=distance;
					temp5=neighborEdges.get(n);
//					System.out.println(n);
				}
			}//得到最匹配路近段


			System.out.println("before"+trackPoints.get(i).getLongitude()+" "+trackPoints.get(i).getLatitude());
			TrackPoint matchResult = MatchUtility.getFoot(temp5.getP1(), temp5.getP2(), temp1);

			System.out.println("数组中"+trackPoints.get(i).getLongitude()+" "+trackPoints.get(i).getLatitude());
			


		}
	
		return trackPoints;
	
	}
	
	public static void main(String[] args) {
		//测试
//		ArrayList<RoadPoint> test = new ArrayList<RoadPoint>();
//		test.add(new RoadPoint(1,2,3));
//		test.add(new RoadPoint(2,3,4));
//		RoadPoint temp=test.get(0);
//		temp.setLatitude(5);;
//		System.out.println(test.get(0)+" "+temp);
		
		String test="117.34716475009918";
		System.out.println(Double.parseDouble(test));
		
		
	}

}
