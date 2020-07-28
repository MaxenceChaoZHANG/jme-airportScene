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
	     * 读取OMS-XML文档
	     */
		public static Road ReadXML() throws DocumentException {
			//创建geoHashHepler对象
//			GeoHashHelper geoHash = new GeoHashHelper();
			MyGeoHashHelper geoHash = new MyGeoHashHelper();
		    //创建DOM4J解析器对象
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
	            //如果为node,则记录路径点（roadPoint)
		        if(element.getName().equals("node")){
			    	roadPoint = new RoadPoint();
			    	roadPoint.setId(Long.parseLong(element.attributeValue("id")));
			    	roadPoint.setLatitude(Double.parseDouble(element.attributeValue("lat"))); 
			        roadPoint.setLongitude(Double.parseDouble(element.attributeValue("lon"))); 
			        roadPoint.setGeoHash(geoHash.encodeWith7Char(roadPoint.getLatitude(), roadPoint.getLongitude()));
//			        roadPoint.setGeoHash(geoHash.encode(roadPoint.getLatitude(), roadPoint.getLongitude()));
			        road.addRoadPoint(roadPoint);
			    }
		        
		        //如果为way，则记录路径边（roadEdge)
		        if(element.getName().equals("way")){
			        Element tag=element.element("tag");
			        //通过tag标签判断是否为滑行道、跑道
			        if(  (tag!=null)&&(tag.attributeValue("k").equals("aeroway"))  ) {
			        	//遍历路径点，创建路径边
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
			        	   }//结束内层for
			        	}
			        road.addRoadWay(roadWay);
			      }//结束way判断
		        
		    }//结束外层while
			return road;

		}
		
		/**
	     * 根据roadPoint的id获得roadPoint对象
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
	     * 删除OSM-XML无关节点
	     */
	    public static void DeleteXML(Road road) throws DocumentException, IOException{
	        //创建Document对象，读取已存在的Xml文件roadmap.xml
	        Document doc=new SAXReader().read(new File("xml/roadmap.xml"));

	        Element rootElement = doc.getRootElement();
	        Iterator<Element> iter1 = rootElement.elementIterator();

	    	ArrayList<RoadPoint> roadPoints=road.getRoadPoints();

		    while (iter1.hasNext()) {
		    	Element element = iter1.next(); 
	            //如果为node,则记录路径点（roadPoint)
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
		        
		        //如果为way，则记录路径边（roadEdge)
		        if(element.getName().equals("way")){
		        	Boolean exist=false;
			        Element tag=element.element("tag");
			        //通过tag标签判断是否为滑行道、跑道
			        if(  (tag!=null)&&(tag.attributeValue("k").equals("aeroway"))  )
			          exist=true;
			        
			        if(!exist)
			        element.detach();
			      }//结束way判断

		        
		      //如果为relation，则记录路径边（roadEdge)
		        if(element.getName().equals("relation")){
			        element.detach();
			      }//结束relation判断
		        
		    }//结束外层while
	        
	        //指定文件输出的位置
	        FileOutputStream out =new FileOutputStream("xml/roadTest.xml");
	        // 指定文本的写出的格式：
	        OutputFormat format=OutputFormat.createPrettyPrint();   //漂亮格式：有空格换行
	        format.setEncoding("UTF-8");
	        //1.创建写出对象
	        XMLWriter writer=new XMLWriter(out,format);
	        //2.写出Document对象
	        writer.write(doc);
	        //3.关闭流
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

