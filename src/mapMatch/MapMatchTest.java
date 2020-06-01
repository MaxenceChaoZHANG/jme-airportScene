package mapMatch;

import java.io.IOException;
import java.util.ArrayList;
import dataEntity.*;
import mapMatch.road.RoadXMLUtility;
import java.awt.Color;
import javax.swing.JFrame;
import org.dom4j.DocumentException;
import org.math.plot.Plot2DPanel;


public class MapMatchTest {
		
		public static void main(String[] args) {
			//读取轨迹数据
			ArrayList<Track> tracks=null;
			try {
				tracks=pathSmooth.ReadData.readData("track/Track_de_1.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//读取路网数据
	        Road road=null;
				try {
					road=RoadXMLUtility.ReadXML();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			ArrayList<RoadPoint> roadPoints=road.getRoadPoints();
			ArrayList<RoadWay> roadWays = road.getRoadWays();
			
			//从RoadWay中抽取所有的RoadEdge
			ArrayList<RoadEdge> roadEdges=new ArrayList<RoadEdge>();
			int wayNumber=roadWays.size();
			RoadWay tempWay=null;
			for(int i=0;i<wayNumber;i++) {
				roadEdges.addAll(roadWays.get(i).getRoadEdges());
			}

		    Color red = new Color(255, 0, 0);
			Color black = new Color(0, 0, 0);
			Color blue = new Color(0, 0, 255);
			
			//create a plot panel
			Plot2DPanel plot = new Plot2DPanel();
			// 遍历路径边，绘制路径边
			RoadPoint roadPoint1=null;
			RoadPoint roadPoint2=null;
			for (int i = 0; i < roadEdges.size(); i++) {
				roadPoint1=roadEdges.get(i).getP1();
				roadPoint2=roadEdges.get(i).getP2();		
				double[] point1= {roadPoint1.getLongitude(),roadPoint1.getLatitude()};
				double[] point2= {roadPoint2.getLongitude(),roadPoint2.getLatitude()};	

				plot.addLinePlot("", black, point1, point2);	
			}
			
			
			
			ArrayList<TrackPoint> trackPoints=tracks.get(0).getTrackPoints();
			for (int i = 0; i < trackPoints.size()-1; i++) {
				TrackPoint Point1 = trackPoints.get(i);
				TrackPoint Point2 = trackPoints.get(i+1);		
				double[] point1= {Point1.getLongitude(),Point1.getLatitude()};
				double[] point2= {Point2.getLongitude(),Point2.getLatitude()};	

				plot.addLinePlot("", red, point1, point2);	
			}

			
			trackPoints=MapMatch.SimpleMapMatch(roadPoints, roadEdges, tracks.get(0).getTrackPoints());
			for (int i = 0; i < trackPoints.size()-1; i++) {
				TrackPoint Point1 = trackPoints.get(i);
				TrackPoint Point2 = trackPoints.get(i+1);		
				double[] point1= {Point1.getLongitude(),Point1.getLatitude()};
				double[] point2= {Point2.getLongitude(),Point2.getLatitude()};	

				plot.addLinePlot("", blue, point1, point2);	
			}

			//set the label of the plot panel
		    plot.setAxisLabel(0," longitude");
		    plot.setAxisLabel(1, "latuitude");
		    
		    //set the boundary of the plot panel
//		    double boundMin1[]  = {0, 0};
//		    double boundMax1[]  = {200, 200};
//		    plot.setFixedBounds(boundMin1, boundMax1);	
		    plot.setSize(1200, 1200);
		    //create a frame
		    JFrame frame = new JFrame("A plot test");
		    
		    //set the size of the frame
		    frame.setSize(1200, 1200);
		    
		    //set the content of the frame as the plot panel
		    frame.setContentPane(plot);
		    frame.setVisible(true);
		}

}


