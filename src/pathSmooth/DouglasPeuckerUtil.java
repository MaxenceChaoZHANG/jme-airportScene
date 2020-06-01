package pathSmooth;

import java.io.IOException;
import java.util.ArrayList;

import dataEntity.Track;
import dataEntity.TrackPoint;

public class DouglasPeuckerUtil {
	
	/**
     * 主函数用于测试
     */
    public static void main(String[] args) {
		ArrayList<Track> tracks=new ArrayList<Track>();
		try {
		    tracks=ReadData.readData("C:\\Users\\HP\\Desktop\\工作报告\\Track_de_1.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		for(int i=0;i<tracks.size();i++) {
			
			ArrayList<TrackPoint> trackPoints =DouglasPeucker(tracks.get(i).getTrackPoints(),30);
			tracks.get(i).setTrackPoints(trackPoints);
		}
		for(int i=0;i<tracks.size();i++) {
			System.out.println(tracks.get(i));	
		}
    }
    
    /**
     * 道格拉斯佩格算法
     */
    public static ArrayList<TrackPoint> DouglasPeucker(ArrayList<TrackPoint> trackPoints, double epsilon) {
        // 找到最大阈值点，即操作（1）
        double maxH = 0;
        int index = 0;
        int end = trackPoints.size();
        for (int i = 1; i < end - 1; i++) {
            double h = DeviationToLine(trackPoints.get(i), trackPoints.get(0), trackPoints.get(end - 1));
            if (h > maxH) {
                maxH = h;
                index = i;
            }
        }

        // 如果存在最大阈值点，就进行递归遍历出所有最大阈值点
        ArrayList<TrackPoint> result = new ArrayList<TrackPoint>();
        if (maxH > epsilon) {
        	ArrayList<TrackPoint> leftPoints = new ArrayList<TrackPoint>();// 左曲线
        	ArrayList<TrackPoint> rightPoints = new ArrayList<TrackPoint>();// 右曲线
            // 分别提取出左曲线和右曲线的坐标点
            for (int i = 0; i < end; i++) {
                if (i <= index) {
                    leftPoints.add(trackPoints.get(i));
                    if (i == index)
                        rightPoints.add(trackPoints.get(i));
                } else {
                    rightPoints.add(trackPoints.get(i));
                }
            }

            // 分别保存两边遍历的结果
            ArrayList<TrackPoint> leftResult = new ArrayList<>();
            ArrayList<TrackPoint> rightResult = new ArrayList<>();
            leftResult = DouglasPeucker(leftPoints, epsilon);
            rightResult = DouglasPeucker(rightPoints, epsilon);

            // 将两边的结果整合
            rightResult.remove(0);//移除重复点
            leftResult.addAll(rightResult);
            result = leftResult;
        } else {// 如果不存在最大阈值点则返回当前遍历的子曲线的起始点
            result.add(trackPoints.get(0));
            result.add(trackPoints.get(end - 1));
        }
        return result;
    }

    /**
     * 计算点到直线的距离
     * 
     * @param trackPoint
     * @param trackPoint2
     * @param trackPoint3
     * @return
     */
    public static double DeviationToLine(TrackPoint trackPoint, TrackPoint trackPoint2, TrackPoint trackPoint3) {
    	double AB = distance(trackPoint2, trackPoint3);
    	double CB = distance(trackPoint, trackPoint2);
    	double CA = distance(trackPoint, trackPoint3);

    	double S = helen(CB, CA, AB);
    	double H = 2 * S / AB;

        return H;
    }

    /**
     * 计算两点之间的距离
     * 
     * @param p1
     * @param p2
     * @return
     */
    public static double distance(TrackPoint p1, TrackPoint p2) {
      
    	double[] xy1=Mercator.lonLat2Mercator(p1.getLongitude(), p1.getLatitude());
    	double[] xy2=Mercator.lonLat2Mercator(p2.getLongitude(), p2.getLatitude());
    	double distance= Math.sqrt(Math.pow(xy1[0]-xy2[0],2)+Math.pow(xy1[1]-xy2[1],2));
		return distance;    
 
    }

    /**
     * 海伦公式，已知三边求三角形面积
     * 
     * @param cB
     * @param cA
     * @param aB
     * @return 面积
     */
    public static double helen(double CB, double CA, double AB) {
    	double p = (CB + CA + AB) / 2;
    	double S =  Math.sqrt(p * (p - CB) * (p - CA) * (p - AB));
        return S;
    }
}