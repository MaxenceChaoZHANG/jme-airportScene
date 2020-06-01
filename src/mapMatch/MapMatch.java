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
		//����geoHashHepler����
		GeoHashHelper geoHash = new GeoHashHelper();
//		MyGeoHashHelper geoHash = new MyGeoHashHelper();
		List<String> geoHashCode=null;
		
		
		//����ÿһ���켣����е�ͼƥ��
		int trackPointNumber=trackPoints.size();
		TrackPoint temp1=null;
		for(int i=0;i<trackPointNumber;i++) {
			//��ȡ��ǰ�㸽����9��geohashCode
			temp1=trackPoints.get(i);
//			geoHashCode=geoHash.aroundWith7Char(temp1.getLatitude(), temp1.getLongitude()) ;
			geoHashCode=geoHash.around(temp1.getLatitude(), temp1.getLongitude()) ;
			
			//����ÿһ��·���㣬����geoHashѰ�Ҿ��뵱ǰ�켣��ȽϽ��ĵ�,���浽������ 
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

			}//�õ����뵱ǰ�켣��ȽϽ��ĵ�ļ�����
			
			//���ݱȽϽ���·����Ѱ�Ҹ�����·���Σ����浽������
			ArrayList<RoadEdge> neighborEdges = new ArrayList<RoadEdge>();
			
			int neighborPointNumber=neighborPoints.size();
//			System.out.println(i+"��ǰƥ��·������Ŀ��"+neighborPointNumber);
			if(neighborPointNumber==0) {
				System.out.println("ƥ��ʧ�ܣ�"+i+" "+temp1.getLongitude()+","+temp1.getLatitude());
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

			}//�õ�������·����
			
			//���ݸ���·���ο�ʼƥ��
			int neighborRoadEdgeNumber = neighborEdges.size();
			double miniDistance=10000;
			RoadEdge temp5=null;
			RoadPoint r1 = null;
			RoadPoint r2 =null;
//			System.out.println(i+"��ǰƥ��·������Ŀ��"+neighborRoadEdgeNumber);
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
			}//�õ���ƥ��·����


			System.out.println("before"+trackPoints.get(i).getLongitude()+" "+trackPoints.get(i).getLatitude());
			TrackPoint matchResult = MatchUtility.getFoot(temp5.getP1(), temp5.getP2(), temp1);

			System.out.println("������"+trackPoints.get(i).getLongitude()+" "+trackPoints.get(i).getLatitude());
			


		}
	
		return trackPoints;
	
	}
	
	public static void main(String[] args) {
		//����
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
