package mapMatch.road;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.dom4j.DocumentException;
import org.math.plot.Plot2DPanel;

import dataEntity.Road;
import dataEntity.RoadEdge;
import dataEntity.RoadPoint;
import dataEntity.RoadWay;
import dataEntity.Track;
import dataEntity.TrackPoint;

public class RoadPlotTest {

	public static void main(String[] args) {

		ArrayList<Track> tracks = null;
		try {
			tracks = pathSmooth.ReadData.readData("track/Track_13_taxi_79a03c.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Road road = null;
		try {
			road = RoadXMLUtility.ReadXML();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<RoadPoint> roadPoints = road.getRoadPoints();
		ArrayList<RoadWay> roadWays = road.getRoadWays();

		Color red = new Color(255, 0, 0);
		Color black = new Color(0, 0, 0);
		Color blue = new Color(0, 0, 255);

		// create a plot panel
		Plot2DPanel plot = new Plot2DPanel();
		// 遍历路径边，绘制路径边
		int wayNumber = roadWays.size();
		RoadWay tempWay = null;
		for (int i = 0; i < wayNumber; i++) {
			tempWay = roadWays.get(i);
			ArrayList<RoadEdge> tempEdges = tempWay.getRoadEdges();
			int edgeNumber = tempEdges.size();
			RoadEdge tempEdge = null;
//			int mid=(int) Math.ceil(edgeNumber/2.0);
			for (int j = 0; j < edgeNumber; j++) {
				tempEdge = tempEdges.get(j);
				RoadPoint roadPoint1 = tempEdge.getP1();
				RoadPoint roadPoint2 = tempEdge.getP2();
				double[] point1 = { roadPoint1.getLongitude(), roadPoint1.getLatitude() };
				double[] point2 = { roadPoint2.getLongitude(), roadPoint2.getLatitude() };

//				if(j==mid) {
//					plot.addLabel(tempWay.getId()+"", black, point1);
//				}
				plot.addLinePlot("", black, point1, point2);
//				 //绘制路径点
//				 point1=new double[]{roadPoint1.getLongitude()};
//				 point2=new double[]{roadPoint1.getLatitude()};
//				 plot.addScatterPlot("",blue,point1,point2);
//				 point1=new double[]{roadPoint2.getLongitude()};
//				 point2=new double[]{roadPoint2.getLatitude()};
//				 plot.addScatterPlot("",blue,point1,point2);
			}
		}

		double[] point1 = null;
		double[] point2 = null;

		ArrayList<TrackPoint> trackPoints = tracks.get(0).getTrackPoints();
		for (int i = 0; i < trackPoints.size() - 1; i++) {
			TrackPoint Point1 = trackPoints.get(i);
			TrackPoint Point2 = trackPoints.get(i + 1);
			point1 = new double[] { Point1.getLongitude(), Point1.getLatitude() };
			point2 = new double[] { Point2.getLongitude(), Point2.getLatitude() };

			plot.addLinePlot("", red, point1, point2);
		}

		plot.addLinePlot("", blue, point1, point2);
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
