package ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.*;
import org.dom4j.io.*;

import dataEntity.AirportInfo;

public class XMLUtility {
	
	
	/**
     * 读取XML文档
     */
	public static ArrayList<AirportInfo> ReadXML() throws DocumentException {
	    //1. 创建DOM4J解析器对象
	    SAXReader reader = new SAXReader();
	   
	    Document doc = reader.read(new File("xml/AirportInfo.xml"));
	    Element rootElement = doc.getRootElement();
	    Iterator<Element> iter1 = rootElement.elementIterator();

	    ArrayList<AirportInfo> airports = new ArrayList<>();
	    AirportInfo airport = null;

	    while (iter1.hasNext()) {
	    	airport = new AirportInfo();
	        Element element = iter1.next();
	        airport.setId(  Integer.parseInt( element.attributeValue("airportId").trim() )  );
	        airport.setName( element.elementText("name"));
	        airport.setICAO_Code(element.elementText("icao_code"));
	        airport.setLontitude(Float.parseFloat(element.elementText("lontitude")));
	        airport.setLatitude(Float.parseFloat(element.elementText("latitude")));
	        airport.setFilePath(element.elementText("file_path"));
	       
	        airports.add(airport);
	    }
	    for (AirportInfo air : airports) {
	        System.out.println(air);
	    }
		return airports;

	}
	
	/**
     * 读取XML文档
     * @param id 根据id查找机场
     */
	public static AirportInfo ReadXML(int id) throws DocumentException {

	    SAXReader reader = new SAXReader();
	    Document doc = reader.read(new File("xml/AirportInfo.xml"));
	    
	    Element rootElement = doc.getRootElement();
	    Iterator<Element> iter1 = rootElement.elementIterator();

	    AirportInfo airport = null;

	    while (iter1.hasNext()) {
	        Element element = iter1.next();
	        if(  id ==  Integer.parseInt( element.attributeValue("airportId").trim())  )
	        {
	        	airport=new AirportInfo();
		        airport.setId(Integer.parseInt( element.attributeValue("airportId").trim()) );
		        airport.setName( element.elementText("name"));
		        airport.setICAO_Code(element.elementText("icao_code"));
		        airport.setLontitude(Float.parseFloat(element.elementText("lontitude")));
		        airport.setLatitude(Float.parseFloat(element.elementText("latitude")));
		        airport.setFilePath(element.elementText("file_path"));
		        break;
	        }
	    }

	    System.out.println(airport);

		return airport;

	}
	
	/**
     * 更新XML节点
     * @param id 根据id查找对应节点
     */
    public static void UpdateXML(AirportInfo airport) throws DocumentException, IOException{
        //创建Document对象，读取已存在的Xml文件person.xml
        Document doc=new SAXReader().read(new File("xml/AirportInfo.xml"));
        Element rootElement = doc.getRootElement();
        Iterator<Element> iter1 = rootElement.elementIterator();

        while (iter1.hasNext()) {
      
	        Element element = iter1.next();
	        if(  airport.getId() ==  Integer.parseInt( element.attributeValue("airportId").trim())  )
	        {
		        element.element("name").setText(airport.getName());
		        element.element("icao_code").setText(airport.getICAO_Code());
		        element.element("lontitude").setText(airport.getLontitude()+"");
		        element.element("latitude").setText(airport.getLatitude()+"");
		        element.element("file_path").setText(airport.getFilePath());
		        
		        break;
	        }
	    }

        //指定文件输出的位置
        FileOutputStream out =new FileOutputStream("xml/AirportInfo.xml");
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
    
    /**
     * 增加：文档、标签、属性 
     * @param airportName  机场名字
     * @param icao_code  机场代码
     * @param lon  机场名字
     * @param lat  机场名字
     * @param filepath  地景文件路径
     */
    public static void AddXML(AirportInfo airportInfo) throws DocumentException, IOException {
    	Document doc=new SAXReader().read(new File("xml/AirportInfo.xml"));
    	
        Element rootElement = doc.getRootElement();
        //遍历已有airport,寻找最大id
        Iterator<Element> iter1 = rootElement.elementIterator();
        int id=0;
        while (iter1.hasNext()) {
      
	        Element element = iter1.next();
	        if(  id <  Integer.parseInt( element.attributeValue("airportId").trim())  )
	        {
		        id=Integer.parseInt( element.attributeValue("airportId").trim());
	        }
	    }
        //设置id为当前id值加1
        id++;
    	//添加airport根节点
        Element airport=rootElement.addElement("airport");
        airport.addAttribute("airportId", id+"");
	        //添加名字子节点
	        Element name=airport.addElement("name");
	        name.addText(airportInfo.getName());
	        //添加名字子节点
	        Element icao=airport.addElement("icao_code");
	        icao.addText(airportInfo.getICAO_Code());
	         //添加名字子节点
	        Element lon=airport.addElement("lontitude");
	        lon.addText(airportInfo.getLontitude()+"");
	        //添加名字子节点
	        Element lat=airport.addElement("latitude");
	        lat.addText(airportInfo.getLatitude()+"");
	      //添加名字子节点
	        Element path=airport.addElement("file_path");
	        path.addText(airportInfo.getFilePath());
     
        //指定文件输出的位置
        FileOutputStream out =new FileOutputStream("xml/AirportInfo.xml"); 
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

	/**
     * 删除XML节点
     * @param id  根据id删除
     */
    public static void DeleteXML(int id) throws DocumentException, IOException{
        //创建Document对象，读取已存在的Xml文件person.xml
        Document doc=new SAXReader().read(new File("xml/AirportInfo.xml"));

        Element rootElement = doc.getRootElement();
        Iterator<Element> iter1 = rootElement.elementIterator();
        
        while (iter1.hasNext()) {
	        Element element = iter1.next();
	        if(  id ==  Integer.parseInt( element.attributeValue("airportId").trim())  )
	        {
	           element.detach();
	           break;
	        }
        }
        //指定文件输出的位置
        FileOutputStream out =new FileOutputStream("xml/AirportInfo.xml");
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

				try {
					//DeleteXML(2);
//					AirportInfo airport=new AirportInfo(4, "上海虹桥国际机场", "ZSSS",121.328559644f,31.203323129f, "xiuxiu");
//					UpdateXML(airport);
					ReadXML();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
//					catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
	}
}
