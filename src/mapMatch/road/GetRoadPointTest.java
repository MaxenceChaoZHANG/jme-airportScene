package mapMatch.road;
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.dom4j.DocumentException;
import org.math.plot.Plot2DPanel;

import dataEntity.Road;
import dataEntity.RoadEdge;
import dataEntity.RoadPoint;
import dataEntity.Track;
import dataEntity.TrackPoint;

public class GetRoadPointTest {

		
		public static void main(String[] args) {
			
			
	        Road road=null;
				try {
					road=RoadXMLUtility.ReadXML();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			ArrayList<RoadPoint> roadPoints=road.getRoadPoints();
			

			System.out.println(roadPoints.size());
			
			FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter("xml/roadPoint.txt");//�����ı��ļ�
				for(int i=0;i<roadPoints.size();i++){
					fileWriter.write(roadPoints.get(i).toString()+"\r\n");//д�� \r\n����
				}
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		    
		}
}


