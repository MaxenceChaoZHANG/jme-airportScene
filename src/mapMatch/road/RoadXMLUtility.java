package mapMatch.road;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.*;
import org.dom4j.io.*;

import dataEntity.*;
import mapMatch.geoHash.*;

public class RoadXMLUtility {		
		/**
	     * ��ȡOMS-XML�ĵ�
	     */
		public static Road ReadXML() throws DocumentException {
			//����geoHashHepler����
//			GeoHashHelper geoHash = new GeoHashHelper();
			MyGeoHashHelper geoHash = new MyGeoHashHelper();
		    //����DOM4J����������
		    SAXReader reader = new SAXReader();
		   
		    Document doc = reader.read(new File("xml/roadTest.xml"));
		    Element rootElement = doc.getRootElement();
		    Iterator<Element> iter1 = rootElement.elementIterator();

		    Road road = new Road();
		    RoadPoint roadPoint = null;
		    RoadWay roadWay = null;
		    RoadEdge roadEdge = null;

		    while (iter1.hasNext()) {
		    	Element element = iter1.next(); 
	            //���Ϊnode,���¼·���㣨roadPoint)
		        if(element.getName().equals("node")){
			    	roadPoint = new RoadPoint();
			    	roadPoint.setId(Long.parseLong(element.attributeValue("id")));
			    	roadPoint.setLatitude(Double.parseDouble(element.attributeValue("lat"))); 
			        roadPoint.setLongitude(Double.parseDouble(element.attributeValue("lon"))); 
			        roadPoint.setGeoHash(geoHash.encodeWith7Char(roadPoint.getLatitude(), roadPoint.getLongitude()));
//			        roadPoint.setGeoHash(geoHash.encode(roadPoint.getLatitude(), roadPoint.getLongitude()));
			        road.addRoadPoint(roadPoint);
			    }
		        
		        //���Ϊway�����¼·���ߣ�roadEdge)
		        if(element.getName().equals("way")){
			        Element tag=element.element("tag");
			        //ͨ��tag��ǩ�ж��Ƿ�Ϊ���е����ܵ�
			        if(  (tag!=null)&&(tag.attributeValue("k").equals("aeroway"))  ) {
			        	//����·���㣬����·����
			        	roadWay = new RoadWay(Long.parseLong(element.attributeValue("id")));
			        	List<Element> nds=element.elements("nd");
			        	for(int i=0;i<nds.size()-1;i++) {
			        	    roadEdge = new RoadEdge();	
			        	    long id1=Long.parseLong(nds.get(i).attributeValue("ref"));
			        	    long id2=Long.parseLong(nds.get(i+1).attributeValue("ref"));
			        	    roadEdge.setId1(id1);
			        	    roadEdge.setId2(id2); 
			        	    RoadPoint p1=getRoadPoint(road.getRoadPoints(),id1);
			        	    RoadPoint p2=getRoadPoint(road.getRoadPoints(),id2);
			        	    roadEdge.setP1(p1);
			        	    roadEdge.setP2(p2);
			        	    roadEdge.computeDist();
			        		roadWay.addRoadEdge(roadEdge);
			        	   }//�����ڲ�for
			        	}
			        road.addRoadWay(roadWay);
			      }//����way�ж�
		        
		    }//�������while
			return road;

		}
		
		/**
	     * ����roadPoint��id���roadPoint����
	     */
		public static RoadPoint getRoadPoint(ArrayList<RoadPoint> roadPoints,long id) {
			RoadPoint result=null;
			
			for(int i=0;i<roadPoints.size();i++) {
				if(roadPoints.get(i).getId()==id)
				{
					result=roadPoints.get(i);
					break;
				}
			}
			
			return result;
		}
	   
		/**
	     * ɾ��OSM-XML�޹ؽڵ�
	     */
	    public static void DeleteXML(Road road) throws DocumentException, IOException{
	        //����Document���󣬶�ȡ�Ѵ��ڵ�Xml�ļ�roadmap.xml
	        Document doc=new SAXReader().read(new File("xml/roadmap.xml"));

	        Element rootElement = doc.getRootElement();
	        Iterator<Element> iter1 = rootElement.elementIterator();

	    	ArrayList<RoadPoint> roadPoints=road.getRoadPoints();

		    while (iter1.hasNext()) {
		    	Element element = iter1.next(); 
	            //���Ϊnode,���¼·���㣨roadPoint)
		        if(element.getName().equals("node")){
		        	Boolean exist=false;
		        	for(int i=0;i<roadPoints.size();i++ )
		        	{
		        		if(roadPoints.get(i).getId()==Long.parseLong(element.attributeValue("id")) );
		        		exist=true;
		        	}
		        	if(!exist)
		        	element.detach();

			    }
		        
		        //���Ϊway�����¼·���ߣ�roadEdge)
		        if(element.getName().equals("way")){
		        	Boolean exist=false;
			        Element tag=element.element("tag");
			        //ͨ��tag��ǩ�ж��Ƿ�Ϊ���е����ܵ�
			        if(  (tag!=null)&&(tag.attributeValue("k").equals("aeroway"))  )
			          exist=true;
			        
			        if(!exist)
			        element.detach();
			      }//����way�ж�

		        
		      //���Ϊrelation�����¼·���ߣ�roadEdge)
		        if(element.getName().equals("relation")){
			        element.detach();
			      }//����relation�ж�
		        
		    }//�������while
	        
	        //ָ���ļ������λ��
	        FileOutputStream out =new FileOutputStream("xml/roadTest.xml");
	        // ָ���ı���д���ĸ�ʽ��
	        OutputFormat format=OutputFormat.createPrettyPrint();   //Ư����ʽ���пո���
	        format.setEncoding("UTF-8");
	        //1.����д������
	        XMLWriter writer=new XMLWriter(out,format);
	        //2.д��Document����
	        writer.write(doc);
	        //3.�ر���
	        writer.close();
	    }

		 public static void main(String[] args) {

//					try {
//						road=ReadXML();
//						DeleteXML(road);
//					} catch (DocumentException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//						catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
		}
}

